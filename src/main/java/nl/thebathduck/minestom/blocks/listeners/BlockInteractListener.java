package nl.thebathduck.minestom.blocks.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import nl.thebathduck.minestom.blocks.placement.DoorPlacement;
import nl.thebathduck.minestom.blocks.placement.SignPlacement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockInteractListener implements EventListener<PlayerBlockInteractEvent> {
    @Override
    public @NotNull Class<PlayerBlockInteractEvent> eventType() {
        return PlayerBlockInteractEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull PlayerBlockInteractEvent event) {
        Instance instance = event.getInstance();
        Block block = event.getBlock();

        if (MinecraftServer.getBlockManager().getBlockPlacementRule(event.getBlock()) instanceof DoorPlacement) {
            Block doorInteraction = block.withProperty("open", Objects.equals(block.getProperty("open"), "true") ? "false" : "true");

            boolean isUpper = doorInteraction.getProperty("half").equals("upper");
            event.getPlayer().sendMessage("isUpper: " + isUpper);

            Pos otherDoorPos = (isUpper ?
                    event.getBlockPosition().sub(0, 1, 0) :
                    event.getBlockPosition().add(0, 1, 0)
            ).asVec().asPosition();
            Block otherDoorInteraction = instance.getBlock(otherDoorPos).withProperty("open", Objects.equals(block.getProperty("open"), "true") ? "false" : "true");

            instance.setBlock(event.getBlockPosition(), doorInteraction);
            instance.setBlock(otherDoorPos, otherDoorInteraction);
        }

        return Result.SUCCESS;
    }
}
