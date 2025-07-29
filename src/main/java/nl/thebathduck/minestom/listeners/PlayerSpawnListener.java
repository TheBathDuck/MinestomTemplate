package nl.thebathduck.minestom.listeners;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import nl.thebathduck.minestom.player.PermissionPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {

    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull PlayerSpawnEvent event) {
        PermissionPlayer player = (PermissionPlayer) event.getPlayer();

        if(player.hasPermission("i.can.fly")) {
            player.setFlying(true);
            player.setAllowFlying(true);
            player.sendMessage("You are so cool, you might fly!");
        }

        player.teleport(new Pos(-70, 125, -1400));
        return Result.SUCCESS;
    }

}
