package dev.syoritohatsuki.yacg.integration.jade

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import dev.syoritohatsuki.yacg.common.block.entity.GeneratorBlockEntity
import dev.syoritohatsuki.yacg.common.item.UpgradeItem
import dev.syoritohatsuki.yacg.registry.ItemsRegistry
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import snownee.jade.api.*
import snownee.jade.api.config.IPluginConfig

object GeneratorComponentProvider : IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    override fun getUid(): Identifier = Identifier(MOD_ID, "generators")

    override fun appendServerData(data: NbtCompound, accessor: BlockAccessor) {
        data.putBoolean("coefficient", false)
        data.putBoolean("count", false)
        data.putBoolean("speed", false)
        data.putBoolean("energy_free", false)
        data.putBoolean("energy_required", false)

        if ((accessor.blockEntity as GeneratorBlockEntity).energyUsage <= 0) data.putBoolean("energy_required", true)
        (accessor.blockEntity as GeneratorBlockEntity).listUpgrades.forEach {
            when (it) {
                UpgradeItem.UpgradesTypes.COEFFICIENT -> data.putBoolean("coefficient", true)
                UpgradeItem.UpgradesTypes.COUNT -> data.putBoolean("count", true)
                UpgradeItem.UpgradesTypes.SPEED -> data.putBoolean("speed", true)
                UpgradeItem.UpgradesTypes.ENERGY_FREE -> data.putBoolean("energy_free", true)
            }
        }
    }

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val energy = tooltip.get(Identifiers.UNIVERSAL_ENERGY_STORAGE)
        val elements = tooltip.elementHelper

        // Remove the origin energy bar and add it later.
        // Require for sorting, maybe exist better way, but not in documentation ¯\_(ツ)_/¯
        tooltip.remove(Identifiers.UNIVERSAL_ENERGY_STORAGE)

        if (accessor.serverData.getBoolean("coefficient")) tooltip.append(
            elements.item(ItemStack(ItemsRegistry.COEFFICIENT_UPGRADE), 0.5f)
        )
        if (accessor.serverData.getBoolean("count")) tooltip.append(
            elements.item(ItemStack(ItemsRegistry.COUNT_UPGRADE), 0.5f)
        )
        if (accessor.serverData.getBoolean("speed")) tooltip.append(
            elements.item(ItemStack(ItemsRegistry.SPEED_UPGRADE), 0.5f)
        )
        if (accessor.serverData.getBoolean("energy_free")) tooltip.append(
            elements.item(ItemStack(ItemsRegistry.ENERGY_FREE_UPGRADE), 0.5f)
        )

        energy.takeIf {
            accessor.serverData.getBoolean("energy_free") || accessor.serverData.getBoolean("energy_required")
        }?.forEach {
            tooltip.add(it)
        }
    }
}