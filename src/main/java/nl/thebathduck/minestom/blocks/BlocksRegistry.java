package nl.thebathduck.minestom.blocks;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import nl.thebathduck.minestom.blocks.handler.SignHandler;
import nl.thebathduck.minestom.blocks.handler.SkullHandler;
import nl.thebathduck.minestom.blocks.listeners.BlockBreakListener;
import nl.thebathduck.minestom.blocks.listeners.BlockInteractListener;
import nl.thebathduck.minestom.blocks.listeners.BlockPlaceListener;
import nl.thebathduck.minestom.blocks.listeners.packet.SignPacketListener;
import nl.thebathduck.minestom.blocks.registry.BlockRule;
import nl.thebathduck.minestom.blocks.registry.IRegisteredRule;
import nl.thebathduck.minestom.blocks.registry.NamespaceRule;
import nl.thebathduck.minestom.blocks.rules.*;
import nl.thebathduck.minestom.blocks.rules.generic.OppositeRotationalRule;
import nl.thebathduck.minestom.blocks.rules.generic.RotationalRule;

import java.util.ArrayList;
import java.util.List;

public class BlocksRegistry {

    @Getter
    private static final List<IRegisteredRule> ruleRegistry = new ArrayList<>();

    public static void register() {
        ruleRegistry.add(new BlockRule(Block.LEVER, LeverRule.class));
        ruleRegistry.add(new BlockRule(Block.ANVIL, AnvilRule.class));
        ruleRegistry.add(new BlockRule(Block.IRON_BARS, IronBarRule.class));

        ruleRegistry.add(new BlockRule(Block.LOOM, OppositeRotationalRule.class));
        ruleRegistry.add(new BlockRule(Block.FURNACE, OppositeRotationalRule.class));
        ruleRegistry.add(new BlockRule(Block.BEEHIVE, OppositeRotationalRule.class));
        ruleRegistry.add(new BlockRule(Block.BELL, RotationalRule.class));

        ruleRegistry.add(new NamespaceRule("_glazed_terracotta", OppositeRotationalRule.class));

        ruleRegistry.add(new NamespaceRule("_stairs", StairsRule.class));
        ruleRegistry.add(new NamespaceRule("_wall", WallRule.class));
        ruleRegistry.add(new NamespaceRule("slab", true, SlabRule.class));
        ruleRegistry.add(new NamespaceRule("_pane", GlassPaneRule.class));

        ruleRegistry.add(new NamespaceRule("_skull", true, SkullRule.class));
        ruleRegistry.add(new NamespaceRule("_head", true, SkullRule.class));
        ruleRegistry.add(new NamespaceRule("_anvil", AnvilRule.class));
        ruleRegistry.add(new NamespaceRule("_banner", BannerRule.class));
        ruleRegistry.add(new NamespaceRule("bed", true, BedRule.class));
        ruleRegistry.add(new NamespaceRule("_button", ButtonRule.class));

        ruleRegistry.add(new NamespaceRule("_door", DoorRule.class));
        ruleRegistry.add(new NamespaceRule("sign", true, SignRule.class));
        ruleRegistry.add(new NamespaceRule("_trapdoor", TrapdoorRule.class));

        ruleRegistry.add(new NamespaceRule("_fence_gate", RotationalRule.class));
        ruleRegistry.add(new NamespaceRule("_fence", FenceRule.class));

        BlockManager blockManager = MinecraftServer.getBlockManager();
        ruleRegistry.iterator().forEachRemaining(iRegisteredRule -> iRegisteredRule.register(blockManager));

        blockManager.registerHandler("minecraft:sign", SignHandler::new);
        blockManager.registerHandler("minecraft:skull", SkullHandler::new);
        MinecraftServer.getPacketListenerManager().setListener(ConnectionState.PLAY, ClientUpdateSignPacket.class, new SignPacketListener());

        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addListener(new BlockPlaceListener());
        eventHandler.addListener(new BlockBreakListener());
        eventHandler.addListener(new BlockInteractListener());
    }

}
