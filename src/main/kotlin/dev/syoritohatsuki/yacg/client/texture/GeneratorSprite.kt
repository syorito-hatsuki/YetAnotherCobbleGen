package dev.syoritohatsuki.yacg.client.texture

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator
import dev.syoritohatsuki.yacg.config.GeneratorsConfig
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.SpriteContents
import net.minecraft.client.texture.SpriteDimensions
import net.minecraft.resource.metadata.ResourceMetadata
import net.minecraft.util.Identifier

object GeneratorSprite {
    private const val WIDTH: Int = 16
    private const val HEIGHT: Int = 16
    private val sides = setOf("back", "bottom", "front", "left", "right", "top")
    private val customTypes = GeneratorsConfig.getCustomTypes()

    fun getGeneratorSpriteContents() = customTypes.map { type ->
        sides.map { side ->
            SpriteContents(
                Identifier.of(YetAnotherCobblestoneGenerator.MOD_ID, "block/${type}/${side}"),
                SpriteDimensions(WIDTH, HEIGHT),
                getNativeImage(type, side),
                ResourceMetadata.NONE
            )
        }
    }.flatten()

    private fun getNativeImage(type: String, side: String) = NativeImage.read(
        FabricLoader.getInstance()
            .configDir
            .resolve("yacg")
            .resolve(type)
            .resolve("$side.png")
            .toFile()
            .inputStream()
    )
}