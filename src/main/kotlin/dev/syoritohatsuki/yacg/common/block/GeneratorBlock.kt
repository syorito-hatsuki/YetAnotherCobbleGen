package dev.syoritohatsuki.yacg.common.block

import com.mojang.serialization.MapCodec
import dev.syoritohatsuki.yacg.common.block.entity.GeneratorBlockEntity
import dev.syoritohatsuki.yacg.common.item.UpgradeItem
import dev.syoritohatsuki.yacg.config.GeneratorsConfig
import dev.syoritohatsuki.yacg.message.generatorChancesTooltip
import dev.syoritohatsuki.yacg.message.hiddenTooltip
import dev.syoritohatsuki.yacg.registry.BlocksEntityRegistry
import dev.syoritohatsuki.yacg.registry.ItemsRegistry
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Property
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

open class GeneratorBlock(internal val type: String) :
    BlockWithEntity(Settings.create().strength(2f).requiresTool()), BlockEntityProvider {
    companion object {
        val ENABLED: BooleanProperty = Properties.ENABLED
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = createCodec {
        GeneratorBlock(type)
    }

    init {
        defaultState = ((stateManager.defaultState as BlockState).with(FACING, Direction.NORTH) as BlockState).with(
            ENABLED, true
        ) as BlockState
    }

    override fun appendTooltip(
        stack: ItemStack, context: Item.TooltipContext, tooltip: MutableList<Text>, options: TooltipType
    ) {
        super.appendTooltip(stack, context, tooltip, options)

        if (!Screen.hasShiftDown()) {
            tooltip.hiddenTooltip()
            return
        }

        GeneratorsConfig.getBlocks(type).forEach {
            tooltip.generatorChancesTooltip(it.coefficient, it.itemId)
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(FACING, ctx.horizontalPlayerFacing.opposite) as BlockState

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState = state.with(
        FACING, rotation.rotate(state.get(FACING) as Direction)
    ) as BlockState

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state.get(FACING) as Direction))

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(*arrayOf<Property<*>>(FACING, ENABLED))
    }

    /*   Block Entity   */
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun onStateReplaced(
        state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean
    ) {
        if (state.block != newState.block) {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is GeneratorBlockEntity) {
                ItemScatterer.spawn(world, pos, blockEntity)
                world.updateComparators(pos, this)
            }
            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }

    override fun neighborUpdate(
        state: BlockState, world: World, pos: BlockPos, sourceBlock: Block, sourcePos: BlockPos, notify: Boolean
    ) {
        val bl: Boolean = !world.isReceivingRedstonePower(pos)
        if (bl != state.get(ENABLED)) world.setBlockState(
            pos, state.with(HopperBlock.ENABLED, bl) as BlockState, 4
        )
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        val x = player.x
        val y = player.y
        val z = player.z
        (world.getBlockEntity(pos) as GeneratorBlockEntity).listUpgrades.forEach { type ->
            when (type) {
                UpgradeItem.UpgradesTypes.COEFFICIENT -> world.spawnEntity(
                    ItemEntity(world, x, y, z, ItemStack(ItemsRegistry.COEFFICIENT_UPGRADE))
                )

                UpgradeItem.UpgradesTypes.COUNT -> world.spawnEntity(
                    ItemEntity(world, x, y, z, ItemStack(ItemsRegistry.COUNT_UPGRADE))
                )

                UpgradeItem.UpgradesTypes.SPEED -> world.spawnEntity(
                    ItemEntity(world, x, y, z, ItemStack(ItemsRegistry.SPEED_UPGRADE))
                )

                UpgradeItem.UpgradesTypes.ENERGY_FREE -> world.spawnEntity(
                    ItemEntity(world, x, y, z, ItemStack(ItemsRegistry.ENERGY_FREE_UPGRADE))
                )
            }
        }
        return super.onBreak(world, pos, state, player)
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) (world.getBlockEntity(pos) as GeneratorBlockEntity).let { blockEntity ->
            if (player.isSneaking) blockEntity.items.forEachIndexed { index, itemStack ->
                world.spawnEntity(ItemEntity(world, player.x, player.y, player.z, itemStack))
                blockEntity.removeStack(index)
            } else {
                val message = Text.empty().append(
                    Text.literal("[").append(Text.translatable("block.yacg.$type")).append("]")
                        .formatted(Formatting.AQUA)
                )
                blockEntity.items.forEach {
                    if (it.item is UpgradeItem || it.item == Items.AIR) return@forEach
                    message.append("\n - ${it.item.name.string} x${it.count}")
                }
                player.sendMessage(message, false)
            }
        }

        return ActionResult.SUCCESS
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        GeneratorBlockEntity(pos, state, type)

    override fun <T : BlockEntity> getTicker(
        world: World, state: BlockState, type: BlockEntityType<T>
    ): BlockEntityTicker<T>? = validateTicker(type, BlocksEntityRegistry.GENERATOR_ENTITY, GeneratorBlockEntity::tick)

}