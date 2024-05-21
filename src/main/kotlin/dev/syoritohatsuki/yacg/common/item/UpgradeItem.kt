package dev.syoritohatsuki.yacg.common.item

import dev.syoritohatsuki.yacg.common.block.entity.GeneratorBlockEntity
import dev.syoritohatsuki.yacg.message.upgradeItemTooltip
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.world.World

class UpgradeItem(val type: UpgradesTypes) : Item(Settings().maxCount(1)) {

    enum class UpgradesTypes {
        COEFFICIENT, COUNT, SPEED, ENERGY_FREE
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip.upgradeItemTooltip(type.name)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val entity = context.world.getBlockEntity(context.blockPos)
        if (entity is GeneratorBlockEntity) {
            val result = entity.insertUpgrade(type)
            if (result.isAccepted) context.stack.decrement(1)
            return result
        }

        return super.useOnBlock(context)
    }
}