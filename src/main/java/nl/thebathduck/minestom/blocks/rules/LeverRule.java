package nl.thebathduck.minestom.blocks.rules;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import nl.thebathduck.minestom.blocks.state.States;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class LeverRule extends BlockRule {

    public LeverRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction face = Objects.requireNonNullElse(placementState.blockFace(), BlockFace.NORTH).toDirection();
        States.Axis axis = States.getAxis(face.opposite());
        Direction facing;
        if (axis == States.Axis.Y) {
            facing = BlockFace.fromYaw(playerPos.yaw()).toDirection();
        } else {
            facing = face;
        }

        Block block = placementState.block().withProperties(Map.of(
                States.FACE, axis == States.Axis.Y ? face.opposite() == Direction.UP ? "ceiling" : "floor" : "wall",
                States.FACING, facing.name().toLowerCase()
        ));

        Direction original = States.getDirection(block).opposite();
        return canPlaceAt(placementState.instance(), placementState.placePosition(), original, placementState.isPlayerShifting()) ? block : null;
    }
}