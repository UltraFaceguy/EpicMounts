package land.face.mounts.listeners;

import land.face.mounts.managers.MountManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EpicDismountListener implements Listener {

    private final MountManager mountManager;

    public EpicDismountListener(MountManager mountManager) {
        this.mountManager = mountManager;
    }

    @EventHandler
    public void onEpicDismount(EntityDismountEvent event) {
        mountManager.removeIfMount(event.getDismounted());
    }
}
