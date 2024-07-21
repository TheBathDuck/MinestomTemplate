package nl.thebathduck.minestom.blocks.rules;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class DoorRule extends BlockRule {

    public DoorRule(Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction direction = BlockFace.fromYaw(playerPos.yaw()).toDirection();
        Point cursorPos = Objects.requireNonNullElse(placementState.cursorPosition(), Vec.ZERO);
        BlockFace facing = BlockFace.fromDirection(direction);
        boolean rightHinge = calculateHingeSide(cursorPos, facing);

        Instance instance = (Instance) placementState.instance();

        Block lowerHalf = placementState.block().withProperties(Map.of(
                "facing", facing.name().toLowerCase(),
                "half", "lower",
                "hinge", rightHinge ? "right" : "left",
                "open", "false",
                "powered", "false"
        ));

        Block upperHalf = placementState.block().withProperties(Map.of(
                "facing", facing.name().toLowerCase(),
                "half", "upper",
                "hinge", rightHinge ? "right" : "left",
                "open", "false",
                "powered", "false"
        ));

        instance.setBlock(placementState.placePosition().add(0, 1, 0), upperHalf);

        return lowerHalf;
    }

    private boolean calculateHingeSide(Point cursorPos, BlockFace facing) {
        switch (facing) {
            case NORTH:
                return cursorPos.x() > 0.5;
            case SOUTH:
                return cursorPos.x() < 0.5;
            case WEST:
                return cursorPos.z() < 0.5;
            case EAST:
                return cursorPos.z() > 0.5;
            default:
                return false;
        }
    }

    @Override
    public void onInteract(PlayerBlockInteractEvent event) {
        Instance instance = event.getInstance();
        Block block = event.getBlock();

        if (event.getBlock().namespace().toString().contains("iron") && event.getPlayer().getGameMode() != GameMode.CREATIVE) return;
        Block doorInteraction = block.withProperty("open", Objects.equals(block.getProperty("open"), "true") ? "false" : "true");

        boolean isUpper = doorInteraction.getProperty("half").equals("upper");

        Pos otherDoorPos = (isUpper ?
                event.getBlockPosition().sub(0, 1, 0) :
                event.getBlockPosition().add(0, 1, 0)
        ).asVec().asPosition();
        Block otherDoorInteraction = instance.getBlock(otherDoorPos).withProperty("open", Objects.equals(block.getProperty("open"), "true") ? "false" : "true");

        instance.setBlock(event.getBlockPosition(), doorInteraction);
        instance.setBlock(otherDoorPos, otherDoorInteraction);
    }

    @Override
    public void onBreak(PlayerBlockBreakEvent event) {
        Instance instance = event.getInstance();
        Block block = event.getBlock();

        boolean isUpper = block.getProperty("half").equals("upper");
        Pos otherDoorPos = (isUpper ? event.getBlockPosition().sub(0, 1, 0) : event.getBlockPosition().add(0, 1, 0)).asVec().asPosition();
        Block doorPart = instance.getBlock(otherDoorPos);

        if (!doorPart.namespace().asString().contains("door")) return;
        instance.setBlock(otherDoorPos, Block.AIR);
    }
}