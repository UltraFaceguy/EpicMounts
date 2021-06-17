package land.face.mounts.listeners;

import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.managers.MountManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class PlayerMoveListener implements Listener {

    private MountManager manager;
    private ArrayList<Material> pathBlocks;
    private double pathSpeed;

    public PlayerMoveListener(EpicMountsPlugin plugin) {
        this.manager = plugin.getMountManager();
        this.pathBlocks = new ArrayList<>();
        this.pathSpeed = plugin.getSettings().getDouble("config.Paths.Speed");
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
        if (vehicle == null || !manager.isMount(vehicle)) return;
        Location location = vehicle.getLocation().add(0, -1, 0);

        //TODO: UPDATE MOUNT SPEED
        if (pathBlocks.contains(location.getBlock().getType())) {
            //entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(pathSpeed * (manager.getMount(player).getSpeed() / 100));
        }
        else {
            //entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2 * (manager.getMount(player).getSpeed() / 100));
        }
    }
}