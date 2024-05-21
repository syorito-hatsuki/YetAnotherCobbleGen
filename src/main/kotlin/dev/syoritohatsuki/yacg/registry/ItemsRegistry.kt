package dev.syoritohatsuki.yacg.registry

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import dev.syoritohatsuki.yacg.common.item.UpgradeItem
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ItemsRegistry {

    private val ITEMS: MutableMap<Item, Identifier> = LinkedHashMap()

    val COEFFICIENT_UPGRADE = UpgradeItem(UpgradeItem.UpgradesTypes.COEFFICIENT).create("upgrade_coefficient")
    val COUNT_UPGRADE = UpgradeItem(UpgradeItem.UpgradesTypes.COUNT).create("upgrade_count")
    val SPEED_UPGRADE = UpgradeItem(UpgradeItem.UpgradesTypes.SPEED).create("upgrade_speed")
    val ENERGY_FREE_UPGRADE = UpgradeItem(UpgradeItem.UpgradesTypes.ENERGY_FREE).create("upgrade_energy_free")

    init {
        ITEMS.keys.forEach { item ->
            Registry.register(Registries.ITEM, ITEMS[item], item)
            ItemGroupEvents.modifyEntriesEvent(ItemGroupsRegistry.YACG_ITEM_GROUP).register {
                it.add(item)
            }
        }
    }

    private fun Item.create(id: String): Item = this.apply {
        ITEMS[this] = Identifier(MOD_ID, id)
    }
}