package dev.syoritohatsuki.yacg.registry

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object ItemGroupsRegistry {

    val YACG_ITEM_GROUP: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, MOD_ID))

    init {
        Registry.register(Registries.ITEM_GROUP, YACG_ITEM_GROUP, FabricItemGroup.builder().icon {
            ItemStack(BlocksRegistry.BLOCKS.keys.first())
        }.displayName(Text.literal(MOD_ID.uppercase())).build())
    }
}