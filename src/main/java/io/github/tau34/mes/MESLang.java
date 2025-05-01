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
    AIR_EXTRACTOR_MODE("zpm", "configurator.air_extractor"),
    UNIVERSE_CONFIG("container", "universe_config"),
    UNIVERSE_DENSITY("universe", "density"),
    UNIVERSE_DENSITY_SPARSE("universe", "density.sparse"),
    UNIVERSE_DENSITY_NORMAL("universe", "density.normal"),
    UNIVERSE_DENSITY_RICH("universe", "density.rich"),
    UNIVERSE_DENSITY_ULTRA_RICH("universe", "density.ultra_rich"),
    UNIVERSE_RARE("universe", "rare"),
    UNIVERSE_RARE_SPARSE("universe", "rare.sparse"),
    UNIVERSE_RARE_NORMAL("universe", "rare.normal"),
    UNIVERSE_RARE_RICH("universe", "rare.rich"),
    UNIVERSE_RARE_ABUNDANT("universe", "rare.abundant"),
    UNIVERSE_RADIUS("universe", "radius"),
    UNIVERSE_RADIUS_COMPACT("universe", "radius.compact"),
    UNIVERSE_RADIUS_MEDIUM("universe", "radius.medium"),
    UNIVERSE_RADIUS_WIDE("universe", "radius.wide"),
    UNIVERSE_RADIUS_VAST("universe", "radius.vast"),
    UNIVERSE_GRAVITY("universe", "gravity"),
    UNIVERSE_GRAVITY_LOW("universe", "gravity.low"),
    UNIVERSE_GRAVITY_NORMAL("universe", "gravity.normal"),
    UNIVERSE_GRAVITY_HIGH("universe", "gravity.high"),
    UNIVERSE_GRAVITY_EXTREME("universe", "gravity.extreme"),
    UNIVERSE_CRUST("universe", "crust"),
    UNIVERSE_CRUST_STANDARD("universe", "crust.standard"),
    UNIVERSE_CRUST_MAGMATIC("universe", "crust.magmatic"),
    UNIVERSE_CRUST_RADIOGENIC("universe", "crust.radiogenic"),
    UNIVERSE_CRUST_CRYSTALLINE("universe", "crust.crystalline"),
    UNIVERSE_GENERATOR_OUTPUT("container", "universe_generator.output"),
    BUTTON_OUTPUT("button", "output"),
    BUTTON_GENERATE("button", "generate"),
    BUTTON_MANAGE("button", "manage"),
    BUTTON_MINE("button", "mine"),
    BUTTON_DISCONNECT("button", "disconnect"),
    AVAILABLE("gui", "available"),
    UNIVERSE_GENERATOR_MANAGER("container", "universe_generator.manager"),
    DESCRIPTION_ZPM_FRAME("description", "zpm_frame"),
    DESCRIPTION_ZPM_PORT("description", "zpm_port"),
    DESCRIPTION_VOID_RESONANCE_ENGINE("description", "void_resonance_engine"),
    DESCRIPTION_AIR_EXTRACTOR("description", "air_extractor"),
    DESCRIPTION_ZPM_LOGIC_ADAPTER("description", "zpm_logic_adapter"),
    DESCRIPTION_NEUTRON_CONDENSER("description", "neutron_condenser"),
    DESCRIPTION_UNIVERSE_GENERATOR("description", "universe_generator"),
    ADVANCED_FUSION_MODE_INPUT_LEFT("advanced_fusion", "port.input_left"),
    ADVANCED_FUSION_MODE_INPUT_RIGHT("advanced_fusion", "port.input_right"),
    ADVANCED_FUSION_MODE_OUTPUT("advanced_fusion", "port.output");

    private final String key;

    MESLang(String type, String path) {
        this.key = Util.makeDescriptionId(type, MESMod.rl(path));
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
