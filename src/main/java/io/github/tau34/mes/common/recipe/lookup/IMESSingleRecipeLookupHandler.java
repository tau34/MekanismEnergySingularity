package io.github.tau34.mes.common.recipe.lookup;

import io.github.tau34.mes.common.recipe.cache.MESInputRecipeCache;
import io.github.tau34.mes.common.recipe.cache.MESSingleInputRecipeCache;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.util.ChemicalUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface IMESSingleRecipeLookupHandler<I, R extends MekanismRecipe & Predicate<I>, IC extends MESSingleInputRecipeCache<I, ?, R, ?>> extends IMESRecipeLookupHandler.IMESRecipeTypedLookupHandler<R, IC> {default boolean containsRecipe(I input) {
    return this.getRecipeType().getInputCache().containsInput(this.getHandlerWorld(), input);
}

    default @Nullable R findFirstRecipe(I input) {
        return this.getRecipeType().getInputCache().findFirstRecipe(this.getHandlerWorld(), input);
    }

    default @Nullable R findFirstRecipe(IInputHandler<I> inputHandler) {
        return this.findFirstRecipe(inputHandler.getInput());
    }

    public interface ChemicalRecipeLookupHandler<C extends Chemical<C>, S extends ChemicalStack<C>, R extends MekanismRecipe & Predicate<S>> extends IMESSingleRecipeLookupHandler<S, R, MESInputRecipeCache.SingleChemical<C, S, R>> {
        default boolean containsRecipe(C input) {
            return this.containsRecipe(ChemicalUtil.withAmount(input, 1L));
        }
    }

    public interface FluidRecipeLookupHandler<R extends MekanismRecipe & Predicate<FluidStack>> extends ISingleRecipeLookupHandler<FluidStack, R, InputRecipeCache.SingleFluid<R>> {
    }

    public interface ItemRecipeLookupHandler<R extends MekanismRecipe & Predicate<ItemStack>> extends ISingleRecipeLookupHandler<ItemStack, R, InputRecipeCache.SingleItem<R>> {
    }
}
