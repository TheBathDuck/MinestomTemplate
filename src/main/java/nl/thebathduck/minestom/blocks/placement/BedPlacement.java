package nl.thebathduck.minestom.blocks.placement;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class BedPlacement extends BlockPlacementRule {
    public BedPlacement(Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        if (placementState.playerPosition()  == null) return block;
        Pos playerPosition = placementState.playerPosition();

        BlockFace facing = BlockFace.fromYaw(playerPosition.yaw());

        Point placed = placementState.placePosition();
        Point relative = placed.add(facing.toDirection().vec().asPosition());

        Instance instance = (Instance) placementState.instance();
        instance.setBlock(relative, placementState.block().registry().material().block()
                .withProperty("part", "head")
                .withProperty("facing", facing.name().toLowerCase())
        );

        return block.withProperty("facing", facing.name().toLowerCase());
    }

}
