package nl.thebathduck.minestom.blocks.rules;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import nl.thebathduck.minestom.blocks.state.States;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class TrapdoorRule extends BlockRule {
    public TrapdoorRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        BlockFace placementFace = placementState.blockFace();
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction direction = BlockFace.fromYaw(playerPos.yaw()).toDirection().opposite();
        Point cursorPos = Objects.requireNonNullElse(placementState.cursorPosition(), Vec.ZERO);
        BlockFace facing = BlockFace.fromDirection(direction);

        BlockFace half = placementFace == BlockFace.BOTTOM
                ||
                placementFace != BlockFace.TOP
                        &&
                        cursorPos.y() > 0.5
                ? BlockFace.TOP
                : BlockFace.BOTTOM;

        Block block = placementState.block().withProperties(Map.of(
                States.HALF, half.name().toLowerCase(),
                States.FACING, facing.name().toLowerCase()
        ));

        return canPlaceAt(placementState.instance(), placementState.placePosition(), placementState.blockFace().toDirection().opposite(), placementState.isPlayerShifting()) ? block : null;
    }

    @Override
    public void onInteract(PlayerBlockInteractEvent event) {
        Instance instance = event.getInstance();
        Block block = event.getBlock();

        if (event.getBlock().namespace().toString().contains("iron") && event.getPlayer().getGameMode() != GameMode.CREATIVE)
            return;

        Block trapdoorInteraction = block.withProperty("open", Objects.equals(block.getProperty("open"), "true") ? "false" : "true");
        instance.setBlock(event.getBlockPosition(), trapdoorInteraction);
    }
}