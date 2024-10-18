package dev.syoritohatsuki.yacg.integration.rei

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import dev.syoritohatsuki.yacg.config.GeneratorsManager
import dev.syoritohatsuki.yacg.message.generationRarityText
import dev.syoritohatsuki.yacg.registry.BlocksRegistry
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

object YacgReiPlugin : REIClientPlugin {

    val GENERATORS_CATEGORY: CategoryIdentifier<TableDisplay> = CategoryIdentifier.of(MOD_ID, "generators")
    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(TableCategory())
        registry.addWorkstations(GENERATORS_CATEGORY, *BlocksRegistry.BLOCKS.keys.map {
            EntryIngredients.of(it)
        }.toTypedArray())
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        GeneratorsManager.dedicatedGenerators.forEach { id ->
            registry.add(
                TableDisplay(EntryIngredients.of(
                    ItemStack(
                        Registries.ITEM.get(
                            Identifier.of(
                                MOD_ID, "${id.namespace}_${id.path}"
                            )
                        )
                    )
                ), GeneratorsManager.getItems(id).map { item ->
                    EntryIngredient.of(
                        EntryStacks.of(ItemStack(Registries.ITEM.get(Identifier.of(item.key)), item.value.count))
                            .tooltip(generationRarityText(item.value.weight))
                    )
                })
            )
        }
    }
}