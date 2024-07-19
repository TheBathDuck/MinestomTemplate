package nl.thebathduck.minestom.blocks.placement;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignPlacement extends BlockPlacementRule {

    private final Map<NamespaceID, Block> replacements = new HashMap<>();

    public SignPlacement(Block block) {
        super(block);

        replacements.put(NamespaceID.from("minecraft:acacia_sign"), Block.ACACIA_WALL_SIGN);
        replacements.put(NamespaceID.from("minecraft:warped_sign"), Block.WARPED_WALL_SIGN);
        replacements.put(NamespaceID.from("minecraft:birch_sign"), Block.BIRCH_WALL_SIGN);
        replacements.put(NamespaceID.from("minecraft:oak_sign"), Block.OAK_WALL_SIGN);
        replacements.put(NamespaceID.from("minecraft:crimson_sign"), Block.CRIMSON_WALL_SIGN);
        replacements.put(NamespaceID.from("minecraft:dark_oak_sign"), Block.DARK_OAK_WALL_SIGN);
        replacements.put(NamespaceID.from("minecraft:jungle_sign"), Block.JUNGLE_WALL_SIGN);
        replacements.put(NamespaceID.from("minecraft:spruce_sign"), Block.SPRUCE_WALL_SIGN);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        if (placementState.playerPosition() == null) return block;
        float yaw = placementState.playerPosition().yaw() + 180;

        BlockFace face = placementState.blockFace();
        if (face != BlockFace.TOP && face != BlockFace.BOTTOM) {
            return replacements.get(block.namespace()).withProperty("facing", face.name().toLowerCase(Locale.ROOT));
        }

        return block.withProperty("rotation", "" + ((int) Math.floor((yaw * 16.0F / 360.0F) + 0.5D) & 15));
    }

}
