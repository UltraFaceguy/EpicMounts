package land.face.mounts;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.tealcube.minecraft.bukkit.shade.google.gson.Gson;
import com.tealcube.minecraft.bukkit.shade.google.gson.GsonBuilder;
import io.pixeloutlaw.minecraft.spigot.config.MasterConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import land.face.mounts.commands.MountsCommand;
import land.face.mounts.data.Mount;
import land.face.mounts.listeners.*;
import land.face.mounts.managers.MountManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicMountsPlugin extends JavaPlugin {

  private static EpicMountsPlugin epicMountsPlugin;
  private MountManager mountManager;

  private MasterConfiguration settings;
  private VersionedSmartYamlConfiguration configYAML;

  @Override
  public void onEnable() {
    epicMountsPlugin = this;

    List<VersionedSmartYamlConfiguration> configurations = new ArrayList<>();
    configurations.add(configYAML = defaultSettingsLoad("config.yml"));
    for (VersionedSmartYamlConfiguration config : configurations) {
      if (config.update()) {
        getLogger().info("Updating " + config.getFileName());
      }
    }
    settings = MasterConfiguration.loadFromFiles(configYAML);
    mountManager = new MountManager(this);

    File file = new File(getDataFolder().getPath() + "/mounts");
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<Mount> list = new ArrayList<>();
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }
    if (!file.exists()) {
      if (file.mkdir()) {
        System.out.println("File Created");
        List<String> lore = new ArrayList<>();
        lore.add("Caca");
        lore.add("Poopoo");
        Mount mount = new Mount("DefaultMount", "Default", Material.LEATHER_HORSE_ARMOR, lore, 20.0, 10.0, 20.0, null, "HORSE", "BROWN", "WHITE");
        list.add(mount);
        try (FileWriter writer = new FileWriter(getDataFolder().getPath() + "/mounts" + "/DefaultMount.json")) {
          gson.toJson(list, writer);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    Bukkit.getPluginManager().registerEvents(new DamageListener(mountManager), this);
    Bukkit.getPluginManager().registerEvents(new DismountListener(mountManager), this);
    Bukkit.getPluginManager().registerEvents(new InventoryListener(mountManager), this);
    Bukkit.getPluginManager().registerEvents(new PlayerExitListener(mountManager), this);
    Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);

    MountsCommand mountsCommand = new MountsCommand(mountManager);
    this.getCommand("mounts").setExecutor(mountsCommand);

    Bukkit.getServer().getLogger().info("EpicMounts has been enabled");
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
    for (Mount mount : getMountManager().getActiveMounts().values()) {
      mount.getMount().remove();
    }
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.getOpenInventory().getTitle().equals(mountManager.getWindowName())) {
        player.closeInventory();
      }
    }
    Bukkit.getServer().getLogger().info("EpicMounts has been disabled");
  }

  public static EpicMountsPlugin getInstance() {
    return epicMountsPlugin;
  }

  public MountManager getMountManager() {
    return mountManager;
  }

  private VersionedSmartYamlConfiguration defaultSettingsLoad(String name) {
    return new VersionedSmartYamlConfiguration(new File(getDataFolder(), name),
            getResource(name), VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
  }

  public MasterConfiguration getSettings() {
    return settings;
  }
}
