package nl.thebathduck.minestom;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import nl.thebathduck.minestom.blocks.BlocksRegistry;
import nl.thebathduck.minestom.commands.ServerInfoCommand;
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

        BlocksRegistry.register();

        registerListeners();
        registerCommands();

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);
    }

    private void registerListeners() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(new PlayerConfigurateListener());
        globalEventHandler.addListener(new PlayerSpawnListener());
    }

    private void registerCommands() {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new ServerInfoCommand());
    }

}
