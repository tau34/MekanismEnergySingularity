package io.github.tau34.mes.common.network;

import io.github.tau34.mes.MESLang;
import io.github.tau34.mes.common.register.MESContainerTypes;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeGui;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.BiFunction;

public class MESPacketGuiButtonPress implements IMekanismPacket {
    private final MESPacketGuiButtonPress.Type type;
    private MESPacketGuiButtonPress.ClickedTileButton tileButton;
    private int extra;
    private BlockPos tilePosition;

    public MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton buttonClicked, BlockEntity tile) {
        this(buttonClicked, tile.getBlockPos());
    }

    public MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton buttonClicked, BlockPos tilePosition) {
        this(buttonClicked, tilePosition, 0);
    }

    public MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton buttonClicked, BlockPos tilePosition, int extra) {
        this.type = MESPacketGuiButtonPress.Type.TILE;
        this.tileButton = buttonClicked;
        this.tilePosition = tilePosition;
        this.extra = extra;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            if (this.type == MESPacketGuiButtonPress.Type.TILE) {
                TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, player.level(), this.tilePosition);
                if (tile != null) {
                    MenuProvider provider = this.tileButton.getProvider(tile, this.extra);
                    if (provider != null) {
                        NetworkHooks.openScreen(player, provider, (buf) -> {
                            buf.writeBlockPos(this.tilePosition);
                            buf.writeVarInt(this.extra);
                        });
                    }
                }
            }

        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.type);
        if (this.type == MESPacketGuiButtonPress.Type.TILE) {
            buffer.writeEnum(this.tileButton);
            buffer.writeBlockPos(this.tilePosition);
            buffer.writeVarInt(this.extra);
        }

    }

    public static MESPacketGuiButtonPress decode(FriendlyByteBuf buffer) {
        if (buffer.readEnum(Type.class) == Type.TILE) {
            return new MESPacketGuiButtonPress(buffer.readEnum(ClickedTileButton.class), buffer.readBlockPos(), buffer.readVarInt());
        }
        throw new IncompatibleClassChangeError();
    }

    public static enum ClickedTileButton {
        BACK_BUTTON((tile, extra) -> {
            AttributeGui attributeGui = Attribute.get(tile.getBlockType(), AttributeGui.class);
            return attributeGui != null ? attributeGui.getProvider(tile) : null;
        }),
        TAB_UNIVERSE_CONFIG((tile, extra) -> tile instanceof TileEntityUniverseGenerator ? MESContainerTypes.UNIVERSE_CONFIG.getProvider(MESLang.UNIVERSE_CONFIG, tile) : null),
        TAB_UNIVERSE_GENERATOR_OUTPUT((tile, extra) -> tile instanceof TileEntityUniverseGenerator ? MESContainerTypes.UNIVERSE_GENERATOR_OUTPUT.getProvider(MESLang.UNIVERSE_GENERATOR_OUTPUT, tile) : null),
        TAB_UNIVERSE_GENERATOR_MANAGER((tile, extra) -> tile instanceof TileEntityUniverseGenerator ? MESContainerTypes.UNIVERSE_GENERATOR_MANAGER.getProvider(MESLang.UNIVERSE_GENERATOR_MANAGER, tile) : null);

        private final BiFunction<TileEntityMekanism, Integer, MenuProvider> providerFromTile;

        private ClickedTileButton(BiFunction<TileEntityMekanism, Integer, MenuProvider> providerFromTile) {
            this.providerFromTile = providerFromTile;
        }

        public MenuProvider getProvider(TileEntityMekanism tile, int extra) {
            return (MenuProvider)this.providerFromTile.apply(tile, extra);
        }
    }

    public static enum Type {
        TILE;

        private Type() {
        }
    }
}
