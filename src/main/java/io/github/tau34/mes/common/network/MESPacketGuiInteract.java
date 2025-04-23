package io.github.tau34.mes.common.network;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import io.github.tau34.mes.common.tile.zpm.TileEntityZPMBlock;
import io.github.tau34.mes.common.tile.zpm.TileEntityZPMLogicAdapter;
import mekanism.api.functions.TriConsumer;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class MESPacketGuiInteract implements IMekanismPacket {
    private final MESPacketGuiInteract.MESGuiInteraction interaction;
    private final BlockPos pos;
    private final double extra;

    public MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction interaction, BlockEntity tile) {
        this(interaction, tile.getBlockPos());
    }

    public MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction interaction, BlockEntity tile, double extra) {
        this(interaction, tile.getBlockPos(), extra);
    }

    public MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction interaction, BlockPos pos) {
        this(interaction, pos, 0.0D);
    }
    
    public MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction interaction, BlockPos pos, double extra) {
        this.interaction = interaction;
        this.pos = pos;
        this.extra = extra;
    }
    
    @Override
    public void handle(NetworkEvent.Context context) {
        Player player = context.getSender();
        if (player != null) {
            TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, player.level(), this.pos);
            if (tile != null) {
                this.interaction.consume(tile, player, this.extra);
            }
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.interaction);
        buffer.writeBlockPos(this.pos);
        buffer.writeDouble(this.extra);
    }

    public static MESPacketGuiInteract decode(FriendlyByteBuf buffer) {
        return new MESPacketGuiInteract(buffer.readEnum(MESGuiInteraction.class), buffer.readBlockPos(), buffer.readDouble());
    }

    public static enum MESGuiInteraction {
        ZPM_ACTIVATE(((tile, player, extra) -> {
            if (tile instanceof TileEntityZPMBlock zpm) {
                zpm.setZPMActive(Math.round(extra) == 1L);
            }
        })),
        LOGIC_TYPE((tile, player, extra) -> {
            if (tile instanceof TileEntityZPMLogicAdapter zpm) {
                zpm.setLogicTypeFromPacket(TileEntityZPMLogicAdapter.ZPMLogic.byIndexStatic((int) Math.round(extra)));
            }
        }),
        UNIVERSE_DENSITY((tile, player, extra) -> {
            if (tile instanceof TileEntityUniverseGenerator ug) {
                ug.setDensity((int) Math.round(extra));
            }
        }),
        UNIVERSE_RARE((tile, player, extra) -> {
            if (tile instanceof TileEntityUniverseGenerator ug) {
                ug.setRare((int) Math.round(extra));
            }
        }),
        UNIVERSE_RADIUS((tile, player, extra) -> {
            if (tile instanceof TileEntityUniverseGenerator ug) {
                ug.setRadius((int) Math.round(extra));
            }
        }),
        UNIVERSE_GRAVITY((tile, player, extra) -> {
            if (tile instanceof TileEntityUniverseGenerator ug) {
                ug.setGravity((int) Math.round(extra));
            }
        }),
        UNIVERSE_CRUST((tile, player, extra) -> {
            if (tile instanceof TileEntityUniverseGenerator ug) {
                ug.setCrust((int) Math.round(extra));
            }
        }),
        UNIVERSE_GENERATE((tile, player, extra) -> {
            if (tile instanceof TileEntityUniverseGenerator ug) {
                ug.generate();
            }
        }),
        UNIVERSE_DISCONNECT((tile, player, extra) -> {
            if (tile instanceof TileEntityUniverseGenerator ug) {
                ug.disconnect();
            }
        }),
        UNIVERSE_MINE((tile, player, extra) -> {
            if (tile instanceof TileEntityUniverseGenerator ug) {
                ug.setMining((int)(Math.round(extra)));
            }
        });
        
        private final TriConsumer<TileEntityMekanism, Player, Double> consumerForTile;

        MESGuiInteraction(TriConsumer<TileEntityMekanism, Player, Double> consumerForTile) {
            this.consumerForTile = consumerForTile;
        }

        public void consume(TileEntityMekanism tile, Player player, double extra) {
            this.consumerForTile.accept(tile, player, extra);
        }
    }
}
