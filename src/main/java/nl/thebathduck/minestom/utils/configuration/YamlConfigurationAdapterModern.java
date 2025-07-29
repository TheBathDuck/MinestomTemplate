package nl.thebathduck.minestom.utils.configuration;

import me.lucko.luckperms.common.config.generic.adapter.ConfigurationAdapter;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;
import me.lucko.luckperms.minestom.LPMinestomPlugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

public final class YamlConfigurationAdapterModern extends ModernConfigurateConfigAdapter implements ConfigurationAdapter {
    public YamlConfigurationAdapterModern(LuckPermsPlugin plugin) {
        super(plugin, ((LPMinestomPlugin) plugin).resolveConfig("luckperms.yml"));
    }

    @Override
    protected ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path) {
        return YamlConfigurationLoader.builder().path(path).build();
    }
}