package land.face.mounts.listeners;

import land.face.mounts.managers.MountManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerExitListener implements Listener {

    private MountManager mountManager;

    public PlayerExitListener(MountManager manager) {
        this.mountManager = manager;
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        if (mountManager.hasMount(event.getPlayer())) {
            mountManager.removeMount(event.getPlayer());
        }
    }

    @EventHandler
    public void OnPlayerKicked(PlayerKickEvent event) {
        if (mountManager.hasMount(event.getPlayer())) {
            mountManager.removeMount(event.getPlayer());
        }
    }
}
