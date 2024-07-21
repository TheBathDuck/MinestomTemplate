package nl.thebathduck.minestom.blocks.handler;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public final class SkullHandler implements BlockHandler {

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.NBT("profile")
        );
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("minecraft:skull");
    }
}