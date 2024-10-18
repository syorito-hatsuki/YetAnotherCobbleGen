package dev.syoritohatsuki.yacg.registry

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import dev.syoritohatsuki.yacg.common.block.GeneratorBlock
import dev.syoritohatsuki.yacg.util.BuildInGenerators
import dev.syoritohatsuki.yacg.config.GeneratorsManager
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object BlocksRegistry {
    val BLOCKS: MutableMap<Block, Identifier> = LinkedHashMap()

    init {
        BuildInGenerators.buildInGenerators.keys.forEach {
            GeneratorBlock(Identifier.of(MOD_ID, it)).create()
        }

        GeneratorsManager.dedicatedGenerators.forEach {
            GeneratorBlock(it).create()
        }

        BLOCKS.keys.forEach { block ->
            Registry.register(Registries.BLOCK, BLOCKS[block], block)
            Registry.register(Registries.ITEM, BLOCKS[block], BlockItem(block, Item.Settings()))
            ItemGroupEvents.modifyEntriesEvent(ItemGroupsRegistry.YACG_ITEM_GROUP).register {
                it.add(block)
            }
        }
    }

    private fun GeneratorBlock.create(): Block = this.apply {
        BLOCKS[this] = Identifier.of(MOD_ID, "${id.namespace}_${id.path}")
    }
}
