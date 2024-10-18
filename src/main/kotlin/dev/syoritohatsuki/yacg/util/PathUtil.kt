package dev.syoritohatsuki.yacg.util

import dev.syoritohatsuki.yacg.config.GeneratorsManager
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import java.nio.file.Path

object PathUtil {

    private val yacgRootPath: Path = FabricLoader.getInstance().configDir.resolve("yacg")
    val generatorsPath: Path = yacgRootPath.resolve("generators")

    fun getNativeImagePath(id: Identifier, side: String): Path =
        generatorsPath.resolve(id.namespace).resolve(id.path).resolve("textures").resolve("$side.png")

    fun getLangFilesPaths(langCode: String): List<Path> = GeneratorsManager.dedicatedGenerators.map {
        generatorsPath.resolve(it.namespace).resolve("$langCode.json")
    }
}