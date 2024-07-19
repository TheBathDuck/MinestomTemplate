package nl.thebathduck.minestom.blocks.placement;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.BlockUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AnvilPlacement extends BlockPlacementRule {

    public AnvilPlacement(Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction direction = BlockFace.fromYaw(playerPos.yaw()).toDirection();
        BlockFace facing = BlockFace.fromDirection(direction);

        return placementState.block().withProperty("facing", BlockUtils.getLeftFace(facing).getOppositeFace().name().toLowerCase());
    }
}
