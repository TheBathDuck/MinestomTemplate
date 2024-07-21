package nl.thebathduck.minestom.blocks.listeners;


import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements EventListener<PlayerBlockBreakEvent> {
    @Override
    public @NotNull Class<PlayerBlockBreakEvent> eventType() {
        return PlayerBlockBreakEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull PlayerBlockBreakEvent event) {
        if (MinecraftServer.getBlockManager().getBlockPlacementRule(event.getBlock()) instanceof BlockRule blockRule) {
            blockRule.onBreak(event);
        }

        return Result.SUCCESS;
    }
}