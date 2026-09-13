package earth.terrarium.olympus.client.pipelines;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;

import java.util.function.Consumer;

public class OlympusPipelines {

    public static void register(
            Consumer<RenderPipeline> requiredPipeline,
            Consumer<RenderPipeline> optionalPipeline
    ) {
        requiredPipeline.accept(RoundedRectangle.PIPELINE);
        requiredPipeline.accept(RoundedTexture.PIPELINE);
    }

}
