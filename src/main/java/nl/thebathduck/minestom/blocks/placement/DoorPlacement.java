package nl.thebathduck.minestom.blocks.placement;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class DoorPlacement extends BlockPlacementRule {

    public DoorPlacement(Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
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
}
