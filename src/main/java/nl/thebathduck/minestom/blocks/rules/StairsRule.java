package nl.thebathduck.minestom.blocks.rules;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import nl.thebathduck.minestom.blocks.state.States;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class StairsRule extends BlockRule {

    public StairsRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @NotNull Block blockUpdate(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.UpdateState updateState) {
        return updateState.currentBlock().withProperty(States.SHAPE, getShape(updateState.instance(), updateState.currentBlock(), updateState.blockPosition()));
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        BlockFace placementFace = placementState.blockFace();
        Point placementPos = placementState.placePosition();
        Point cursorPos = Objects.requireNonNullElse(placementState.cursorPosition(), Vec.ZERO);
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);

        BlockFace half = placementFace == BlockFace.BOTTOM
                ||
                placementFace != BlockFace.TOP
                &&
                cursorPos.y() > 0.5
                ? BlockFace.TOP
                : BlockFace.BOTTOM;
        BlockFace facing = BlockFace.fromYaw(playerPos.yaw());

        Block block = this.block.withProperties(Map.of(
            States.HALF, half.name().toLowerCase(),
            States.FACING, facing.name().toLowerCase()
        ));

        block = block.withProperty(States.SHAPE, getShape(placementState.instance(), block, placementPos));

        Block offset = placementState.instance().getBlock(placementPos.add(placementFace.toDirection().opposite().normalX(), placementFace.toDirection().opposite().normalY(), placementFace.toDirection().opposite().normalZ()));
        boolean canPlace = canPlaceAt(placementState.instance(), placementPos, placementFace.toDirection().opposite(), placementState.isPlayerShifting()) || offset.namespace().toString().contains("_stairs");
        return canPlace ? block : null;

    }

    private String getShape(Block.Getter instance, Block block, Point blockPos) {
        Direction direction = States.getFacing(block).toDirection();
        Block offsetBlock = instance.getBlock(blockPos.add(direction.normalX(), direction.normalY(), direction.normalZ()));
        Direction offsetDirection = States.getFacing(offsetBlock).toDirection();
        Block oppositeOffsetBlock = instance.getBlock(blockPos.add(direction.opposite().normalX(), direction.opposite().normalY(), direction.opposite().normalZ()));
        Direction oppositeOffsetDirection = States.getFacing(oppositeOffsetBlock).toDirection();

        if (isStairs(offsetBlock)
            &&
            States.getHalf(block) == States.getHalf(offsetBlock)
            &&
            States.getAxis(offsetDirection) != States.getAxis(direction)
            &&
            isDifferentOrientation(instance, block, blockPos, offsetDirection.opposite())
        ) {
            if (offsetDirection == States.rotateYCounterclockwise(direction)) {
                return "outer_left";
            } else {
                return "outer_right";
            }
        }

        if (isStairs(oppositeOffsetBlock)
            &&
            States.getHalf(block) == States.getHalf(oppositeOffsetBlock)
            &&
            States.getAxis(oppositeOffsetDirection) != States.getAxis(direction)
            &&
            isDifferentOrientation(instance, block, blockPos, oppositeOffsetDirection)
        ) {
            if (oppositeOffsetDirection == States.rotateYCounterclockwise(direction)) {
                return "inner_left";
            } else {
                return "inner_right";
            }
        }

        return "straight";
    }

    private boolean isDifferentOrientation(Block.Getter instance, Block block, Point blockPos, Direction direction) {
        BlockFace facing = States.getFacing(block);
        BlockFace half = States.getHalf(block);
        Block instanceBlock = instance.getBlock(blockPos.add(direction.normalX(), direction.normalY(), direction.normalZ()));
        BlockFace instanceBlockFacing = States.getFacing(instanceBlock);
        BlockFace instanceBlockHalf = States.getHalf(instanceBlock);

        return !isStairs(instanceBlock) || instanceBlockFacing != facing || instanceBlockHalf != half;
    }

    private boolean isStairs(Block block) {
        return block.name().endsWith("_stairs");
    }
}