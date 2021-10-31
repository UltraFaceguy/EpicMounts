package land.face.mounts.listeners;

import land.face.mounts.managers.MountManager;
import org.bukkit.entity.Strider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.StriderTemperatureChangeEvent;

public class MountBehaviorListeners implements Listener {

  private final MountManager mountManager;

  public MountBehaviorListeners(MountManager manager) {
    this.mountManager = manager;
  }

  @EventHandler
  public void onStriderTemperatureChangeEvent(
      StriderTemperatureChangeEvent event) { //broken till spigot or paper blesses us
    Strider strider = event.getEntity();
    if (!mountManager.isMount(strider)) {
      return;
    }
    event.setCancelled(true);
  }
}
