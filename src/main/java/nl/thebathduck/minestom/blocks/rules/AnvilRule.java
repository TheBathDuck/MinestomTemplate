package nl.thebathduck.minestom.blocks.rules;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import nl.thebathduck.minestom.blocks.state.States;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class AnvilRule extends BlockRule {

    public AnvilRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction direction = States.rotateYCounterclockwise(BlockFace.fromYaw(playerPos.yaw()).toDirection());
        BlockFace facing = BlockFace.fromDirection(direction);

        Block block = placementState.block().withProperty(States.FACING, facing.name().toLowerCase());

        return canPlaceAt(placementState.instance(), placementState.placePosition(), placementState.blockFace().toDirection().opposite(), placementState.isPlayerShifting()) ? block : null;
    }
}