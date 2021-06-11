package land.face.mounts.data;

import lombok.Data;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@Data
public class Mount {

    private Player mountOwner;

    private String id;
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
