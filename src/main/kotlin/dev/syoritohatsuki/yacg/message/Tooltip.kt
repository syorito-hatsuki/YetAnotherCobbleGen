package dev.syoritohatsuki.yacg.message

import dev.syoritohatsuki.yacg.common.item.UpgradeItem
import dev.syoritohatsuki.yacg.config.UpgradesConfig
import net.minecraft.client.util.InputUtil
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

fun MutableList<Text>.hiddenTooltip() = add(
    Text.translatable(
        "item.yacg.hidden.tooltip", InputUtil.fromKeyCode(
            GLFW.GLFW_KEY_LEFT_SHIFT, 0
        ).localizedText.copy().formatted(Formatting.AQUA)
    ).formatted(Formatting.DARK_GRAY)
)

fun MutableList<Text>.generatorChancesTooltip(coefficient: Int, itemId: String) = add(
    Text.literal(" - ").append(Registries.ITEM.get(Identifier.of(itemId)).name).append(Text.literal(" ["))
        .append(generationRarityText(coefficient)).append(Text.literal("]")).formatted(Formatting.DARK_GRAY)
)

fun MutableList<Text>.upgradeItemTooltip(type: String) = when {
    type.equals(
        UpgradeItem.UpgradesTypes.ENERGY_FREE.name, true
    ) -> add(Text.translatable("item.yacg.upgrade_${type.lowercase()}.tooltip").formatted(Formatting.YELLOW))

    else -> add(
        Text.translatable(
            "item.yacg.upgrade_${type.lowercase()}.tooltip",
            UpgradesConfig.getUpgradeModify(UpgradeItem.UpgradesTypes.valueOf(type)),
            '%'
        ).formatted(Formatting.YELLOW)
    )
}

fun generationRarityText(coefficient: Int): Text = when {
    coefficient >= 80 -> Text.literal("Common").formatted(Formatting.GREEN)
    coefficient in 31..79 -> Text.literal("Uncommon").formatted(Formatting.YELLOW)
    else -> Text.literal("Rare").formatted(Formatting.RED)
}