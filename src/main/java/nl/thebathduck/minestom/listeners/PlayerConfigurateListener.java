package nl.thebathduck.minestom.listeners;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import nl.thebathduck.minestom.Server;
import org.jetbrains.annotations.NotNull;

public class PlayerConfigurateListener implements EventListener<AsyncPlayerConfigurationEvent> {

    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull AsyncPlayerConfigurationEvent event) {
        final Player player = event.getPlayer();
        event.setSpawningInstance(Server.getInstance().getWorldInstance());
        player.setGameMode(GameMode.CREATIVE);
        return Result.SUCCESS;
    }

}
