package nl.thebathduck.minestom.blocks.registry;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.utils.NamespaceID;
import nl.thebathduck.minestom.Server;
import nl.thebathduck.minestom.blocks.rule.BlockRule;

public abstract class IRegisteredRule {
    private final Class<? extends BlockRule> ruleClass;

    public IRegisteredRule(Class<? extends BlockRule> ruleClass) {
        this.ruleClass = ruleClass;
    }

    public abstract boolean check(NamespaceID namespaceID);

    public void register(BlockManager blockManager) {
        for (Block registeredBlock : Block.values()) {
            if (!check(registeredBlock.namespace())) continue;
            if (blockManager.getBlockPlacementRule(registeredBlock) != null) continue;

            try {
                BlockRule rule = ruleClass.getDeclaredConstructor(Block.class).newInstance(registeredBlock);
                blockManager.registerBlockPlacementRule(rule);

                Server.getLogger().info("Registered block placement rule for " + registeredBlock.namespace());
            } catch (Exception e) {
                Server.getLogger().warn("Failed to register block placement rule for " + registeredBlock.namespace());
            }
        }
    }
}