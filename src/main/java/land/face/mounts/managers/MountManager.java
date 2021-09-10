package land.face.mounts.managers;

import com.tealcube.minecraft.bukkit.TextUtils;
import com.tealcube.minecraft.bukkit.shade.apache.commons.lang.ClassUtils;
import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.data.Mount;
import land.face.mounts.gson.GsonUtils;
import land.face.mounts.menu.MountSelectorMenu;
import lombok.Data;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class MountManager {

    private final EpicMountsPlugin plugin;
    private final NPCRegistry NPCRegistry;
    private final String path;

    private final Map<String, Mount> loadedMounts = new HashMap<>();
    private       ArrayList<UUID> mountCooldowns = new ArrayList<>();

    private final String METADATA_KEY = "EpicMounts";
    private final String defaultPermission;
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
        defaultPermission = plugin.getSettings().getString("config.permission.default_mount", "EpicMount.DefaultMount");
        cooldownDelay = plugin.getSettings().getLong("config.Cooldown", 0);
        windowName = plugin.getSettings().getString("config.WindowName", "Epic Gamer Mounts!");
        prefix = plugin.getSettings().getString("config.language.prefix", "&7[Mounts] ");
        mountedMessage = plugin.getSettings().getString("config.language.mounted", "&eYou have mounted {mountname}!");
        invalidLocationMessage = plugin.getSettings().getString("config.language.invalid_location", "&cCould not summon mount at this location, move to a more open location!");
        noMountsMessage = plugin.getSettings().getString("config.language.no_mounts", "&cYou do not have any available mounts!");
        cooldownMessage = plugin.getSettings().getString("config.language.cooldown", "&cCooling Down!");
        despawnMessage = plugin.getSettings().getString("config.language.despawn", "&oYour mount wandered away...");
        NPCRegistry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
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
            String id = file.getName().replace(".json", "");
            mount.setId(id);
            if (mount.getPermission() == null || mount.getPermission().equalsIgnoreCase("")) {
                mount.setPermission(defaultPermission);
            }
            if (mount.getName() == null || mount.getName().equalsIgnoreCase("")) mount.setName(id);
            if (mount.getLore() == null) mount.setLore(new String[0]);
            loadedMounts.put(id, mount);
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

        loadedMounts.values().forEach(this::sanitizeGenericData);

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
    private void sanitizeGenericData(Mount mount) {
        List<?> entityInterfaces = ClassUtils.getAllInterfaces(EntityType.valueOf(mount.getType()).getEntityClass());
        if (entityInterfaces == null) return;
        if (!entityInterfaces.contains(Ageable.class)) mount.setBaby(null);
        if (!entityInterfaces.contains(AbstractHorse.class)) mount.setJumpStrength(null);
    }

    public boolean isLoaded(String mountID) {
        return getMount(mountID) != null;
    }

    public Mount getMount(Entity entity) {
        return getMount(getMountID(entity));
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

    public void removeIfMount(Entity entity) {
        if (isMount(entity)) removeMount(entity);
    }

    public void removeMount(Player player) {
        removeMount(player.getVehicle());
    }
    public void removeMount(Entity entity) {
        if (entity == null || !isMount(entity)) return;
        NPC npc = NPCRegistry.getNPC(entity);
        npc.destroy();
        NPCRegistry.deregister(npc);
    }

    public void removeAllMounts() {
        NPCRegistry.despawnNPCs(DespawnReason.PLUGIN);
        NPCRegistry.deregisterAll();
    }

    public void applyMountMetadata(Entity entity) {
        applyMountMetadata(entity, null);
    }
    public void applyMountMetadata(Entity entity, String ID) {
        entity.setMetadata(METADATA_KEY,new FixedMetadataValue(plugin, ID));
    }

    public void applyCooldown(Player player) {
        mountCooldowns.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(plugin, () -> mountCooldowns.remove(player.getUniqueId()), cooldownDelay);
    }

    public boolean hasCooldown(Player player) {
        return mountCooldowns.contains(player.getUniqueId());
    }

    public String getMountID(Entity entity) {
        if (!isMount(entity)) return null;
        for (MetadataValue metadata : entity.getMetadata(METADATA_KEY)) {
            if (metadata.getOwningPlugin() != plugin) break;
            return metadata.asString();
        }
        return null;
    }

    public NPC getAsNPC(Entity entity) {
        return NPCRegistry.getNPC(entity);
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

    public List<Mount> getAvailableMounts(Player player) {
        return loadedMounts.values()
                .stream()
                .filter(mount -> player.hasPermission(mount.getPermission()))
                .collect(Collectors.toList());
    }

    public void showGUI(Player player) {
        List<Mount> mounts = getAvailableMounts(player);
        if (mounts.isEmpty()) player.sendMessage(TextUtils.color(noMountsMessage));
        else new MountSelectorMenu(mounts).open(player);
    }

    public void equipMount(Player player, Mount mount) {
        player.sendMessage(TextUtils.color(prefix + mountedMessage).replace("{mountname}", "mount.getName()"));
        mountCooldowns.add(player.getUniqueId());
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> mountCooldowns.remove(player.getUniqueId()), cooldownDelay);
    }
}