package nl.thebathduck.minestom.blocks.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceListener implements EventListener<PlayerBlockPlaceEvent> {
    @Override
    public @NotNull Class<PlayerBlockPlaceEvent> eventType() {
        return PlayerBlockPlaceEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull PlayerBlockPlaceEvent event) {
        if (MinecraftServer.getBlockManager().getBlockPlacementRule(event.getBlock()) instanceof BlockRule blockRule) {
            blockRule.onPlace(event);
        }

        return Result.SUCCESS;
    }
}