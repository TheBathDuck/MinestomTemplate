package nl.thebathduck.minestom.blocks.packet;

import net.kyori.adventure.nbt.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.listener.manager.PacketPrePlayListenerConsumer;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class SignPacketListener implements PacketPrePlayListenerConsumer<ClientUpdateSignPacket> {

    @Override
    public void accept(ClientUpdateSignPacket signPacket, PlayerConnection playerConnection) {
        final Player player = playerConnection.getPlayer();
        if (player == null) return;
        Instance instance = player.getInstance();
        if (instance == null) return;

        Block block = instance.getBlock(signPacket.blockPosition());
        if (!block.namespace().asString().contains("sign")) return;

        List<BinaryTag> signLines = new ArrayList<>();
        signPacket.lines().forEach(line -> {
            signLines.add(StringBinaryTag.stringBinaryTag(JSONComponentSerializer.json().serialize(Component.text(line))));
        });

        CompoundBinaryTag tag = CompoundBinaryTag.builder()
                .put("has_glowing_text", ByteBinaryTag.byteBinaryTag((byte) 0))
                .put("color", StringBinaryTag.stringBinaryTag("black"))
                .put("messages", ListBinaryTag.listBinaryTag(BinaryTagTypes.STRING, signLines))
                .build();

        Block newSign = block.withTag(Tag.NBT("front_text"), tag);
        instance.setBlock(signPacket.blockPosition(), newSign);
    }
}