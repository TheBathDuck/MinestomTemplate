package nl.thebathduck.minestom.blocks;

import lombok.experimental.UtilityClass;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import nl.thebathduck.minestom.blocks.handlers.SignHandler;
import nl.thebathduck.minestom.blocks.listeners.BlockPlaceListener;
import nl.thebathduck.minestom.blocks.packet.SignPacketListener;
import nl.thebathduck.minestom.blocks.placement.SignPlacement;
import nl.thebathduck.minestom.blocks.placement.SlabPlacement;
import nl.thebathduck.minestom.blocks.placement.StairPlacement;

@UtilityClass
public class BlockUtils {

    public void register() {
        Block.values().stream().filter(block -> block.namespace().asString().contains("slab")).forEach(block -> {
            MinecraftServer.getBlockManager().registerBlockPlacementRule(new SlabPlacement(block));
        });

        Block.values().stream().filter(block -> block.namespace().asString().contains("stair")).forEach(block -> {
            MinecraftServer.getBlockManager().registerBlockPlacementRule(new StairPlacement(block));
        });

        Block.values().stream().filter(block -> block.namespace().asString().contains("sign")).forEach(block -> {
            MinecraftServer.getBlockManager().registerBlockPlacementRule(new SignPlacement(block));
        });

        MinecraftServer.getBlockManager().registerHandler("minecraft:sign", SignHandler::new);
        MinecraftServer.getPacketListenerManager().setListener(ConnectionState.PLAY, ClientUpdateSignPacket.class, new SignPacketListener());
        MinecraftServer.getGlobalEventHandler().addListener(new BlockPlaceListener());
    }

}
