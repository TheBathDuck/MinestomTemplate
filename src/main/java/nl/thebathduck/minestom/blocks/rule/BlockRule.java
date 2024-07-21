package nl.thebathduck.minestom.blocks.rule;

import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BlockRule extends net.minestom.server.instance.block.rule.BlockPlacementRule {
    private final List<String> INTERACTION_BLOCK_NAMES = List.of("_button", "minecraft:anvil", "_anvil", "_trapdoor");

    protected BlockRule(@NotNull Block block) {
        super(block);
    }

    // Override this method to handle block interactions
    public void onInteract(PlayerBlockInteractEvent event) {
    }

    // Override this method to handle block placements
    public void onPlace(PlayerBlockPlaceEvent event) {
    }

    // Override this method to handle block breaks
    public void onBreak(PlayerBlockBreakEvent event) {
    }

    protected boolean canPlaceAt(Block.Getter instance, Point blockPos, Direction direction, boolean shifting) {
        Block offset = instance.getBlock(blockPos.add(direction.normalX(), direction.normalY(), direction.normalZ()));

        if (((INTERACTION_BLOCK_NAMES.contains(offset.name()) || INTERACTION_BLOCK_NAMES.stream().filter(s -> offset.name().endsWith(s)).findFirst().orElse(null) != null) && !shifting)) return false;
        return offset.registry().collisionShape().isFaceFull(BlockFace.fromDirection(direction).getOppositeFace());
    }
}