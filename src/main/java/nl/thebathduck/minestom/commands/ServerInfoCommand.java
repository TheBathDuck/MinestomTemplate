package nl.thebathduck.minestom.commands;

import net.minestom.server.command.builder.Command;
import nl.thebathduck.minestom.utils.ChatUtils;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class ServerInfoCommand extends Command {

    public ServerInfoCommand() {
        super("serverinfo");

        setDefaultExecutor(((commandSender, commandContext) -> {
            long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
            long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
            long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);

            long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
            long uptimeSeconds = TimeUnit.MILLISECONDS.toSeconds(uptime);
            long seconds = uptimeSeconds % 60;
            long minutes = uptimeSeconds / 60 % 60;
            long hours = uptimeSeconds / (60 * 60) % 24;
            long days = uptimeSeconds / (24 * 60 * 60);

            commandSender.sendMessage(ChatUtils.color("<primary>Server Uptime: <secondary>" + days + " days, " + hours + " hours, " + minutes + " minutes and " + seconds + " seconds"));
            commandSender.sendMessage(ChatUtils.color("<primary>Current Memory Usage: <secondary>" + (totalMemory - freeMemory) + "/" + totalMemory + "mb (Max: " + maxMemory + " mb)"));
        }));

    }

}
