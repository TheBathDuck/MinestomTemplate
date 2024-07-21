package nl.thebathduck.minestom.blocks.rules;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FenceGateRule extends BlockRule {
    public FenceGateRule(Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        if (!canPlaceAt(placementState.instance(), placementState.placePosition(), placementState.blockFace().toDirection().opposite(), placementState.isPlayerShifting())) return null;

        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction direction = BlockFace.fromYaw(playerPos.yaw()).toDirection();
        BlockFace facing = BlockFace.fromDirection(direction);

        return placementState.block().withProperty("facing", facing.name().toLowerCase());
    }

    @Override
    public void onInteract(PlayerBlockInteractEvent event) {
        Instance instance = event.getInstance();
        Block block = event.getBlock();
        Direction direction = event.getBlockFace().getOppositeFace().toDirection();

        Block gateInteraction = block.withProperty("open", Objects.equals(block.getProperty("open"), "true") ? "false" : "true")
                .withProperty("facing", direction.name().toLowerCase());

        instance.setBlock(event.getBlockPosition(), gateInteraction);
    }
}