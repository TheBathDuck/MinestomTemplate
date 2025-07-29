package nl.thebathduck.minestom.utils.configuration;

import com.google.common.base.Splitter;
import lombok.SneakyThrows;
import me.lucko.luckperms.common.config.generic.adapter.ConfigurationAdapter;
import me.lucko.luckperms.common.plugin.LuckPermsPlugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ModernConfigurateConfigAdapter implements ConfigurationAdapter {
    private final LuckPermsPlugin plugin;
    private final Path path;
    private ConfigurationNode root;

    public ModernConfigurateConfigAdapter(LuckPermsPlugin plugin, Path path) {
        this.plugin = plugin;
        this.path = path;
        reload();
    }

    protected abstract ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path);

    @Override
    public void reload() {
        ConfigurationLoader<? extends ConfigurationNode> loader = createLoader(this.path);
        try {
            this.root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ConfigurationNode resolvePath(String path) {
        if (this.root == null) {
            throw new RuntimeException("Config is not loaded.");
        }

        return this.root.node(Splitter.on('.').splitToList(path).toArray());
    }

    @Override
    public String getString(String path, String def) {
        return resolvePath(path).getString(Objects.requireNonNullElse(def, ""));
    }

    @Override
    public int getInteger(String path, int def) {
        return resolvePath(path).getInt(def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return resolvePath(path).getBoolean(def);
    }

    @Override
    @SneakyThrows
    public List<String> getStringList(String path, List<String> def) {
        ConfigurationNode node = resolvePath(path);
        if (node.virtual() || !node.isList()) {
            return def;
        }

        return node.getList(String.class, def);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public Map<String, String> getStringMap(String path, Map<String, String> def) {
        ConfigurationNode node = resolvePath(path);
        if (node.virtual()) {
            return def;
        }

        if (node.childrenMap() == null) {
            return def;
        }
        return node.childrenMap().entrySet().stream().collect(Collectors.toMap(objectEntry -> objectEntry.getKey().toString(), v -> v.getValue().toString()));
    }

    @Override
    public LuckPermsPlugin getPlugin() {
        return this.plugin;
    }
}