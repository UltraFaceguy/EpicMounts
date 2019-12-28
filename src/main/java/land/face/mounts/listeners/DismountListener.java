package land.face.mounts.listeners;

import land.face.mounts.managers.MountManager;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class DismountListener implements Listener {

    private MountManager mountManager;

    public DismountListener(MountManager manager) {
        this.mountManager = manager;
    }

    @EventHandler
    public void OnHorseDismount(VehicleExitEvent event) {
        if (event.getVehicle() instanceof AbstractHorse) {
            Entity entity = event.getExited();
            if (entity instanceof Player) {
                Player player = (Player) entity;
                if (mountManager.hasMount(player)) {
                    mountManager.removeMount(player);
                }
            }
        }
    }
}
