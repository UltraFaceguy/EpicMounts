package land.face.mounts.managers;

import com.tealcube.minecraft.bukkit.TextUtils;
import com.tealcube.minecraft.bukkit.shade.google.gson.GsonBuilder;
import com.tealcube.minecraft.bukkit.shade.google.gson.reflect.TypeToken;
import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.data.Mount;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class MountManager {

    private EpicMountsPlugin plugin;
    
    private HashMap<UUID, Mount> activeMounts = new HashMap<>();
    private ArrayList<UUID> mountCooldowns = new ArrayList<>();

    private String windowName;
    private Long cooldownDelay;
    private String prefix;
    private String mounted;
    private String invalidLocation;
    private String noMounts;
    private String cooldown;
    private String despawn;

    public MountManager(EpicMountsPlugin plugin) {
        this.plugin = plugin;
        cooldownDelay = plugin.getSettings().getLong("config.Cooldown", 0);
        windowName = plugin.getSettings().getString("config.WindowName", "Epic Gamer Mounts!");
        prefix = plugin.getSettings().getString("config.language.prefix", "&7[Mounts] ");
        mounted = plugin.getSettings().getString("config.language.mounted", "&eYou have mounted {mountname}!");
        invalidLocation = plugin.getSettings().getString("config.language.invalid_location", "&cCould not summon mount at this location, move to a more open location!");
        noMounts = plugin.getSettings().getString("config.language.no_mounts", "&cYou do not have any available mounts!");
        cooldown = plugin.getSettings().getString("config.language.cooldown", "&cCooling Down!");
        despawn = plugin.getSettings().getString("config.language.despawn", "&oYour mount wandered away...");
    }

    public String getWindowName() {
        return windowName;
    }

    public Long getCooldownDelay() {
        return cooldownDelay;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMountedMessage() {
        return mounted;
    }

    public String getInvalidLocationMessage() {
        return invalidLocation;
    }

    public String getNoMountsMessage() {
        return noMounts;
    }

    public String getCooldownMessage() {
        return cooldown;
    }

    public String getDespawnMessage() {
        return despawn;
    }

    public void setMount(Player player, Mount mount) {
        activeMounts.put(player.getUniqueId(), mount);
    }

    public boolean hasMount(Player player) {
        return activeMounts.containsKey(player.getUniqueId());
    }

    public Mount getMount(Player player) {
        return activeMounts.get(player.getUniqueId());
    }

    public Mount getMount(Entity entity) {
        for (UUID uuid : activeMounts.keySet()) {
            if (activeMounts.get(uuid).getMount() == entity) {
                return activeMounts.get(uuid);
            }
        }
        return null;
    }

    public void removeMount(Player player) {
        activeMounts.get(player.getUniqueId()).getMount().remove();
        activeMounts.remove(player.getUniqueId());
    }
    
    public List getAllMounts() {
        List<Mount> list = null;
        try {
            list = new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(plugin.getDataFolder().getPath() + "/mounts" + "/DefaultMount.json"), new TypeToken<List<Mount>>() {}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Mount> getAvailableMounts(Player player) {
        ArrayList<Mount> available = new ArrayList<>();
        try {
            List<Mount> list;
            list = new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(plugin.getDataFolder().getPath() + "/mounts" + "/DefaultMount.json"), new TypeToken<List<Mount>>() {}.getType());
            for (Mount mount : list) {
                if (player.hasPermission("EpicMounts." + mount.getId())) {
                    available.add(mount);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return available;
    }

    public Map<UUID, Mount> getActiveMounts() {
        return activeMounts;
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
            inv.setItem(slot, mount.getDisplayItem());
            slot += 1;
        }
        player.openInventory(inv);
    }

    public void equipMount(Player player, Mount mount) {
        player.sendMessage(TextUtils.color(prefix + mounted).replace("{mountname}", mount.getName()));
        mount.spawn(player);
        setMount(player, mount);
        mountCooldowns.add(player.getUniqueId());
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                mountCooldowns.remove(player.getUniqueId());
            }
        }, cooldownDelay);
    }

    public boolean isMount(Entity entity) {
        return getMount(entity) != null;
    }
}