package earth.terrarium.olympus.mixins;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PictureInPictureRenderer.class)
public interface PictureInPictureRendererAccessor {

    @Accessor("textureView")
    GpuTextureView olympus$textureView();
    @Accessor("depthTextureView")
    GpuTextureView olympus$depthTextureView();

}
