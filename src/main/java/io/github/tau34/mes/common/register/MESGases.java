package io.github.tau34.mes.common.register;

import mekanism.api.chemical.gas.Gas;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;

public class MESGases {
    public static final GasDeferredRegister REGISTER = new GasDeferredRegister("mes");

    public static final GasRegistryObject<Gas> QUANTUM_STABILIZER = REGISTER.register("quantum_stabilizer", 0x9B5DE5);
}
