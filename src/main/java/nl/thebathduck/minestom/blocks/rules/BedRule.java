package nl.thebathduck.minestom.blocks.rules;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BedRule extends BlockRule {
    private static final String PROP_PART = "part";
    private static final String PROP_FACING = "facing";

    public BedRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        if (!canPlaceAt(placementState.instance(), placementState.placePosition(), placementState.blockFace().toDirection().opposite(), placementState.isPlayerShifting())) return null;

        var playerPosition = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        var facing = BlockFace.fromYaw(playerPosition.yaw());

        //todo bad code using instance directly
        if (!(placementState.instance() instanceof Instance instance)) return null;

        var headPosition = placementState.placePosition().relative(facing);
        if (!instance.getBlock(headPosition, Block.Getter.Condition.TYPE).isAir())
            return null;

        var headBlock = this.block.withProperty(PROP_PART, "head")
                .withProperty(PROP_FACING, facing.name().toLowerCase());
        instance.setBlock(headPosition, headBlock);

        return headBlock.withProperty(PROP_PART, "foot");
    }

    @Override
    public void onBreak(PlayerBlockBreakEvent event) {
        Instance instance = event.getInstance();
        Block block = event.getBlock();
        Point blockPoint = event.getBlockPosition();

        boolean isHead = block.getProperty("part").equals("head");
        BlockFace face = BlockFace.valueOf(block.getProperty("facing").toUpperCase());
        Point otherPoint = isHead ? blockPoint.add(face.getOppositeFace().toDirection().vec().asPosition()) : blockPoint.add(face.toDirection().vec().asPosition());

        instance.setBlock(otherPoint, Block.AIR);

        boolean isFoot = block.getProperty("part").equals("foot");
        Direction facing = Direction.valueOf(block.getProperty("facing").toUpperCase());
        if (isFoot) {
            facing = facing.opposite();
        }

        Point otherPartPosition = blockPoint.add(facing.normalX(), facing.normalY(), facing.normalZ());
        instance.setBlock(blockPoint, Block.AIR);
        instance.setBlock(otherPartPosition, Block.AIR);
    }
}