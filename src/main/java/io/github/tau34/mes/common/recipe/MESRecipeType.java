package io.github.tau34.mes.common.recipe;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.MESMod;
import io.github.tau34.mes.common.recipe.cache.MESInputRecipeCache;
import io.github.tau34.mes.common.registration.MESRecipeTypeDeferredRegister;
import io.github.tau34.mes.common.registration.impl.MESRecipeTypeRegistryObject;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.chemical.ChemicalToChemicalRecipe;
import mekanism.client.MekanismClient;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MESRecipeType<R extends MekanismRecipe, IC extends IInputRecipeCache> implements RecipeType<R>, IMESRecipeTypeProvider<R, IC> {
    public static final MESRecipeTypeDeferredRegister REGISTER = new MESRecipeTypeDeferredRegister("mes");
    public static final MESRecipeTypeRegistryObject<GasToGasRecipe, MESInputRecipeCache.SingleChemical<Gas, GasStack, GasToGasRecipe>> NEUTRON_CONDENSING = register("neutron_condensing", rt -> new MESInputRecipeCache.SingleChemical<>(rt, ChemicalToChemicalRecipe::getInput));
    public static final MESRecipeTypeRegistryObject<ChemicalInfuserRecipe, MESInputRecipeCache.EitherSideChemical<Gas, GasStack, ChemicalInfuserRecipe>> ADVANCED_FUSION = register("advanced_fusion", MESInputRecipeCache.EitherSideChemical::new);
    public static final MESRecipeTypeRegistryObject<GasToGasRecipe, MESInputRecipeCache.SingleChemical<Gas, GasStack, GasToGasRecipe>> PLASMA_COOLING = register("plasma_cooling", rt -> new MESInputRecipeCache.SingleChemical<>(rt, ChemicalToChemicalRecipe::getInput));

    private List<R> cachedRecipes = Collections.emptyList();
    private final ResourceLocation registryName;
    private final IC inputCache;

    private MESRecipeType(String name, Function<MESRecipeType<R, IC>, IC> inputCacheCreator) {
        registryName = MESMod.rl(name);
        inputCache = inputCacheCreator.apply(this);
    }

    public static void clearCache() {
        for (IMESRecipeTypeProvider<?, ?> recipeTypeProvider : REGISTER.getAllRecipeTypes()) {
            recipeTypeProvider.getRecipeType().clearCaches();
        }
    }

    private static <R extends MekanismRecipe, IC extends IInputRecipeCache> MESRecipeTypeRegistryObject<R, IC> register(String name, Function<MESRecipeType<R, IC>, IC> inputCacheCreator) {
        return REGISTER.register(name, () -> new MESRecipeType<>(name, inputCacheCreator));
    }

    @Override
    public String toString() {
        return registryName.toString();
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public MESRecipeType<R, IC> getRecipeType() {
        return this;
    }

    private void clearCaches() {
        this.cachedRecipes = Collections.emptyList();
        this.inputCache.clear();
    }

    @Override
    public IC getInputCache() {
        return inputCache;
    }

    @NotNull
    @Override
    public List<R> getRecipes(@Nullable Level world) {
        if (world == null) {
            if (FMLEnvironment.dist.isClient()) {
                world = MekanismClient.tryGetClientWorld();
            } else {
                world = ServerLifecycleHooks.getCurrentServer().overworld();
            }
            if (world == null) {
                return Collections.emptyList();
            }
        }
        if (cachedRecipes.isEmpty()) {
            RecipeManager recipeManager = world.getRecipeManager();
            List<R> recipes = recipeManager.getAllRecipesFor(this);
            cachedRecipes = recipes.stream().filter(recipe -> !recipe.isIncomplete()).toList();
        }
        LogUtils.getLogger().info("{}", cachedRecipes);
        return cachedRecipes;
    }

    public static <C extends Container, RT extends Recipe<C>> Optional<RT> getRecipeFor(RecipeType<RT> recipeType, C inventory, Level level) {
        return level.getRecipeManager().getRecipeFor(recipeType, inventory, level).filter(recipe -> recipe.isSpecial() || !recipe.isIncomplete());
    }

    public static Optional<? extends Recipe<?>> byKey(Level level, ResourceLocation id) {
        return level.getRecipeManager().byKey(id).filter((recipe) -> recipe.isSpecial() || !recipe.isIncomplete());
    }
}
