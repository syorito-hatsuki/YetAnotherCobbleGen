package dev.syoritohatsuki.yacg.common.block.entity

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.logger
import dev.syoritohatsuki.yacg.common.block.GeneratorBlock
import dev.syoritohatsuki.yacg.common.item.UpgradeItem.UpgradesTypes
import dev.syoritohatsuki.yacg.config.GeneratorsConfig
import dev.syoritohatsuki.yacg.config.UpgradesConfig
import dev.syoritohatsuki.yacg.registry.BlocksEntityRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.random.Random

class GeneratorBlockEntity(
    blockPos: BlockPos, blockState: BlockState, private var type: String = ""
) : BlockEntity(BlocksEntityRegistry.GENERATOR_ENTITY, blockPos, blockState), ImplementedInventory {

    private var progress: Byte = 0
    private val maxProcess: Byte = 20

    val listUpgrades: MutableList<UpgradesTypes> = mutableListOf()

    private var countMultiply: Byte = 1
    private var coefficientMultiply: Byte = 1
    private var speedDivider: Byte = 1

    private val inventory = DefaultedList.ofSize(255, ItemStack.EMPTY)

    companion object {

        fun tick(world: World, blockPos: BlockPos, blockState: BlockState, entity: GeneratorBlockEntity) {

            if (world.isClient) return

            if (entity.progress < 0) entity.progress = 0

            if (entity.progress == (entity.maxProcess / entity.speedDivider).toByte()) {

                if (!blockState.get(GeneratorBlock.ENABLED)) return

                val randomBlock = getRandomBlock(
                    entity.type, GeneratorsConfig.getBlocks(entity.type) ?: return, entity.coefficientMultiply
                ) ?: return

                if (!entity.inventory.none { it.isOf(randomBlock.item) }) {
                    val slot = entity.inventory.indexOfFirst { it.isOf(randomBlock.item) }

                    entity.setStack(
                        slot,
                        randomBlock.copyWithCount(entity.inventory[slot].count + (randomBlock.count * entity.countMultiply))
                    )
                } else entity.setStack(
                    getEmptySlot(entity.inventory) ?: return, randomBlock.copyWithCount(entity.countMultiply.toInt())
                )
                entity.progress = 0
            }

            entity.progress++

            markDirty(world, blockPos, blockState)
        }

        private fun getRandomBlock(
            type: String,
            blocks: Set<GeneratorsConfig.Generators.GenerateItem>,
            coefficient: Byte,
        ): ItemStack? {
            if (blocks.isEmpty()) {
                logger.error("Blocks list for $type is empty")
                return null
            }

            blocks.map {
                if (it.coefficient < 50) it.coefficient *= coefficient
            }

            var randomNumber = Random.nextInt(blocks.sumOf { it.coefficient })
            blocks.forEach { block ->
                if (randomNumber < block.coefficient) return ItemStack(
                    Registries.ITEM.get(Identifier(block.itemId)), block.count
                )
                randomNumber -= block.coefficient
            }

            logger.error("Blocks list for $type is empty")
            return null
        }

        private fun getEmptySlot(items: DefaultedList<ItemStack>): Int? {
            items.indices.forEach {
                if (items[it].isEmpty) return it
            }
            logger.warn("Can't find any free slot from 255 slot's")
            return null
        }
    }

    override var items: DefaultedList<ItemStack> = inventory

    override fun markDirty() = super<ImplementedInventory>.markDirty()

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        Inventories.writeNbt(nbt, inventory, registryLookup)
        nbt.putByte("yacg.progress", progress)
        nbt.putString("yacg.type", type)
        listUpgrades.let { types ->
            types.forEach {
                nbt.putString("yacg.upgrade.${it.name.lowercase()}", it.name)
            }
        }
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup)
        UpgradesTypes.entries.toTypedArray().let { types ->
            types.forEach {
                nbt.getString("yacg.upgrade.${it.name.lowercase()}").apply {
                    if (isNullOrBlank()) return@forEach
                    insertUpgrade(UpgradesTypes.valueOf(this))
                }
            }
        }
        super.readNbt(nbt, registryLookup)
        type = nbt.getString("yacg.type")
        progress = nbt.getByte("yacg.progress")
    }

    fun insertUpgrade(upgradeType: UpgradesTypes): ActionResult {
        if (listUpgrades.contains(upgradeType)) return ActionResult.FAIL

        listUpgrades.add(upgradeType)

        when (upgradeType) {
            UpgradesTypes.SPEED -> speedDivider = UpgradesConfig.getUpgradeModify(upgradeType) ?: 2
            UpgradesTypes.COEFFICIENT -> coefficientMultiply = UpgradesConfig.getUpgradeModify(upgradeType) ?: 2
            UpgradesTypes.COUNT -> countMultiply = UpgradesConfig.getUpgradeModify(upgradeType) ?: 2
        }

        return ActionResult.SUCCESS
    }

}
