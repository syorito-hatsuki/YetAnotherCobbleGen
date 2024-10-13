package dev.syoritohatsuki.yacg.mixin.client.texture;

import dev.syoritohatsuki.yacg.client.texture.GeneratorSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteLoader;
import net.minecraft.client.texture.TextureStitcher;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(SpriteLoader.class)
public abstract class SpriteLoaderMixin {
    @Inject(method = "collectStitchedSprites", at = @At(value = "HEAD"))
    private void putGeneratorsSpriteContents(TextureStitcher<SpriteContents> stitcher, int atlasWidth, int atlasHeight, CallbackInfoReturnable<Map<Identifier, Sprite>> cir) {
        GeneratorSprite.INSTANCE.getGeneratorSpriteContents().forEach(stitcher::add);
    }
}
