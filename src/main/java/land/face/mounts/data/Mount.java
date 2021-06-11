package land.face.mounts.data;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Mount {

    private Player mountOwner;

    private EntityType entityType;

    public Mount(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setOwner(Player player) {
        this.mountOwner = mountOwner;
    }

    public void spawnMount() {
        Entity mount = mountOwner.getWorld().spawnEntity(mountOwner.getLocation(), entityType);
        mount.addPassenger((Entity) mountOwner);
    }
}
