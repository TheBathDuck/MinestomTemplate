package nl.thebathduck.minestom.blocks.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import nl.thebathduck.minestom.blocks.placement.DoorPlacement;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements EventListener<PlayerBlockBreakEvent> {
    @Override
    public @NotNull Class<PlayerBlockBreakEvent> eventType() {
        return PlayerBlockBreakEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull PlayerBlockBreakEvent event) {
        Instance instance = event.getInstance();

        if (MinecraftServer.getBlockManager().getBlockPlacementRule(event.getBlock()) instanceof DoorPlacement) {
            Block block = event.getBlock();
            boolean isUpper = block.getProperty("half").equals("upper");

            Pos otherDoorPos = (isUpper ?
                    event.getBlockPosition().sub(0, 1, 0) :
                    event.getBlockPosition().add(0, 1, 0)
            ).asVec().asPosition();

            Block doorPart = instance.getBlock(otherDoorPos);
            if(!doorPart.namespace().asString().contains("door")) return Result.INVALID;
            instance.setBlock(otherDoorPos, Block.AIR);
        }

        return Result.SUCCESS;
    }
}
