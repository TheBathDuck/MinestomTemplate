package nl.thebathduck.minestom.blocks.placement.rotational;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RotationalPlacement extends BlockPlacementRule {

    private final boolean opposite;

    public RotationalPlacement(Block block, boolean opposite) {
        super(block);
        this.opposite = opposite;
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction direction = BlockFace.fromYaw(playerPos.yaw()).toDirection();
        BlockFace facing = BlockFace.fromDirection(direction);

        BlockFace newFace = (opposite ? facing.getOppositeFace() : facing);

        return placementState.block().withProperty("facing", newFace.name().toLowerCase());
    }
}
