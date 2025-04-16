package io.github.tau34.mes.common.tile.zpm;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.MESLang;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.IConfigurable;
import mekanism.api.text.EnumColor;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.resolver.BasicCapabilityResolver;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.text.BooleanStateDisplay;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAirExtractor extends TileEntityMekanism implements IConfigurable {
    public TileEntityAirExtractor(BlockPos pos, BlockState state) {
        super(MESBlocks.AIR_EXTRACTOR, pos, state);
        this.addCapabilityResolver(BasicCapabilityResolver.constant(Capabilities.CONFIGURABLE, this));
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        if (!this.isRemote()) {
            boolean oldMode = this.getActive();
            this.setActive(!oldMode);
            LogUtils.getLogger().info(String.valueOf(oldMode));
            player.displayClientMessage(MESLang.AIR_EXTRACTOR_MODE.translateColored(EnumColor.GRAY, BooleanStateDisplay.ActiveDisabled.of(!oldMode, true)), true);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onRightClick(Player player) {
        return InteractionResult.PASS;
    }
}
