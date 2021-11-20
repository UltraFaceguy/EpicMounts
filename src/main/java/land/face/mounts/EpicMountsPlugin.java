package land.face.mounts;

import com.tealcube.minecraft.bukkit.shade.acf.BukkitCommandManager;
import com.tealcube.minecraft.bukkit.shade.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import io.pixeloutlaw.minecraft.spigot.config.MasterConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import land.face.mounts.commands.MountsCommand;
import land.face.mounts.data.Mount;
import land.face.mounts.gson.GsonUtils;
import land.face.mounts.listeners.*;
import land.face.mounts.managers.MountManager;
import land.face.mounts.util.Loader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class EpicMountsPlugin extends JavaPlugin {

  private static EpicMountsPlugin epicMountsPlugin;
  private BukkitCommandManager commandManager;

  private MountManager mountManager;

  private MasterConfiguration settings;
  private VersionedSmartYamlConfiguration configYAML;

  private Set<Class<? extends Mount>> mountClassCache = new HashSet<>();

  @Override
  public void onEnable() {
    epicMountsPlugin = this;
    commandManager = new BukkitCommandManager(this);

    List<VersionedSmartYamlConfiguration> configurations = new ArrayList<>();
    configurations.add(configYAML = defaultSettingsLoad("config.yml"));
    for (VersionedSmartYamlConfiguration config : configurations) {
      if (config.update()) {
        getLogger().info("Updating " + config.getFileName());
      }
    }
    settings = MasterConfiguration.loadFromFiles(configYAML);

    cacheMountClasses();
    registerSubTypes();

    mountManager = new MountManager(this);
    mountManager.loadMounts();

    Bukkit.getPluginManager().registerEvents(new DamageListener(mountManager), this);
    Bukkit.getPluginManager().registerEvents(new PlayerExitListener(mountManager), this);
    Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
    Bukkit.getPluginManager().registerEvents(new ChunkUnloadListener(mountManager), this);
    Bukkit.getPluginManager().registerEvents(new EpicDismountListener(mountManager),this);
    Bukkit.getPluginManager().registerEvents(new MountBehaviorListeners(mountManager), this);

    commandManager.registerCommand(new MountsCommand(mountManager));

    Bukkit.getServer().getLogger().info("EpicMounts has been enabled");
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.getOpenInventory().getTitle().equals(mountManager.getWindowName())) {
        player.closeInventory();
      }
    }
    //mountManager.saveMounts(); Only save mount files if explicitly called via commands
    mountManager.removeAllMounts();
    Bukkit.getServer().getLogger().info("EpicMounts has been disabled");
  }

  public static EpicMountsPlugin getInstance() {
    return epicMountsPlugin;
  }

  public void registerCommandCompletion(String id, Collection<String> completions) {
    commandManager.getCommandCompletions().registerAsyncCompletion(id, c -> completions);
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

  public Set<Class<? extends Mount>> getMountClassCache() {
    return mountClassCache;
  }

  private void cacheMountClasses() {
    Set<Class<? extends Mount>> set = new HashSet<>();
    for (final Class<? extends Mount> clazz : new Loader(this.getClassLoader()).find(Mount.class)) {
      try {
        set.add(clazz);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    this.mountClassCache = set;
  }

  private void registerSubTypes() {
    RuntimeTypeAdapterFactory<Mount> adapter = RuntimeTypeAdapterFactory.of(Mount.class, "type");
    mountClassCache.forEach(clazz -> adapter.registerSubtype(clazz, clazz.getSimpleName().toUpperCase()));
    GsonUtils.registerTypeAdapter(adapter);
  }
}
