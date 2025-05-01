package io.github.tau34.mes.common.recipe.impl;

import io.github.tau34.mes.common.recipe.MESRecipeType;
import io.github.tau34.mes.common.register.MESBlocks;
import io.github.tau34.mes.common.register.MESRecipeSerializers;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class AdvancedFusionIRecipe extends ChemicalInfuserRecipe {
    public AdvancedFusionIRecipe(ResourceLocation id, ChemicalStackIngredient.GasStackIngredient leftInput, ChemicalStackIngredient.GasStackIngredient rightInput, GasStack output) {
        super(id, leftInput, rightInput, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MESRecipeSerializers.ADVANCED_FUSION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MESRecipeType.ADVANCED_FUSION.getRecipeType();
    }

    public String getGroup() {
        return MESBlocks.ADVANCED_FUSION_FRAME.getName();
    }

    public ItemStack getToastSymbol() {
        return MESBlocks.ADVANCED_FUSION_FRAME.getItemStack();
    }
}
