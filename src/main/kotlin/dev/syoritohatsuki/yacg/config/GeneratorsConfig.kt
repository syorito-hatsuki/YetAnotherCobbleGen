package dev.syoritohatsuki.yacg.config

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.logger
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.util.math.MathHelper
import java.io.File
import java.nio.file.Paths
import kotlin.math.roundToInt
import kotlin.system.exitProcess

object GeneratorsConfig {
    private val configDir: File = Paths.get("", "config", MOD_ID).toFile()
    private val configFile = File(configDir, "generators.json")

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private val defaultConfig = setOf(
        Generator(
            "cobble", 0, setOf(
                Generator.GenerateItem("minecraft:cobblestone", 100, 1),
                Generator.GenerateItem("minecraft:cobbled_deepslate", 30, 1),
                Generator.GenerateItem("minecraft:mossy_cobblestone", 10, 1),
            )
        ), Generator(
            "ore", 0, setOf(
                Generator.GenerateItem("minecraft:coal_ore", 100, 1),
                Generator.GenerateItem("minecraft:copper_ore", 70, 1),
                Generator.GenerateItem("minecraft:iron_ore", 50, 1),
                Generator.GenerateItem("minecraft:gold_ore", 30, 1),
                Generator.GenerateItem("minecraft:redstone_ore", 20, 1),
                Generator.GenerateItem("minecraft:lapis_ore", 20, 1),
                Generator.GenerateItem("minecraft:diamond_ore", 15, 1),
                Generator.GenerateItem("minecraft:emerald_ore", 10, 1),
                Generator.GenerateItem("minecraft:nether_quartz_ore", 5, 1),
            )
        ), Generator(
            "stone", 0, setOf(
                Generator.GenerateItem("minecraft:stone", 100, 1),
                Generator.GenerateItem("minecraft:diorite", 50, 1),
                Generator.GenerateItem("minecraft:granite", 50, 1),
                Generator.GenerateItem("minecraft:andesite", 50, 1),
                Generator.GenerateItem("minecraft:calcite", 20, 1),
                Generator.GenerateItem("minecraft:dripstone_block", 20, 1),
                Generator.GenerateItem("minecraft:deepslate", 5, 1),
            )
        )
    )

    init {
        if (!configDir.exists()) configDir.mkdirs()
        if (!configFile.exists()) configFile.writeText(json.encodeToString(defaultConfig))
    }

    fun getBlocks(type: String): Set<Generator.GenerateItem> = try {
        val generator = json.decodeFromString<Set<Generator>>(configFile.readText()).find { it.type == type }
        if (generator == null) throw RuntimeException("No generator found for $type")
        if (generator.items.size <= 1) throw RuntimeException("No items found for $type")
        generator.items.apply {
            map {
                it.coefficient = MathHelper.map(
                    it.coefficient.toFloat(),
                    generator.items.minOf { it.coefficient }.toFloat(),
                    generator.items.maxOf { it.coefficient }.toFloat(),
                    1F,
                    100F
                ).roundToInt()
            }.toSet()
        }
    } catch (e: Exception) {
        logger.error(e.localizedMessage)
        emptySet()
    }

    fun getTypes(): Set<String> = try {
        json.decodeFromString<Set<Generator>>(configFile.readText()).map { it.type }.toSet()
    } catch (e: Exception) {
        try {
            configFile.apply {
                copyTo(File(configDir, "backup_generators.json"))
                writeText(json.encodeToString(defaultConfig))
            }
            json.decodeFromString<Set<Generator>>(configFile.readText()).map { it.type }.toSet()
        } catch (e: Exception) {
            logger.error(e.localizedMessage)
            exitProcess(0)
        }
    }

    fun getEnergyUsage(type: String) = json.decodeFromString<Set<Generator>>(configFile.readText()).find {
        println("Q: $type | R: ${it.type}")
        it.type == type
    }?.energyUsage ?: 0

    @Serializable
    data class Generator(
        val type: String, val energyUsage: Int, val items: Set<GenerateItem> = emptySet()
    ) {
        @Serializable
        data class GenerateItem(
            val itemId: String, var coefficient: Int, val count: Int
        )
    }
}