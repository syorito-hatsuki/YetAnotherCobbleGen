package dev.syoritohatsuki.yacg.common.block.entity

import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.logger
import dev.syoritohatsuki.yacg.common.block.GeneratorBlock
import dev.syoritohatsuki.yacg.common.item.UpgradeItem.UpgradesTypes
import dev.syoritohatsuki.yacg.config.Generator
import dev.syoritohatsuki.yacg.config.GeneratorsManager
import dev.syoritohatsuki.yacg.config.ItemId
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
import team.reborn.energy.api.base.SimpleEnergyStorage
import kotlin.random.Random

class GeneratorBlockEntity(
    blockPos: BlockPos, blockState: BlockState, private var id: Identifier = Identifier.of(""), var energyUsage: Int = 0
) : BlockEntity(BlocksEntityRegistry.GENERATOR_ENTITY, blockPos, blockState), ImplementedInventory {

    private var progress: Byte = 0

    val listUpgrades: MutableSet<UpgradesTypes> = mutableSetOf()

    private var countMultiply: Byte = 1
    private var coefficientMultiply: Byte = 1
    private var speedDivider: Byte = 1
    private var energyRequired = true

    private val inventory = DefaultedList.ofSize(255, ItemStack.EMPTY)

    val energyStorage: SimpleEnergyStorage = object : SimpleEnergyStorage(10000, 1000, 0) {
        override fun onFinalCommit() {
            markDirty()
        }
    }

    companion object {
        private const val MAX_PROGRESS: Byte = 20

        fun tick(world: World, blockPos: BlockPos, blockState: BlockState, entity: GeneratorBlockEntity) {

            if (world.isClient) return

            // Disabled by redstone
            if (!blockState.get(GeneratorBlock.ENABLED)) return

            // Reset progress if going out of range
            if (entity.progress !in 0..20) entity.progress = 0

            // Energy required and amount less that required
            if (entity.energyRequired && entity.energyStorage.amount < entity.energyUsage) return

            // Progress is equals required
            if (entity.progress == (MAX_PROGRESS / entity.speedDivider).toByte()) {

                // Get random block from setOf blocks from config
                val randomBlock =
                    getRandomItem(entity.id, GeneratorsManager.getItems(entity.id), entity.coefficientMultiply)
                        ?: return

                // Check if item already exists in any slot (Max cap per slot 1024)
                entity.inventory.firstOrNull { it.isOf(randomBlock.item) }?.let { existingItem ->
                    entity.setStack(
                        entity.inventory.indexOf(existingItem),
                        randomBlock.copyWithCount(existingItem.count + (randomBlock.count * entity.countMultiply))
                    )
                } ?: getEmptySlot(entity.inventory)?.let { emptySlot ->
                    entity.setStack(emptySlot, randomBlock.copyWithCount(entity.countMultiply.toInt()))
                } ?: return

                // Use energy only if required
                if (entity.energyRequired) entity.energyStorage.amount -= entity.energyUsage

                entity.progress = 0
            }

            // Add progress only if energy not required or amount of energy require for generating
            if (!entity.energyRequired || entity.energyStorage.amount >= entity.energyUsage) {
                entity.progress++
            }

            markDirty(world, blockPos, blockState)
        }

        private fun getRandomItem(
            type: Identifier,
            items: Map<ItemId, Generator.ItemSettings>,
            coefficient: Byte,
        ): ItemStack? {
            if (items.isEmpty()) {
                logger.error("Blocks list for $type is empty")
                return null
            }

            if (coefficient > 1) items.map {
                if (it.value.weight < 50) it.value.weight *= coefficient
            }

            var randomNumber = Random.nextInt(items.values.sumOf { it.weight })

            items.forEach { item ->
                if (randomNumber < item.value.weight) return ItemStack(
                    Registries.ITEM.get(Identifier.of(item.key)), item.value.count
                )
                randomNumber -= item.value.weight
            }

            logger.error("Blocks list for $type is empty")
            return null
        }

        private fun getEmptySlot(items: DefaultedList<ItemStack>): Int? =
            items.indexOfFirst { it.isEmpty }.takeIf { it != -1 } ?: run {
                logger.warn("Can't find any free slot from 255 slot's")
                return null
            }
    }

    override var items: DefaultedList<ItemStack> = inventory

    override fun markDirty() = super<ImplementedInventory>.markDirty()

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        Inventories.writeNbt(nbt, inventory, registryLookup)
        nbt.putLong("yacg.energy", energyStorage.amount)
        nbt.putByte("yacg.progress", progress)
        nbt.putString("yacg.id", id.toString())
        listUpgrades.forEach {
            nbt.putString("yacg.upgrade.${it.name.lowercase()}", it.name)
        }
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup)
        energyStorage.amount = nbt.getLong("yacg.energy")
        progress = nbt.getByte("yacg.progress")
        id = Identifier.of(nbt.getString("yacg.id"))
        energyUsage = GeneratorsManager.getEnergyUsage(id)
        UpgradesTypes.entries.forEach {
            nbt.getString("yacg.upgrade.${it.name.lowercase()}").apply {
                if (isNullOrBlank()) return@forEach
                insertUpgrade(UpgradesTypes.valueOf(this))
            }
        }
        super.readNbt(nbt, registryLookup)
    }

    fun insertUpgrade(upgradeType: UpgradesTypes): ActionResult {
        if (listUpgrades.contains(upgradeType)) return ActionResult.FAIL

        listUpgrades.add(upgradeType)

        when (upgradeType) {
            UpgradesTypes.SPEED -> speedDivider = UpgradesConfig.getUpgradeModify(upgradeType) ?: 2
            UpgradesTypes.COEFFICIENT -> coefficientMultiply = UpgradesConfig.getUpgradeModify(upgradeType) ?: 2
            UpgradesTypes.COUNT -> countMultiply = UpgradesConfig.getUpgradeModify(upgradeType) ?: 2
            UpgradesTypes.ENERGY_FREE -> energyRequired = false
        }

        return ActionResult.SUCCESS
    }

}
