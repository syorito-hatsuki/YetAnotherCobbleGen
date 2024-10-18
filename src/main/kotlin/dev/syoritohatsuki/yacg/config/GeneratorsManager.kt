package dev.syoritohatsuki.yacg.config

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator
import dev.syoritohatsuki.yacg.util.BuildInGenerators
import dev.syoritohatsuki.yacg.util.PathUtil.generatorsPath
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.util.Identifier
import kotlin.io.path.*

object GeneratorsManager {

    private val generatorsPathRegex = Regex("^[a-z]+$")

    private val _dedicatedGenerators = mutableSetOf<Identifier>()
    val dedicatedGenerators: Set<Identifier> = _dedicatedGenerators

    private val generatorsSettings = mutableMapOf<Identifier, Generator>()

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    init {
        generateBuildInConfigs()
        registerCustomGenerators()
        loadSettings()
        _dedicatedGenerators.forEach {
            YetAnotherCobblestoneGenerator.logger.error(it.toString())
        }
    }

    private fun generateBuildInConfigs() {
        BuildInGenerators.buildInGenerators.keys.forEach { buildInGenerator ->
            generatorsPath.resolve(YetAnotherCobblestoneGenerator.MOD_ID).apply {
                if (notExists()) createDirectory()
                resolve(buildInGenerator).apply {
                    if (notExists()) createDirectory()
                    BuildInGenerators.buildInGenerators[buildInGenerator]?.let {
                        resolve("config.json").apply {
                            if (notExists()) {
                                createFile()
                                writeText(json.encodeToString(it))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun registerCustomGenerators() {
        generatorsPath.listDirectoryEntries().forEach { owner ->
            if (owner.name == YetAnotherCobblestoneGenerator.MOD_ID) return@forEach
            if (!owner.name.contains(generatorsPathRegex)) return@forEach

            owner.listDirectoryEntries().forEach generatorsForEach@{ generator ->
                if (!generator.name.contains(generatorsPathRegex)) return@generatorsForEach
                _dedicatedGenerators.add(Identifier.of(owner.name, generator.name))
            }
        }
    }

    private fun loadSettings() {
        generatorsPath.listDirectoryEntries().forEach { owner ->
            if (!owner.name.contains(generatorsPathRegex)) return@forEach

            owner.listDirectoryEntries().forEach generatorsForEach@{ generator ->
                if (!generator.isDirectory()) return@generatorsForEach
                generator.resolve("config.json").apply {
                    generatorsSettings[Identifier.of(owner.name, generator.name)] =
                        json.decodeFromString(this.readText())
                }
            }
        }
    }

    fun getItems(identifier: Identifier) = generatorsSettings[identifier]?.items ?: emptyMap()

    fun getEnergyUsage(identifier: Identifier) = generatorsSettings[identifier]?.energyUsage ?: 0
}