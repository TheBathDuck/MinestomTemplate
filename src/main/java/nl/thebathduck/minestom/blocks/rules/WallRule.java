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

public class WallRule extends BlockRule {
    public WallRule(@NotNull Block block) {
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

        Block updated = updateState.currentBlock().withProperties(Map.of(
                States.NORTH, canConnect(instance, placePos, north, BlockFace.NORTH, BlockFace.SOUTH),
                States.EAST, canConnect(instance, placePos, east, BlockFace.EAST, BlockFace.WEST),
                States.SOUTH, canConnect(instance, placePos, south, BlockFace.SOUTH, BlockFace.NORTH),
                States.WEST, canConnect(instance, placePos, west, BlockFace.WEST, BlockFace.EAST)
        ));

        return updated.withProperty(States.UP, needUp(updated, instance.getBlock(placePos.relative(BlockFace.TOP))) ? "true" : "false");
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        Block.Getter instance = placementState.instance();
        Point placePos = placementState.placePosition();
        Point north = placePos.relative(BlockFace.NORTH);
        Point east = placePos.relative(BlockFace.EAST);
        Point south = placePos.relative(BlockFace.SOUTH);
        Point west = placePos.relative(BlockFace.WEST);

        Block updated = placementState.block().withProperties(Map.of(
                States.NORTH, canConnect(instance, placePos, north, BlockFace.NORTH, BlockFace.SOUTH),
                States.EAST, canConnect(instance, placePos, east, BlockFace.EAST, BlockFace.WEST),
                States.SOUTH, canConnect(instance, placePos, south, BlockFace.SOUTH, BlockFace.NORTH),
                States.WEST, canConnect(instance, placePos, west, BlockFace.WEST, BlockFace.EAST)
        ));

        return updated.withProperty(States.UP, needUp(updated, instance.getBlock(placePos.relative(BlockFace.TOP))) ? "true" : "false");
    }

    public boolean needUp(Block block, Block upBlock) {
        if (isWall(upBlock) && upBlock.getProperty("up").equalsIgnoreCase("true")) {
            return true;
        } else {
            int connected = 0;
            for (BlockFace blockFace : BlockFace.values()) {
                if (blockFace == BlockFace.TOP || blockFace == BlockFace.BOTTOM) continue;
                String connection = block.getProperty(blockFace.name().toLowerCase());
                if (connection == null) continue;
                if (!connection.equalsIgnoreCase("none")) {
                    connected++;
                }
            }

            if (connected == 0 || connected == 1 || connected == 3) {
                return true;
            } else if (connected == 2 || connected == 4) {
                if (connected == 2) {
                    boolean east = !block.getProperty("east").equalsIgnoreCase("none");
                    boolean west = !block.getProperty("west").equalsIgnoreCase("none");
                    boolean north = !block.getProperty("north").equalsIgnoreCase("none");
                    boolean south = !block.getProperty("south").equalsIgnoreCase("none");

                    return !(east == west) || !(north == south);
                } else {
                    if (isWall(upBlock)) {
                        return upBlock.getProperty("up").equalsIgnoreCase("true");
                    } else {
                        return upBlock.isSolid();
                    }
                }
            } else {
                return true;
            }
        }
    }

    private String canConnect(Block.Getter instance, Point self, Point pos, BlockFace selfBlockFace, BlockFace blockFace) {
        Block instanceBlock = instance.getBlock(pos);
        boolean canConnectToWall = canConnectToWall(instanceBlock);
        boolean canFenceGateConnect = instanceBlock.name().endsWith("_fence_gate") && States.getAxis(States.getFacing(instanceBlock).toDirection()).equals(States.getAxis(States.rotateYClockwise(blockFace.toDirection())));
        boolean isFaceFull = instanceBlock.registry().collisionShape().isFaceFull(blockFace);

        boolean connected = !cannotConnect(instanceBlock) && isFaceFull || canConnectToWall || canFenceGateConnect || instanceBlock.namespace().toString().endsWith("_bars") || instanceBlock.namespace().toString().endsWith("_pane");
        if (!connected) return "none";

        Block upBlock = instance.getBlock(self.relative(BlockFace.TOP));
        if (upBlock.isSolid() && !isWall(upBlock)) {
            return "tall";
        } else if (isWall(upBlock)) {
            if (!upBlock.getProperty(selfBlockFace.name().toLowerCase()).equalsIgnoreCase("none")) {
                return "tall";
            } else {
                return "low";
            }
        } else {
            return "low";
        }
    }

    private boolean canConnectToWall(Block block) {
        TagManager tagManager = MinecraftServer.getTagManager();
        return tagManager.getTag(Tag.BasicType.BLOCKS, "minecraft:walls").contains(block.namespace());
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

    public boolean isWall(Block block) {
        return block.namespace().toString().contains("_wall");
    }
}