package io.github.tau34.mes.mixin;

import io.github.tau34.mes.common.network.MESPacketGuiInteract;
import mekanism.common.network.BasePacketHandler;
import mekanism.generators.common.network.GeneratorsPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GeneratorsPacketHandler.class, remap = false)
public abstract class PacketHandlerMixin extends BasePacketHandler {
    @Inject(method = "initialize", at = @At("HEAD"))
    private void modifyInitialize(CallbackInfo ci) {
        this.registerClientToServer(MESPacketGuiInteract.class, MESPacketGuiInteract::decode);
    }
}
