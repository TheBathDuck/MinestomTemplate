package nl.thebathduck.minestom.blocks.rules;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.gamedata.tags.Tag;
import net.minestom.server.gamedata.tags.TagManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import nl.thebathduck.minestom.blocks.state.States;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FenceRule extends BlockRule {
    public FenceRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @NotNull Block blockUpdate(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.UpdateState updateState) {
        Block.Getter instance = updateState.instance();
        Point placePos = updateState.blockPosition();
        Point north = placePos.relative(BlockFace.NORTH);
        Point east = placePos.relative(BlockFace.EAST);
        Point south = placePos.relative(BlockFace.SOUTH);
        Point west = placePos.relative(BlockFace.WEST);

        return updateState.currentBlock().withProperties(Map.of(
                States.NORTH, String.valueOf(canConnect(instance, north, BlockFace.SOUTH)),
                States.EAST, String.valueOf(canConnect(instance, east, BlockFace.WEST)),
                States.SOUTH, String.valueOf(canConnect(instance, south, BlockFace.NORTH)),
                States.WEST, String.valueOf(canConnect(instance, west, BlockFace.EAST))
        ));
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        Block.Getter instance = placementState.instance();
        Point placePos = placementState.placePosition();
        Point north = placePos.relative(BlockFace.NORTH);
        Point east = placePos.relative(BlockFace.EAST);
        Point south = placePos.relative(BlockFace.SOUTH);
        Point west = placePos.relative(BlockFace.WEST);


        return placementState.block().withProperties(Map.of(
                States.NORTH, String.valueOf(canConnect(instance, north, BlockFace.SOUTH)),
                States.EAST, String.valueOf(canConnect(instance, east, BlockFace.WEST)),
                States.SOUTH, String.valueOf(canConnect(instance, south, BlockFace.NORTH)),
                States.WEST, String.valueOf(canConnect(instance, west, BlockFace.EAST))
        ));
    }

    private boolean canConnect(Block.Getter instance, Point pos, BlockFace blockFace) {
        Block instanceBlock = instance.getBlock(pos);
        boolean isBlockNetherBrickFence = block.name().endsWith("_brick_fence");
        boolean isInstanceBlockNetherBrickFence = instanceBlock.name().endsWith("_brick_fence");
        boolean canConnectToFence = canConnectToFence(instanceBlock);
        boolean canFenceGateConnect = instanceBlock.name().endsWith("_fence_gate") && States.getAxis(States.getFacing(instanceBlock).toDirection()).equals(States.getAxis(States.rotateYClockwise(blockFace.toDirection())));
        boolean isFaceFull = instanceBlock.registry().collisionShape().isFaceFull(blockFace);


        return !cannotConnect(instanceBlock) && isFaceFull || (canConnectToFence && !isBlockNetherBrickFence) || canFenceGateConnect || (isBlockNetherBrickFence && isInstanceBlockNetherBrickFence);
    }

    private boolean canConnectToFence(Block block) {
        TagManager tagManager = MinecraftServer.getTagManager();
        boolean isFence = tagManager.getTag(Tag.BasicType.BLOCKS, "minecraft:fences").contains(block.namespace());
        boolean isWoodenFence = tagManager.getTag(Tag.BasicType.BLOCKS, "minecraft:wooden_fences").contains(block.namespace());

        return isFence && isWoodenFence;
    }

    private boolean cannotConnect(Block block) {
        String name = block.name().replaceAll("minecraft:", "");
        return name.endsWith("leaves")
            ||
            name.equals("barrier")
            ||
            name.equals("carved_pumpkin")
            ||
            name.equals("jack_o_lantern")
            ||
            name.equals("melon")
            ||
            name.equals("pumpkin")
            ||
            name.endsWith("_shulker_box");
    }
}