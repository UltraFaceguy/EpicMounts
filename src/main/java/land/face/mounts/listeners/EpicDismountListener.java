package land.face.mounts.listeners;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EpicDismountListener implements Listener {

    @EventHandler
    public void onEpicDismount(EntityDismountEvent event) {
        if (event.getDismounted().hasMetadata("EpicMount")) {
            CitizensAPI.getNPCRegistry().getNPC(event.getDismounted()).destroy();
        }
    }
}
