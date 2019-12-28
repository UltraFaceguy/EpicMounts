package land.face.mounts.listeners;

import land.face.mounts.managers.MountManager;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DamageListener implements Listener {

    private MountManager manager;

    public DamageListener(MountManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (manager.hasMount(player)) {
                manager.removeMount(player);
            }
        }
        if (event.getEntity() instanceof AbstractHorse) {
            if (manager.getMount(event.getEntity()) != null) {
                manager.getMount(event.getEntity()).remove();
            }
        }
    }

    @EventHandler
    public void OnEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (manager.hasMount(player)) {
                manager.removeMount(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnHorseDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof AbstractHorse) {
            Entity entity = event.getEntity();
            if (manager.getMount(entity) != null) {
                event.setDroppedExp(0);
                event.getDrops().clear();
                manager.getMount(entity).remove();
            }
        }
    }
}
