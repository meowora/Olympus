package earth.terrarium.olympus.client.pipelines.renderer;

import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public record PipelineSubmit(
        RenderPipeline pipeline,
        MeshData mesh,
        int color,
        TextureSetup textures,
        Consumer<RenderPass> options
) implements SubmitNode {
    public static PipelineSubmitBuilder builder(RenderPipeline pipeline, MeshData data) {
        return new PipelineSubmitBuilder(pipeline, data);
    }

    @Override
    public @NonNull FeatureRendererType<? extends SubmitNode> featureType() {
        return PipelineSubmitFeatureRenderer.TYPE;
    }
}