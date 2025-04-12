package io.github.tau34.mes;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum MESLang implements ILangEntry {
    MES("constants", "mod_name"),
    ZPM("zpm", "zpm");

    private final String key;

    MESLang(String type, String path) {
        this.key = Util.makeDescriptionId(type, MESMod.rl(path));
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
