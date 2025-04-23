package io.github.tau34.mes.mixin;

import io.github.tau34.mes.common.network.MESPacketGuiButtonPress;
import io.github.tau34.mes.common.network.MESPacketGuiInteract;
import mekanism.common.network.BasePacketHandler;
import mekanism.common.network.PacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class PacketHandlerMixin extends BasePacketHandler {
    @Inject(method = "initialize", at = @At("HEAD"))
    private void modifyInitialize(CallbackInfo ci) {
        this.registerClientToServer(MESPacketGuiInteract.class, MESPacketGuiInteract::decode);
        this.registerClientToServer(MESPacketGuiButtonPress.class, MESPacketGuiButtonPress::decode);
    }
}
