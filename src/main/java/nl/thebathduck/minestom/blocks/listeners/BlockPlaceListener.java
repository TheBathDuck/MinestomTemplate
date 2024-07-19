package nl.thebathduck.minestom.blocks.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import nl.thebathduck.minestom.blocks.placement.SignPlacement;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceListener implements EventListener<PlayerBlockPlaceEvent> {
    @Override
    public @NotNull Class<PlayerBlockPlaceEvent> eventType() {
        return PlayerBlockPlaceEvent.class;
    }

    @NotNull
    @Override
    public Result run(@NotNull PlayerBlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (MinecraftServer.getBlockManager().getBlockPlacementRule(event.getBlock()) instanceof SignPlacement) {
            player.sendPacket(new OpenSignEditorPacket(event.getBlockPosition(), true));
        }

        return Result.SUCCESS;
    }
}
