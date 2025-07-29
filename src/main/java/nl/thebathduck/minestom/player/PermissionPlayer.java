package nl.thebathduck.minestom.player;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.platform.PlayerAdapter;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.util.Tristate;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class PermissionPlayer extends Player {

    private final @NotNull LuckPerms luckPerms;
    private final @NotNull PlayerAdapter<Player> playerAdapter;

    public PermissionPlayer(@NotNull LuckPerms luckPerms, @NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        this.luckPerms = luckPerms;
        this.playerAdapter = this.luckPerms.getPlayerAdapter(Player.class);
    }

    private @NotNull User getLuckPermsUser() {
        return this.playerAdapter.getUser(this);
    }

    public void addPermission(@NotNull String permission) {
        User user = this.getLuckPermsUser();
        user.data().add(Node.builder(permission).build());
        this.luckPerms.getUserManager().saveUser(user);
    }

    public @NotNull DataMutateResult setPermission(@NotNull Node permission, boolean value) {
        User user = this.getLuckPermsUser();
        DataMutateResult result = value
                ? user.data().add(permission)
                : user.data().remove(permission);
        this.luckPerms.getUserManager().saveUser(user);
        return result;
    }

    public void removePermission(@NotNull String permissionName) {
        User user = this.getLuckPermsUser();
        user.data().remove(Node.builder(permissionName).build());
        this.luckPerms.getUserManager().saveUser(user);
    }

    public boolean hasPermission(@NotNull String permissionName) {
        User user = this.getLuckPermsUser();
        return user.getCachedData().getPermissionData().checkPermission(permissionName).asBoolean();
    }

    public @NotNull Tristate getPermissionValue(@NotNull String permissionName) {
        User user = this.getLuckPermsUser();
        return user.getCachedData().getPermissionData().checkPermission(permissionName);
    }

    public int getWeight() {
        User user = this.getLuckPermsUser();
        return user.getCachedData().getMetaData().getWeight();
    }

    public Group getHighestGroup() {
        User user = this.getLuckPermsUser();
        QueryOptions queryOptions = user.getQueryOptions();
        return user.getInheritedGroups(queryOptions)
                .stream()
                .max(Comparator.comparingInt(group -> group.getWeight().orElse(0)))
                .orElse(null);

    }

}
