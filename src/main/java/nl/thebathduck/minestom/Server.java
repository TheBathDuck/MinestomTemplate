package nl.thebathduck.minestom;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import nl.thebathduck.minestom.listeners.PlayerConfigurateListener;
import nl.thebathduck.minestom.listeners.PlayerSpawnListener;
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

    public Server() {
        instance = this;

        MinecraftServer minecraftServer = MinecraftServer.init();

        File runFolder = new File(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
        File worldFolder = new File(runFolder, "world");

        worldInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        worldInstance.setChunkLoader(new AnvilLoader(worldFolder.getPath()));
        worldInstance.setChunkSupplier(LightingChunk::new);

        var eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addChild(events());

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);
        logger.info("woah");
        logger.warn("warn");
        logger.error("err");
    }

    private EventNode<Event> events() {
        EventNode<Event> events = EventNode.all("server");
        events.addListener(new PlayerConfigurateListener());
        events.addListener(new PlayerSpawnListener());

        return events;
    }


}
