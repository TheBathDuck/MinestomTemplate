package nl.thebathduck.minestom;

import lombok.Getter;
import lombok.SneakyThrows;
import me.lucko.luckperms.minestom.CommandRegistry;
import me.lucko.luckperms.minestom.LuckPermsMinestom;
import net.luckperms.api.LuckPerms;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.network.ConnectionManager;
import nl.thebathduck.minestom.listeners.PlayerConfigurateListener;
import nl.thebathduck.minestom.listeners.PlayerSpawnListener;
import nl.thebathduck.minestom.player.PermissionPlayer;
import nl.thebathduck.minestom.utils.configuration.YamlConfigurationAdapterModern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Getter
public class Server {

    @Getter
    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    @Getter
    private static Server instance;

    private final InstanceContainer worldInstance;
    private final LuckPerms luckPerms;
    private final File runFolder;

    public Server() {
        instance = this;

        MinecraftServer minecraftServer = MinecraftServer.init();

        runFolder = new File(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
        File worldFolder = new File(runFolder, "world");

        luckPerms = setupLuckPerms();

        worldInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        worldInstance.setChunkLoader(new AnvilLoader(worldFolder.getPath()));
        worldInstance.setChunkSupplier(LightingChunk::new);

        if (luckPerms != null) {
            ConnectionManager connectionManager = MinecraftServer.getConnectionManager();
            connectionManager.setPlayerProvider(((connection, gameProfile) -> new PermissionPlayer(luckPerms, connection, gameProfile)));
        }

        var eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addChild(events());

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);
    }

    private EventNode<Event> events() {
        EventNode<Event> events = EventNode.all("server");
        events.addListener(new PlayerConfigurateListener());
        events.addListener(new PlayerSpawnListener());

        return events;
    }

    @SneakyThrows
    private LuckPerms setupLuckPerms() {
        File lpDirectory = new File(runFolder, "luckperms");
        if (!lpDirectory.exists()) {
            boolean dir = lpDirectory.mkdir();
        }

        try {
            return LuckPermsMinestom.builder(lpDirectory.toPath())
                    .commandRegistry(CommandRegistry.minestom())
                    .configurationAdapter(YamlConfigurationAdapterModern::new)
                    .dependencyManager(true)
                    .enable();
        } catch (Exception e) {
            logger.error("Couldn't load luckperms: " + e.getMessage());
            return null;
        }
    }

}
