package nl.thebathduck.minestom.blocks.rules;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.minestom.server.utils.Direction;
import nl.thebathduck.minestom.blocks.rule.BlockRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class SignRule extends BlockRule {
    public SignRule(Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull net.minestom.server.instance.block.rule.BlockPlacementRule.PlacementState placementState) {
        if (placementState.playerPosition() == null) return block;
        float yaw = placementState.playerPosition().yaw() + 180;

        BlockFace face = placementState.blockFace();
        if (!block.namespace().toString().contains("hanging")) {
            if (face != BlockFace.TOP && face != BlockFace.BOTTOM) {
                return Block.fromNamespaceId(block.namespace().toString().replace("_sign", "_wall_sign"))
                        .withProperty("facing", face.name().toLowerCase(Locale.ROOT));
            }
        } else {
            if (face == BlockFace.TOP) return null;
            if (face != BlockFace.BOTTOM) {
                Direction dirFace = getNextDirection(face.toDirection());
                return Block.fromNamespaceId(block.namespace().toString().replace("_hanging_sign", "_wall_hanging_sign"))
                        .withProperty("facing", dirFace.name().toLowerCase(Locale.ROOT));
            }
        }

        return block.withProperty("rotation", "" + ((int) Math.floor((yaw * 16.0F / 360.0F) + 0.5D) & 15));
    }

    private Direction getNextDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
            default -> direction;  // If for some reason it's not one of the four primary directions
        };
    }

    @Override
    public void onPlace(PlayerBlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlockFace() == BlockFace.TOP && event.getBlock().namespace().toString().contains("_hanging_sign")) {
            event.setCancelled(true);
            return;
        }

        player.sendPacket(new OpenSignEditorPacket(event.getBlockPosition(), true));
    }
}