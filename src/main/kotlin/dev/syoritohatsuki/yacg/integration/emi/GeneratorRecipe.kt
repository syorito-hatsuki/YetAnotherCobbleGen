package dev.syoritohatsuki.yacg.integration.emi

import dev.emi.emi.api.recipe.EmiIngredientRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiResolutionRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.syoritohatsuki.yacg.YetAnotherCobblestoneGenerator.MOD_ID
import dev.syoritohatsuki.yacg.config.Generator
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

class GeneratorRecipe(private val id: Identifier, private val items: Map<String, Generator.ItemSettings>) :
    EmiIngredientRecipe() {
    override fun getCategory(): EmiRecipeCategory = YacgEmiPlugin.GENERATORS_CATEGORY

    override fun getId(): Identifier = Identifier.of("emi", "$MOD_ID/${id.namespace}/${id.path}_${category.id.path}")

    override fun getIngredient(): EmiIngredient = EmiStack.of(Registries.ITEM.get(Identifier.of(MOD_ID, "${id.namespace}_${id.path}")))

    override fun getInputs(): List<EmiIngredient> = emptyList()

    override fun getOutputs(): List<EmiStack> = items.map {
        EmiStack.of(ItemStack(Registries.ITEM.get(Identifier.of(it.key)), it.value.count))
    }

    override fun getStacks(): List<EmiStack> = outputs

    override fun getRecipeContext(stack: EmiStack, offset: Int): EmiRecipe = EmiResolutionRecipe(ingredient, stack)

}