package nl.thebathduck.minestom.blocks.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;

public class BlockInteractListener implements EventListener<PlayerBlockInteractEvent> {
    @Override
    public @NotNull Class<PlayerBlockInteractEvent> eventType() {
        return PlayerBlockInteractEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull PlayerBlockInteractEvent event) {
        if (event.getHand() == Player.Hand.OFF) return Result.INVALID;
        if (MinecraftServer.getBlockManager().getBlockPlacementRule(event.getBlock()) instanceof BlockRule blockRule) {
            blockRule.onInteract(event);
        }

        return Result.SUCCESS;
    }
}