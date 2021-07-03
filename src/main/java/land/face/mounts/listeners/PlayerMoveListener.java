package land.face.mounts.listeners;

import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.managers.MountManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerMoveListener implements Listener {

  private final MountManager manager;
  private final ArrayList<Material> pathBlocks;

  public PlayerMoveListener(EpicMountsPlugin plugin) {
    this.manager = plugin.getMountManager();
    this.pathBlocks = new ArrayList<>();
    for (String string : plugin.getSettings().getStringList("config.Paths.Blocks")) {
      try {
        pathBlocks.add(Material.getMaterial(string.toUpperCase()));
      } catch (IllegalArgumentException e) {
        System.out.println("[Mounts] Illegal material type for path block: " + string);
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Entity vehicle = event.getPlayer().getVehicle();
    if (!(vehicle instanceof LivingEntity) || !manager.isMount(vehicle)) {
      return;
    }
    Location location = vehicle.getLocation().add(0, -1, 0);
    if (pathBlocks.contains(location.getBlock().getType())) {
      ((LivingEntity) vehicle)
          .addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5, 0, true, false));
    }
  }
}