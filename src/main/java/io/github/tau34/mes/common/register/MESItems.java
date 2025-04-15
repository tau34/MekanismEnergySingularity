package io.github.tau34.mes.common.register;

import mekanism.api.text.EnumColor;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;

public class MESItems {
    public static final ItemDeferredRegister REGISTER = new ItemDeferredRegister("mes");

    public static final ItemRegistryObject<Item> NEUTRONIUM_PELLET = REGISTER.register("neutronium_pellet", EnumColor.WHITE);
}
