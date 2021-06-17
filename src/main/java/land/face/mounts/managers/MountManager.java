package land.face.mounts.managers;

import com.tealcube.minecraft.bukkit.TextUtils;
import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.data.Mount;
import land.face.mounts.utils.GsonUtils;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Data
public class MountManager {

    private final EpicMountsPlugin plugin;
    private final String path;

    private final Map<String, Mount> loadedMounts = new HashMap<>();
    private       ArrayList<UUID> mountCooldowns = new ArrayList<>();

    private final String METADATA_KEY = "EpicMounts";
    private final String windowName;
    private final Long cooldownDelay;
    private final String prefix;
    private final String mountedMessage;
    private final String invalidLocationMessage;
    private final String noMountsMessage;
    private final String cooldownMessage;
    private final String despawnMessage;

    public MountManager(EpicMountsPlugin plugin) {
        this.plugin = plugin;
        this.path = plugin.getDataFolder() + File.separator + "MountData";
        cooldownDelay = plugin.getSettings().getLong("config.Cooldown", 0);
        windowName = plugin.getSettings().getString("config.WindowName", "Epic Gamer Mounts!");
        prefix = plugin.getSettings().getString("config.language.prefix", "&7[Mounts] ");
        mountedMessage = plugin.getSettings().getString("config.language.mounted", "&eYou have mounted {mountname}!");
        invalidLocationMessage = plugin.getSettings().getString("config.language.invalid_location", "&cCould not summon mount at this location, move to a more open location!");
        noMountsMessage = plugin.getSettings().getString("config.language.no_mounts", "&cYou do not have any available mounts!");
        cooldownMessage = plugin.getSettings().getString("config.language.cooldown", "&cCooling Down!");
        despawnMessage = plugin.getSettings().getString("config.language.despawn", "&oYour mount wandered away...");
    }

    public void loadMounts() {
        loadedMounts.clear();
        try {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .map(Path::toFile)
                    .forEach(this::load);
        } catch (Exception e) {
            try {
                Files.createDirectory(Paths.get(path));
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }
        plugin.registerCommandCompletion("mount-ids", loadedMounts.keySet());
    }

    private void load(File file) {
        try {
            Mount mount = GsonUtils.getGson().fromJson(new FileReader(file), Mount.class);
            loadedMounts.put(mount.getId(), mount);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveMounts() {
        if (loadedMounts.isEmpty()) {
            Bukkit.getLogger().warning("No mount data to save!");
            return;
        }
        for (String mountID : loadedMounts.keySet()) {
            try {
                FileWriter writer = new FileWriter(String.format("%s%s%s.json", path, File.separator, mountID));
                GsonUtils.getGson().toJson(loadedMounts.get(mountID), writer);
                writer.flush();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public boolean isLoaded(String mountID) {
        return getMount(mountID) != null;
    }

    public Mount getMount(String mountID) {
        return loadedMounts.get(mountID);
    }

    public boolean hasMount(Player player) {
        if (player.getVehicle() == null) return false;
        return isMount(player.getVehicle());
    }

    public boolean isMount(Entity entity) {
        return entity.hasMetadata(METADATA_KEY);
    }

    public void removeMount(Player player) {
        removeMount(player.getVehicle());
    }
    public void removeMount(Entity entity) {
        if (entity == null || !entity.hasMetadata(METADATA_KEY)) return;
        entity.remove();
        //TODO: remove from NPC registry
    }

    public boolean hasCooldown(Player player) {
        return mountCooldowns.contains(player.getUniqueId());
    }

    public boolean canMount(Player player) {
        if (player != null) {
            for (double y = -1; y < 3; y++)
                for (double x = player.getLocation().getBlock().getRelative(BlockFace.SOUTH_WEST).getLocation().getX(); x <= player.getLocation().getBlock().getRelative(BlockFace.NORTH_EAST).getLocation().getX(); x++) {
                    for (double z = player.getLocation().getBlock().getRelative(BlockFace.NORTH_EAST).getLocation().getZ(); z <= player.getLocation().getBlock().getRelative(BlockFace.SOUTH_WEST).getLocation().getZ(); z++) {
                        Location loc = new Location(player.getWorld(), x, Math.ceil(player.getLocation().getY()) + y, z);
                        if (y == -1 && loc.getBlock().getType() == Material.AIR) {
                            loc.setY(loc.getY() - 1);
                            if (loc.getBlock().getType() == Material.AIR) {
                                return false;
                            }
                        }
                        else if (y > -1 && loc.getBlock().getType().isSolid()) {
                            return false;
                        }
                    }
                }
            return true;
        }
        return false;
    }

    public void showGUI(Player player) {
        String name = TextUtils.color(windowName);
        List<Mount> playerMounts = getAvailableMounts(player);
        double rows = Math.ceil((double) playerMounts.size() / 9);
        if (rows == 0) {
            rows = 1;
        }
        Inventory inv = Bukkit.getServer().createInventory(null, (int)(9 * rows), name);
        int slot = 0;
        for (Mount mount : playerMounts) {
            inv.setItem(slot, mount.getIcon());
            slot += 1;
        }
        player.openInventory(inv);
    }

    public void equipMount(Player player, Mount mount) {
        player.sendMessage(TextUtils.color(prefix + mountedMessage).replace("{mountname}", "mount.getName()"));
        mountCooldowns.add(player.getUniqueId());
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> mountCooldowns.remove(player.getUniqueId()), cooldownDelay);
    }
}