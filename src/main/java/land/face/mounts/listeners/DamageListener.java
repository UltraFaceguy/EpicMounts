package land.face.mounts.listeners;

import land.face.mounts.managers.MountManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DamageListener implements Listener {

    private final MountManager mountManager;

    public DamageListener(MountManager manager) {
        this.mountManager = manager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        mountManager.removeMount(event.getEntity());
    }

    @EventHandler
    public void OnEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            mountManager.removeMount((Player) event.getDamager());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnHorseDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (mountManager.isMount(entity)) {
            event.setDroppedExp(0);
            event.getDrops().clear();
            mountManager.removeMount(entity);
        }
    }
}
