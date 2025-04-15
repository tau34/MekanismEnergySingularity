package io.github.tau34.mes.client;

import io.github.tau34.mes.MESMod;
import io.github.tau34.mes.client.jei.MESRecipeRegistryHelper;
import io.github.tau34.mes.common.recipe.MESRecipeType;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.machine.GasToGasRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class MESJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return MESMod.rl("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        MekanismJEI.registerItemSubtypes(registration, MESBlocks.REGISTER.getAllBlocks());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new GasToGasRecipeCategory(guiHelper, MESJEIRecipeType.NEUTRON_CONDENSING, MESBlocks.NEUTRON_CONDENSER));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        CatalystRegistryHelper.register(registration, MESBlocks.NEUTRON_CONDENSER, MESJEIRecipeType.NEUTRON_CONDENSING);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        MESRecipeRegistryHelper.register(registration, MESJEIRecipeType.NEUTRON_CONDENSING, MESRecipeType.NEUTRON_CONDENSING);
    }
}
