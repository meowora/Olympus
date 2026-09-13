package earth.terrarium.olympus.client.pipelines.renderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import earth.terrarium.olympus.mixins.PictureInPictureRendererAccessor;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;

public record PipelineTarget(GpuTextureView texture, GpuTextureView depthTexture) {

    public PipelineTarget(RenderTarget target) {
        this(target.getColorTextureView(), target.getDepthTextureView());
    }

    public PipelineTarget(PictureInPictureRenderer<?> renderer) {
        this(((PictureInPictureRendererAccessor) renderer).olympus$textureView(), ((PictureInPictureRendererAccessor) renderer).olympus$depthTextureView());
    }
}
