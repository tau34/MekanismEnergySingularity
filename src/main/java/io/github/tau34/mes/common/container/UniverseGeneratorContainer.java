package io.github.tau34.mes.common.container;

import io.github.tau34.mes.common.register.MESContainerTypes;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class UniverseGeneratorContainer extends MekanismTileContainer<TileEntityUniverseGenerator> {
    public UniverseGeneratorContainer(int id, Inventory inv, @NotNull TileEntityUniverseGenerator tileEntityUniverseGenerator) {
        super(MESContainerTypes.UNIVERSE_GENERATOR, id, inv, tileEntityUniverseGenerator);
    }

    @Override
    protected void addSlots() {
    }
}
