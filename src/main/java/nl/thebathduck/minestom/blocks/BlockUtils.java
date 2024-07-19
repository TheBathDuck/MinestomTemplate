package nl.thebathduck.minestom.blocks;

import lombok.experimental.UtilityClass;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.tag.Tag;
import nl.thebathduck.minestom.Server;
import nl.thebathduck.minestom.blocks.handlers.SignHandler;
import nl.thebathduck.minestom.blocks.listeners.BlockBreakListener;
import nl.thebathduck.minestom.blocks.listeners.BlockInteractListener;
import nl.thebathduck.minestom.blocks.listeners.BlockPlaceListener;
import nl.thebathduck.minestom.blocks.packet.SignPacketListener;
import nl.thebathduck.minestom.blocks.placement.*;

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

        Block.values().stream().filter(block -> block.namespace().asString().contains("_door")).forEach(block -> {
            MinecraftServer.getBlockManager().registerBlockPlacementRule(new DoorPlacement(block));
        });

        Block.values().stream().filter(block -> block.namespace().asString().endsWith("_anvil") || block.name().equals("minecraft:anvil")).forEach(block -> {
            Server.getLogger().info("Registered for: " + block.namespace().asString());
            MinecraftServer.getBlockManager().registerBlockPlacementRule(new RotationalPlacement(block));
        });


        MinecraftServer.getBlockManager().registerHandler("minecraft:sign", SignHandler::new);
        MinecraftServer.getPacketListenerManager().setListener(ConnectionState.PLAY, ClientUpdateSignPacket.class, new SignPacketListener());

        MinecraftServer.getGlobalEventHandler().addListener(new BlockPlaceListener());
        MinecraftServer.getGlobalEventHandler().addListener(new BlockBreakListener());
        MinecraftServer.getGlobalEventHandler().addListener(new BlockInteractListener());
    }

    public BlockFace getLeftFace(BlockFace face) {
        switch (face) {
            case NORTH -> {
                return BlockFace.WEST;
            }
            case SOUTH -> {
                return BlockFace.EAST;
            }
            case WEST -> {
                return BlockFace.SOUTH;
            }
            case EAST -> {
                return BlockFace.NORTH;
            }
        }
        return BlockFace.NORTH;
    }

}
