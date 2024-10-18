package dev.syoritohatsuki.yacg.client.texture

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator
import dev.syoritohatsuki.yacg.config.GeneratorsManager
import dev.syoritohatsuki.yacg.util.PathUtil
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.SpriteContents
import net.minecraft.client.texture.SpriteDimensions
import net.minecraft.resource.metadata.ResourceMetadata
import net.minecraft.util.Identifier

object GeneratorSprite {
    private const val WIDTH: Int = 16
    private const val HEIGHT: Int = 16
    private val sides = setOf("back", "bottom", "front", "left", "right", "top")

    fun getGeneratorSpriteContents() = GeneratorsManager.dedicatedGenerators.map { type ->
        sides.map { side ->
            SpriteContents(
                Identifier.of(YetAnotherCobblestoneGenerator.MOD_ID, "block/${type.namespace}_${type.path}/${side}"),
                SpriteDimensions(WIDTH, HEIGHT),
                getNativeImage(type, side),
                ResourceMetadata.NONE
            )
        }
    }.flatten()

    private fun getNativeImage(id: Identifier, side: String) = NativeImage.read(
        PathUtil.getNativeImagePath(id, side).toFile().inputStream()
    )
}