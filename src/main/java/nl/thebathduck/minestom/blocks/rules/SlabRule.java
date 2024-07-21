package nl.thebathduck.minestom.blocks.rules;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class SlabRule extends BlockRule {
    private static final String PROP_TYPE = "type";

    public SlabRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        var existingBlock = placementState.instance().getBlock(placementState.placePosition());
        if (existingBlock.id() == this.block.id()) {
            return existingBlock.withProperty(PROP_TYPE, "double");
        }

        var blockFace = placementState.blockFace();
        if (blockFace == BlockFace.TOP) return block.withProperty(PROP_TYPE, "bottom");
        if (blockFace == BlockFace.BOTTOM) return block.withProperty(PROP_TYPE, "top");

        var type = Objects.requireNonNullElse(placementState.cursorPosition(), Vec.ZERO).y() > 0.5 ? "top" : "bottom";
        return block.withProperty(PROP_TYPE, type);
    }

    @Override
    public boolean isSelfReplaceable(@NotNull Replacement replacement) {
        var block = replacement.block();
        var newMaterial = replacement.material();
        if (block.id() != this.block.id() || !newMaterial.isBlock() || block.id() != newMaterial.block().id())
            return false;

        var type = block.getProperty(PROP_TYPE);
        var blockFace = replacement.blockFace();
        if ((blockFace == BlockFace.TOP && "bottom".equals(type)) ||
                (blockFace == BlockFace.BOTTOM && "top".equals(type)))
            return true;

        var cursorPosition = replacement.cursorPosition();
        return ("bottom".equals(type) && cursorPosition.y() > 0.5) ||
                ("top".equals(type) && cursorPosition.y() < 0.5);
    }

}