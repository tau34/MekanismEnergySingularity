package io.github.tau34.mes.common.tile;

import io.github.tau34.mes.common.multiblock.data.ZPMMultiblockData;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.math.MathUtils;
import mekanism.api.text.EnumColor;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.util.NBTUtils;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.base.IReactorLogic;
import mekanism.generators.common.base.IReactorLogicMode;
import mekanism.generators.common.content.fission.FissionReactorMultiblockData;
import mekanism.generators.common.tile.fission.TileEntityFissionReactorLogicAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityZPMLogicAdapter extends TileEntityZPMBlock implements IReactorLogic<TileEntityZPMLogicAdapter.ZPMLogic> {
    public ZPMLogic logic;
    private RedstoneStatus prevStatus;

    public TileEntityZPMLogicAdapter(BlockPos pos, BlockState state) {
        super(MESBlocks.ZPM_LOGIC_ADAPTER, pos, state);
        this.logic = ZPMLogic.DISABLED;
        this.prevStatus = RedstoneStatus.IDLE;
    }

    @Override
    protected boolean onUpdateServer(ZPMMultiblockData multiblock) {
        boolean np = super.onUpdateServer(multiblock);
        RedstoneStatus status = this.getStatus();
        return np;
    }

    @Override
    public ZPMLogic getMode() {
        return logic;
    }

    @Override
    public ZPMLogic[] getModes() {
        return ZPMLogic.MODES;
    }

    public int getRedstoneLevel(Direction side) {
        return !this.isRemote() && this.getMultiblock().isPositionOutsideBounds(this.worldPosition.relative(side)) && this.getStatus() == RedstoneStatus.OUTPUTTING ? 15 : 0;
    }

    @ComputerMethod(nameOverride = "getRedstoneLogicStatus")
    public RedstoneStatus getStatus() {
        if (this.isRemote()) {
            return prevStatus;
        } else {
            ZPMMultiblockData multiblock = this.getMultiblock();
            if (multiblock.isFormed()) {
                switch (this.logic) {
                    case ACTIVATION:
                        if (isPowered()) {
                            return RedstoneStatus.POWERED;
                        }
                        break;
                    case DEPLETED:
                        if (multiblock.stabilizerTank.getStored() < 2000) {
                            return RedstoneStatus.OUTPUTTING;
                        }
                }
            }
        }
        return RedstoneStatus.IDLE;
    }

    @ComputerMethod(nameOverride = "setLogicMode")
    public void setLogicTypeFromPacket(ZPMLogic logicType) {
        if (this.logic != logicType) {
            this.logic = logicType;
            this.markForSave();
        }
    }

    @Override
    public void onPowerChange() {
        super.onPowerChange();
        if (!this.isRemote()) {
            ZPMMultiblockData multiblock = this.getMultiblock();
            if (multiblock.isFormed() && this.logic == ZPMLogic.ACTIVATION) {
                multiblock.setActive(this.isPowered());
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        NBTUtils.setEnumIfPresent(nbt, "logicType", ZPMLogic::byIndexStatic, (logicType) -> this.logic = logicType);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        NBTUtils.writeEnum(nbtTags, "logicType", this.logic);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableEnum.create(ZPMLogic::byIndexStatic, ZPMLogic.DISABLED, this::getMode, (value) -> this.logic = value));
        container.track(SyncableEnum.create(RedstoneStatus::byIndexStatic, RedstoneStatus.IDLE, () -> this.prevStatus, (value) -> this.prevStatus = value));
    }

    @Override
    public boolean canBeMaster() {
        return false;
    }

    @NothingNullByDefault
    public static enum ZPMLogic implements IReactorLogicMode<ZPMLogic>, IHasTranslationKey {
        DISABLED(GeneratorsLang.REACTOR_LOGIC_DISABLED, GeneratorsLang.DESCRIPTION_REACTOR_DISABLED, new ItemStack(Items.GUNPOWDER), EnumColor.DARK_GRAY),
        ACTIVATION(GeneratorsLang.REACTOR_LOGIC_ACTIVATION, GeneratorsLang.DESCRIPTION_REACTOR_ACTIVATION, new ItemStack(Items.FLINT_AND_STEEL), EnumColor.AQUA),
        DEPLETED(GeneratorsLang.REACTOR_LOGIC_DEPLETED, GeneratorsLang.DESCRIPTION_REACTOR_DEPLETED, new ItemStack(Items.REDSTONE), EnumColor.RED);

        private static final ZPMLogic[] MODES = values();
        private final ILangEntry name;
        private final ILangEntry description;
        private final ItemStack renderStack;
        private final EnumColor color;

        ZPMLogic(ILangEntry name, ILangEntry description, ItemStack stack, EnumColor color) {
            this.name = name;
            this.description = description;
            this.renderStack = stack;
            this.color = color;
        }

        public ItemStack getRenderStack() {
            return this.renderStack;
        }

        public String getTranslationKey() {
            return this.name.getTranslationKey();
        }

        public Component getDescription() {
            return this.description.translate(new Object[0]);
        }

        public EnumColor getColor() {
            return this.color;
        }

        public static ZPMLogic byIndexStatic(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }
    }

    @NothingNullByDefault
    public static enum RedstoneStatus implements IHasTranslationKey {
        IDLE(MekanismLang.IDLE),
        OUTPUTTING(GeneratorsLang.REACTOR_LOGIC_OUTPUTTING),
        POWERED(GeneratorsLang.REACTOR_LOGIC_POWERED);

        private static final RedstoneStatus[] MODES = values();
        private final ILangEntry name;

        private RedstoneStatus(ILangEntry name) {
            this.name = name;
        }

        public String getTranslationKey() {
            return this.name.getTranslationKey();
        }

        public static RedstoneStatus byIndexStatic(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }
    }
}
