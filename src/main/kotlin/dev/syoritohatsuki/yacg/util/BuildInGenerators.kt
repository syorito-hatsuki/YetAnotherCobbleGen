package dev.syoritohatsuki.yacg.util

import dev.syoritohatsuki.yacg.config.Generator

object BuildInGenerators {
    val buildInGenerators = mapOf(
        "cobble" to Generator(
            0, mapOf(
                "minecraft:cobblestone" to Generator.ItemSettings(100, 1),
                "minecraft:cobbled_deepslate" to Generator.ItemSettings(30, 1),
                "minecraft:mossy_cobblestone" to Generator.ItemSettings(10, 1),
            )
        ),
        "ore" to Generator(
            0, mapOf(
                "minecraft:coal_ore" to Generator.ItemSettings(100, 1),
                "minecraft:copper_ore" to Generator.ItemSettings(70, 1),
                "minecraft:iron_ore" to Generator.ItemSettings(50, 1),
                "minecraft:gold_ore" to Generator.ItemSettings(30, 1),
                "minecraft:redstone_ore" to Generator.ItemSettings(20, 1),
                "minecraft:lapis_ore" to Generator.ItemSettings(20, 1),
                "minecraft:diamond_ore" to Generator.ItemSettings(15, 1),
                "minecraft:emerald_ore" to Generator.ItemSettings(10, 1),
                "minecraft:nether_quartz_ore" to Generator.ItemSettings(5, 1),
            )
        ),
        "stone" to Generator(
            0, mapOf(
                "minecraft:stone" to Generator.ItemSettings(100, 1),
                "minecraft:diorite" to Generator.ItemSettings(50, 1),
                "minecraft:granite" to Generator.ItemSettings(50, 1),
                "minecraft:andesite" to Generator.ItemSettings(50, 1),
                "minecraft:calcite" to Generator.ItemSettings(20, 1),
                "minecraft:dripstone_block" to Generator.ItemSettings(20, 1),
                "minecraft:deepslate" to Generator.ItemSettings(5, 1),
            )
        ),
    )
}