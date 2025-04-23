package io.github.tau34.mes.common.blocktype;

import mekanism.api.Upgrade;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.*;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import java.util.EnumSet;
import java.util.function.Supplier;

public class MESMachine<TILE extends TileEntityMekanism> extends BlockTypeTile<TILE> {
    public MESMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
        super(tileEntityRegistrar, description);
        this.add((new AttributeParticleFX()).add(ParticleTypes.SMOKE, (rand) -> new Pos3D((double)(rand.nextFloat() * 0.6F - 0.3F), rand.nextFloat() * 6.0F / 16.0F, 0.52)).add(DustParticleOptions.REDSTONE, (rand) -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, (double)(rand.nextFloat() * 6.0F / 16.0F), 0.52)));
        this.add(Attributes.ACTIVE_LIGHT, new AttributeStateFacing(), Attributes.SECURITY, Attributes.INVENTORY, Attributes.REDSTONE, Attributes.COMPARATOR);
        this.add(new AttributeUpgradeSupport(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING)));
    }

    public static class MESMachineBuilder<MACHINE extends MESMachine<TILE>, TILE extends TileEntityMekanism, T extends MESMachineBuilder<MACHINE, TILE, T>> extends BlockTypeTile.BlockTileBuilder<MACHINE, TILE, T> {
        protected MESMachineBuilder(MACHINE holder) {
            super(holder);
        }

        public static <TILE extends TileEntityMekanism> MESMachineBuilder<MESMachine<TILE>, TILE, ?> createMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
            return new MESMachineBuilder<>(new MESMachine<>(tileEntityRegistrar, description));
        }
    }
}