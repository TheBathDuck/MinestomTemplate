package nl.thebathduck.minestom.utils.configuration;


import lombok.Getter;
import lombok.SneakyThrows;
import nl.thebathduck.minestom.Bootstrap;
import nl.thebathduck.minestom.Server;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class ConfigurateConfig {
    protected final YamlConfigurationLoader loader;
    protected ConfigurationNode rootNode;

    @SneakyThrows
    public ConfigurateConfig(String name) {
        loader = YamlConfigurationLoader.builder()
                .path(new File(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().toPath().resolve(name))
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .headerMode(HeaderMode.PRESET)
                .build();

        try {
            rootNode = loader.load();
        } catch (IOException e) {
            Server.getLogger().warn("An error occurred while loading this configuration: {}", e.getMessage());
        }
    }

    public void saveConfiguration() {
        try {
            loader.save(rootNode);
        } catch (Exception e) {
            Server.getLogger().warn("Unable to save your messages configuration! Sorry! {}", e.getMessage());
        }
    }
}