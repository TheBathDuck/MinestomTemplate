package nl.thebathduck.minestom.blocks.registry;

import net.minestom.server.utils.NamespaceID;
import nl.thebathduck.minestom.blocks.rule.BlockRule;

public class NamespaceRule extends IRegisteredRule {
    private final String namespace;
    private final boolean contains;

    public NamespaceRule(String namespace, boolean contains, Class<? extends BlockRule> ruleClass) {
        super(ruleClass);
        this.namespace = namespace;
        this.contains = contains;
    }

    public NamespaceRule(String namespace, Class<? extends BlockRule> ruleClass) {
        this(namespace, false, ruleClass);
    }

    @Override
    public boolean check(NamespaceID namespaceID) {
        if (!contains) {
            return namespaceID.toString().endsWith(namespace);
        } else {
            return namespaceID.toString().contains(namespace);
        }
    }
}