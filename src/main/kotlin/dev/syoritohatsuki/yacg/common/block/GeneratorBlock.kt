package dev.syoritohatsuki.yacg.common.block

import com.mojang.serialization.MapCodec
import dev.syoritohatsuki.yacg.common.block.entity.GeneratorBlockEntity
import dev.syoritohatsuki.yacg.common.item.UpgradeItem
import dev.syoritohatsuki.yacg.config.GeneratorsManager
import dev.syoritohatsuki.yacg.message.generatorChancesTooltip
import dev.syoritohatsuki.yacg.message.hiddenTooltip
import dev.syoritohatsuki.yacg.registry.BlocksEntityRegistry
import dev.syoritohatsuki.yacg.registry.ItemsRegistry
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.gui.screen.Screen
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.server.world.ServerWorld
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
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

open class GeneratorBlock(internal val id: Identifier) : BlockWithEntity(Settings.create().strength(2f).requiresTool()),
    BlockEntityProvider {
    companion object {
        val ENABLED: BooleanProperty = Properties.ENABLED
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = createCodec {
        GeneratorBlock(id)
    }

    init {
        defaultState = stateManager.defaultState
            .with(FACING, Direction.NORTH)
            .with(ENABLED, true)
    }

    override fun appendTooltip(
        stack: ItemStack, context: Item.TooltipContext, tooltip: MutableList<Text>, options: TooltipType
    ) {
        super.appendTooltip(stack, context, tooltip, options)

        if (!Screen.hasShiftDown()) {
            tooltip.hiddenTooltip()
            return
        }

        GeneratorsManager.getItems(id).forEach {
            tooltip.generatorChancesTooltip(it.value.weight, it.key)
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
        if (state.block == newState.block) return

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is GeneratorBlockEntity) {
            ItemScatterer.spawn(world, pos, blockEntity)
            world.updateComparators(pos, this)
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun neighborUpdate(state: BlockState, world: World, pos: BlockPos, sourceBlock: Block, sourcePos: BlockPos, notify: Boolean) {
        if (!world.isClient && state.get(ENABLED) != !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(ENABLED), NOTIFY_LISTENERS)
        }
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        (world.getBlockEntity(pos) as GeneratorBlockEntity).listUpgrades.forEach { type ->
            world.spawnEntity(
                ItemEntity(
                    world, player.x, player.y, player.z, when (type) {
                        UpgradeItem.UpgradesTypes.COEFFICIENT -> ItemStack(ItemsRegistry.COEFFICIENT_UPGRADE)
                        UpgradeItem.UpgradesTypes.COUNT -> ItemStack(ItemsRegistry.COUNT_UPGRADE)
                        UpgradeItem.UpgradesTypes.SPEED -> ItemStack(ItemsRegistry.SPEED_UPGRADE)
                        UpgradeItem.UpgradesTypes.ENERGY_FREE -> ItemStack(ItemsRegistry.ENERGY_FREE_UPGRADE)
                    }
                )
            )
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
                    Text.literal("[").append(Text.translatable("block.yacg.${id.namespace}_${id.path}")).append("]")
                        .formatted(Formatting.AQUA)
                )
                blockEntity.items.forEach {
                    if (it.item == Items.AIR) return@forEach
                    message.append("\n - ${it.item.name.string} x${it.count}")
                }
                player.sendMessage(message, false)
            }
        }

        return ActionResult.SUCCESS
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        GeneratorBlockEntity(pos, state, id, GeneratorsManager.getEnergyUsage(id))

    override fun <T : BlockEntity> getTicker(
        world: World, state: BlockState, type: BlockEntityType<T>
    ): BlockEntityTicker<T>? = validateTicker(type, BlocksEntityRegistry.GENERATOR_ENTITY, GeneratorBlockEntity::tick)

    override fun getDroppedStacks(
        state: BlockState,
        builder: LootContextParameterSet.Builder
    ): MutableList<ItemStack> = mutableListOf(this.asItem().defaultStack)

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        if (state.get(ENABLED) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(ENABLED), NOTIFY_LISTENERS)
        }
    }
}