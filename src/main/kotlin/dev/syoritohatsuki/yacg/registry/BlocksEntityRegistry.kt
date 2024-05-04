package dev.syoritohatsuki.yacg.registry

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator
import dev.syoritohatsuki.yacg.common.block.entity.GeneratorBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object BlocksEntityRegistry {
    val GENERATOR_ENTITY: BlockEntityType<GeneratorBlockEntity> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Identifier(YetAnotherCobblestoneGenerator.MOD_ID, "generator_block_entity"),
        BlockEntityType.Builder.create(::GeneratorBlockEntity, *BlocksRegistry.BLOCKS.keys.toTypedArray()).build()
    )
}