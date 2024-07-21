package nl.thebathduck.minestom.blocks.registry;

import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;

public class BlockRule extends IRegisteredRule {
    private final Block block;

    public BlockRule(Block block, Class<? extends nl.thebathduck.minestom.blocks.rule.BlockRule> ruleClass) {
        super(ruleClass);
        this.block = block;
    }

    @Override
    public boolean check(NamespaceID namespaceID) {
        return namespaceID.toString().equalsIgnoreCase(block.namespace().toString());
    }
}