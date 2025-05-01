package io.github.tau34.mes.common.recipe.impl;

import io.github.tau34.mes.common.recipe.MESRecipeType;
import io.github.tau34.mes.common.register.MESBlocks;
import io.github.tau34.mes.common.register.MESRecipeSerializers;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class PlasmaCoolerIRecipe extends GasToGasRecipe {
    public PlasmaCoolerIRecipe(ResourceLocation id, ChemicalStackIngredient.GasStackIngredient input, GasStack output) {
        super(id, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MESRecipeSerializers.PLASMA_COOLING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MESRecipeType.PLASMA_COOLING.get();
    }

    @Override
    public String getGroup() {
        return MESBlocks.PLASMA_COOLER.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return MESBlocks.PLASMA_COOLER.getItemStack();
    }
}
