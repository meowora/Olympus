package earth.terrarium.olympus.mixins;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import earth.terrarium.olympus.client.pipelines.OlympusPipelines;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPipelines.class)
public class RenderPipelinesMixin {

    @Shadow
    public static RenderPipeline registerOptional(RenderPipeline pipeline) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    public static RenderPipeline register(RenderPipeline pipeline) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerOlympusPipelines(CallbackInfo ci) {
        OlympusPipelines.register(RenderPipelinesMixin::register, RenderPipelinesMixin::registerOptional);
    }

}
