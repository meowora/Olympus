package earth.terrarium.olympus.client.pipelines.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderer;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class PipelineSubmitFeatureRenderer implements FeatureRenderer<PipelineSubmit> {
    public static FeatureRendererType<PipelineSubmit> TYPE = FeatureRendererType.create("olympus:pipeline_renderer");

    private record Buffers(
            GpuBuffer vertex,
            GpuBuffer index,
            IndexType type
    ) {
        private static Buffers of(MeshData mesh, RenderPipeline pipeline, GpuDevice device) {
            GpuBuffer vertex = device.createBuffer(
                    () -> "Vertex data for: " + pipeline.getLocation(),
                    GpuBuffer.USAGE_VERTEX,
                    mesh.vertexBuffer());
            var indexBuffer = mesh.indexBuffer();
            if (indexBuffer == null) {
                var storage = RenderSystem.getSequentialBuffer(mesh.drawState().primitiveTopology());
                return new Buffers(
                        vertex,
                        storage.getBuffer(mesh.drawState().indexCount()),
                        storage.type()
                );
            }
            return new Buffers(
                    vertex,
                    device.createBuffer(
                            () -> "Vertex Index for: " + pipeline.getLocation(),
                            GpuBuffer.USAGE_INDEX,
                            indexBuffer),
                    mesh.drawState().indexType()
            );
        }
    }

    private static GpuBufferSlice getDynamicUniforms(int color) {
        return RenderSystem.getDynamicUniforms()
                .writeTransform(
                        RenderSystem.getModelViewMatrixCopy(),
                        new Vector4f(
                                ARGB.redFloat(color),
                                ARGB.greenFloat(color),
                                ARGB.blueFloat(color),
                                ARGB.alphaFloat(color)),
                        new Vector3f(),
                        new Matrix4f()
                );
    }

    @Override
    public void prepareGroup(
            @NonNull FeatureFrameContext context,
            @NonNull List<PipelineSubmit> pipelineSubmitNodes,
            boolean strictlyOrdered
    ) {
    }

    @Override
    public void executeGroup(
            @NonNull FeatureFrameContext context,
            @Nullable OitStage stage,
            @NonNull RenderPass pass,
            int groupIndex,
            List<PipelineSubmit> pipelineSubmitNodes,
            boolean strictlyOrdered
    ) {
        for (var pipelineSubmitNode : pipelineSubmitNodes) {
            try (var mesh = pipelineSubmitNode.mesh()) {
                var buffers = Buffers.of(mesh, pipelineSubmitNode.pipeline(), RenderSystem.getDevice());
                pass.setPipeline(RenderSystem.getCompiledPipeline(pipelineSubmitNode.pipeline()));

                var scissor = RenderSystem.getScissorStateForRenderTypeDraws();
                if (scissor.enabled()) {
                    pass.enableScissor(scissor.x(), scissor.y(), scissor.width(), scissor.height());
                }
                var textures = pipelineSubmitNode.textures();

                if (textures.texure0() != null) {
                    pass.setUniform("Sampler0", textures.texure0(), textures.sampler0());
                }
                if (textures.texure1() != null) {
                    pass.setUniform("Sampler1", textures.texure1(), textures.sampler1());
                }
                if (textures.texure2() != null) {
                    pass.setUniform("Sampler2", textures.texure2(), textures.sampler2());
                }

                var uniforms = getDynamicUniforms(pipelineSubmitNode.color());

                RenderSystem.bindDefaultUniforms(pass);
                pass.setUniform("DynamicTransforms", uniforms);

                pipelineSubmitNode.options().accept(pass);

                pass.setVertexBuffer(0, buffers.vertex().slice());
                pass.setIndexBuffer(buffers.index(), buffers.type());

                pass.drawIndexed(mesh.drawState().indexCount(), 1, 0, 0, 0);
            }
        }
    }
}
