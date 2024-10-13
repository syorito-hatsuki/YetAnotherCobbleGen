package dev.syoritohatsuki.yacg.mixin.client.texture.atlas;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import dev.syoritohatsuki.yacg.client.texture.GeneratorSprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteOpener;
import net.minecraft.client.texture.atlas.AtlasLoader;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;

@Mixin(AtlasLoader.class)
public class AtlasLoaderMixin {
    @Inject(method = "loadSources", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;addAll(Ljava/lang/Iterable;)Lcom/google/common/collect/ImmutableList$Builder;"))
    private void loadGenerators(ResourceManager resourceManager, CallbackInfoReturnable<List<Function<SpriteOpener, SpriteContents>>> cir, @Local ImmutableList.Builder<Function<SpriteOpener, SpriteContents>> builder) {
        GeneratorSprite.INSTANCE.getGeneratorSpriteContents().forEach(spriteContents -> builder.add(spriteOpener -> spriteContents));
    }
}
