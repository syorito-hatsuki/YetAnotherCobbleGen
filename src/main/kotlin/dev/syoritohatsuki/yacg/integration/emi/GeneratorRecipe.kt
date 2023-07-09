package dev.syoritohatsuki.yacg.integration.emi

import dev.emi.emi.api.recipe.EmiIngredientRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiResolutionRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import dev.syoritohatsuki.yacg.config.GeneratorsConfig.Generators.GenerateItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class GeneratorRecipe(private val type: String, private val items: Set<GenerateItem>) : EmiIngredientRecipe() {
    override fun getCategory(): EmiRecipeCategory = YacgEmiPlugin.GENERATORS_CATEGORY

    override fun getId(): Identifier = Identifier("emi", "$MOD_ID/$type/${category.id.path}")

    override fun getIngredient(): EmiIngredient = EmiStack.of(Registry.ITEM.get(Identifier(MOD_ID, type)))

    override fun getInputs(): List<EmiIngredient> = emptyList()

    override fun getOutputs(): List<EmiStack> = items.map {
        EmiStack.of(ItemStack(Registry.ITEM.get(Identifier(it.itemId)), it.count))
    }

    override fun getStacks(): List<EmiStack> = outputs

    override fun getRecipeContext(stack: EmiStack, offset: Int): EmiRecipe = EmiResolutionRecipe(ingredient, stack)

}