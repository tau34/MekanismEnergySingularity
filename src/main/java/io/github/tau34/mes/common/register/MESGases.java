package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.attribute.Plasma;
import mekanism.api.chemical.gas.Gas;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;

public class MESGases {
    public static final GasDeferredRegister REGISTER = new GasDeferredRegister("mes");

    public static final GasRegistryObject<Gas> QUANTUM_STABILIZER = REGISTER.register("quantum_stabilizer", 0x9B5DE5);
    public static final GasRegistryObject<Gas> NEUTRON_SOURCE = REGISTER.register("neutron_source", 0xEFE958);
    public static final GasRegistryObject<Gas> NEUTRONIUM = REGISTER.register("neutronium", 0xCDD3D8);
    public static final GasRegistryObject<Gas> HELIUM_PLASMA = REGISTER.register("helium_plasma", -2035457, new Plasma(() -> 750_000_000));
}
