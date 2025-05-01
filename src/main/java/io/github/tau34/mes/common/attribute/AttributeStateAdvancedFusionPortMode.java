package io.github.tau34.mes.common.attribute;

import io.github.tau34.mes.MESLang;
import mekanism.api.IIncrementalEnum;
import mekanism.api.math.MathUtils;
import mekanism.api.text.EnumColor;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AttributeStateAdvancedFusionPortMode implements AttributeState {
    public static EnumProperty<AdvancedFusionPortMode> modeProperty = EnumProperty.create("mode", AdvancedFusionPortMode.class);

    @Override
    public BlockState copyStateData(BlockState oldState, BlockState newState) {
        if (Attribute.has(newState, AttributeStateAdvancedFusionPortMode.class)) {
            newState = newState.setValue(modeProperty, oldState.getValue(modeProperty));
        }

        return newState;
    }

    @Override
    public void fillBlockStateContainer(Block block, List<Property<?>> properties) {
        properties.add(modeProperty);
    }

    @Override
    public BlockState getDefaultState(@NotNull BlockState state) {
        return state.setValue(modeProperty, AdvancedFusionPortMode.INPUT_LEFT);
    }

    public static enum AdvancedFusionPortMode implements IIncrementalEnum<AdvancedFusionPortMode>, StringRepresentable, IHasTextComponent {
        INPUT_LEFT(MESLang.ADVANCED_FUSION_MODE_INPUT_LEFT, EnumColor.RED),
        INPUT_RIGHT(MESLang.ADVANCED_FUSION_MODE_INPUT_RIGHT, EnumColor.AQUA),
        OUTPUT(MESLang.ADVANCED_FUSION_MODE_OUTPUT, EnumColor.DARK_BLUE);

        final ILangEntry lang;
        final EnumColor color;

        AdvancedFusionPortMode(ILangEntry mesLang, EnumColor color) {
            this.lang = mesLang;
            this.color = color;
        }

        @Override
        public AdvancedFusionPortMode byIndex(int i) {
            return MathUtils.getByIndexMod(values(), i);
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }

        @Override
        public Component getTextComponent() {
            return this.lang.translateColored(color, lang);
        }
    }
}
