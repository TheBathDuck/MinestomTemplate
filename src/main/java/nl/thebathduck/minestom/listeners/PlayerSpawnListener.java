package nl.thebathduck.minestom.listeners;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {
    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull PlayerSpawnEvent event) {
        final Player player = event.getPlayer();
        player.teleport(new Pos(-70, 125, -1400));
        return Result.SUCCESS;
    }
}
