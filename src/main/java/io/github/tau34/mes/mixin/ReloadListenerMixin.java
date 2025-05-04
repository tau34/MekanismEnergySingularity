package io.github.tau34.mes.mixin;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.common.recipe.MESRecipeType;
import mekanism.common.ReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ReloadListener.class, remap = false)
public class ReloadListenerMixin {
    @Inject(method = "onResourceManagerReload", at = @At("TAIL"))
    public void onResourceManagerReload(ResourceManager resourceManager, CallbackInfo ci) {
        MESRecipeType.clearCache();
    }
}