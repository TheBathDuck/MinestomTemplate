package nl.thebathduck.minestom.blocks.rules;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class SkullRule extends BlockRule {
    public SkullRule(Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        if (placementState.playerPosition() == null) return block;
        float yaw = placementState.playerPosition().yaw();

        BlockFace face = placementState.blockFace();
        if (face != BlockFace.TOP && face != BlockFace.BOTTOM) {
            return Block.fromNamespaceId(block.namespace().toString().replace("_skull", "_wall_skull").replace("_head", "_wall_head"))
                    .withProperty("facing", face.name().toLowerCase(Locale.ROOT));
        }

        return block.withProperty("rotation", "" + ((int) Math.floor((yaw * 16.0F / 360.0F) + 0.5D) & 15));
    }

}
