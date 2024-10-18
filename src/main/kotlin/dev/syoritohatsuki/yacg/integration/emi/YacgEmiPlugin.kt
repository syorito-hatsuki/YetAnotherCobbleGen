package dev.syoritohatsuki.yacg.integration.emi

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import dev.syoritohatsuki.yacg.util.BuildInGenerators
import dev.syoritohatsuki.yacg.config.GeneratorsManager
import dev.syoritohatsuki.yacg.registry.BlocksRegistry
import net.minecraft.util.Identifier

object YacgEmiPlugin : EmiPlugin {

    private val GENERATORS_ID = Identifier.of(MOD_ID, "generators")

    val GENERATORS_CATEGORY = EmiRecipeCategory(GENERATORS_ID, EmiStack.of(BlocksRegistry.BLOCKS.keys.random()))

    override fun register(registry: EmiRegistry) {
        registry.addCategory(GENERATORS_CATEGORY)

        BlocksRegistry.BLOCKS.keys.forEach {
            registry.addWorkstation(GENERATORS_CATEGORY, EmiStack.of(it))
        }

        GeneratorsManager.dedicatedGenerators.forEach { id ->
            registry.addRecipe(GeneratorRecipe(id, GeneratorsManager.getItems(id)))
        }

        BuildInGenerators.buildInGenerators.keys.forEach { generator ->
            val id = Identifier.of(MOD_ID, generator)
            registry.addRecipe(GeneratorRecipe(id, GeneratorsManager.getItems(id)))
        }
    }
}