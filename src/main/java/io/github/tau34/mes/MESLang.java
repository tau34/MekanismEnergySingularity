package io.github.tau34.mes;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum MESLang implements ILangEntry {
    MES("constants", "mod_name"),
    ZPM("zpm", "zpm"),
    ZPM_LOGIC_DEPLETED("zpm", "logic.depleted"),
    DESCRIPTION_ZPM_DEPLETED("description", "zpm.logic.depleted"),
    DESCRIPTION_ZPM_READY("description", "zpm.logic.ready"),
    ZPM_PORT_EJECT("zpm", "configurator.port_eject"),
    AIR_EXTRACTOR_MODE("zpm", "configurator.air_extractor");

    private final String key;

    MESLang(String type, String path) {
        this.key = Util.makeDescriptionId(type, MESMod.rl(path));
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
