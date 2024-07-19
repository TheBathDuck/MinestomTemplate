package nl.thebathduck.minestom.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@UtilityClass
public class ChatUtils {
    public Component color(String message) {
        MiniMessage miniMessage = MiniMessage.builder()
                .editTags(tags -> {
                    tags.resolver(TagResolver.resolver("primary", Tag.styling(TextColor.fromHexString("#43FF61"))));
                    tags.resolver(TagResolver.resolver("secondary", Tag.styling(TextColor.fromHexString("#35bd62"))));
                    tags.resolver(TagResolver.resolver("danger", Tag.styling(TextColor.fromHexString("#ff999b"))));
                }).build();
        return miniMessage.deserialize(message).decoration(TextDecoration.ITALIC, false);
    }
}