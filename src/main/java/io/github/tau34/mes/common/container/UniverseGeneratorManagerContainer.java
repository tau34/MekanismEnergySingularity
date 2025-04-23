package io.github.tau34.mes.common.container;

import io.github.tau34.mes.common.register.MESContainerTypes;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class UniverseGeneratorManagerContainer extends MekanismTileContainer<TileEntityUniverseGenerator> {
    public UniverseGeneratorManagerContainer(int id, Inventory inv, @NotNull TileEntityUniverseGenerator tileEntityUniverseGenerator) {
        super(MESContainerTypes.UNIVERSE_GENERATOR_MANAGER, id, inv, tileEntityUniverseGenerator);
    }

    @Override
    protected void addSlots() {
    }
}
