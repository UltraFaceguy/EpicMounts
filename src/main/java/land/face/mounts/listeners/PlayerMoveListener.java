package land.face.mounts.listeners;

import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.managers.MountManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class PlayerMoveListener implements Listener {

    private MountManager manager;

    private ArrayList<String> mountTypes;
    private ArrayList<Material> pathBlocks;

    private double pathSpeed;

    public PlayerMoveListener(EpicMountsPlugin plugin) {
        this.manager = plugin.getMountManager();
        this.mountTypes = new ArrayList<>();
        this.pathBlocks = new ArrayList<>();
        mountTypes.add("HORSE");
        mountTypes.add("DONKEY");
        mountTypes.add("MULE");
        mountTypes.add("ZOMBIE_HORSE");
        mountTypes.add("SKELETON_HORSE");
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
        Player player = event.getPlayer();
        if (!manager.hasMount(player) || player.getVehicle() == null || !mountTypes.contains(player.getVehicle().getType().toString().toUpperCase())) {
            return;
        }
        LivingEntity entity = (LivingEntity) player.getVehicle();
        Location location = entity.getLocation().add(0, -1, 0);
        if (pathBlocks.contains(location.getBlock().getType())) {
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(pathSpeed * (manager.getMount(player).getSpeed() / 100));
        }
        else {
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2 * (manager.getMount(player).getSpeed() / 100));
        }
    }
}