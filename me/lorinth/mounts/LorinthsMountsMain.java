package me.lorinth.mounts;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LorinthsMountsMain extends JavaPlugin implements Listener{

	private ConsoleCommandSender console;

	public static HashMap<String, Mount> ALL_MOUNTS = new HashMap<>();
	HashMap<Player, AbstractHorse> activeHorses = new HashMap<>();
	
	
	public ArrayList<Player> mountCooldowns = new ArrayList<Player>();
	public boolean notify = true;
	
	File config;
	YamlConfiguration configYml;
	
	private File mount;
	private YamlConfiguration mountYml;

	String windowName;
	long cooldownDelay;
	
	
	
	@Override
	public void onEnable(){
		console = Bukkit.getServer().getConsoleSender();
		
		LoadFiles();
		
		printLine("Has been enabled");
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	void LoadFiles(){
		//Load config
		//Load mounts
		LoadMounts();
	}
	
	void LoadMounts(){
		mount = new File(getDataFolder(), "mounts.yml");
		if(!mount.exists()){
			
			new File(this.getDataFolder() + "").mkdir();
			
			try {
				if(mount.createNewFile()){
					try {
						PrintWriter writer = new PrintWriter(mount);
						
						writer.write( "#Developer : Lorinthios \n"
								+"Config:\n"
								+"    Notify: true #shows mount/dismount messages to players\n"
								+"    Cooldown: 20 #Cooldown, in seconds, between summons, set to 0 if you don't want a cooldown\n"
								+"    WindowName: RPG_MOUNTS\n"
								+"Mounts:\n"
								+"    'IronSteed': #Use permission LMounts.IronSteed\n"
								+"        DisplayName: '&5Iron Steed' #Display name on item and above mounts head, can take colors\n"
								+"        Item: SADDLE\n"
								+"        Lore: #This can take color codes\n"
								+"        - '&6This mount is clad in iron armor'\n"
								+"        - '&7Speed : 150%'\n"
								+"        - '&8Jump : 120%'\n"
								+"        Speed: 150 #% of player speed\n"
								+"        Jump: 0.7 #Scales between 0 and 2, 0.7 is average\n"
								+"        Health: 1 #1hp = 1 hit dead\n"
								+"        Variant: HORSE\n");
						writer.close();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(mount.getAbsolutePath());
				e.printStackTrace();
			}
		}
		mountYml = YamlConfiguration.loadConfiguration(mount);
		boolean mountYmlChanged = false;
		
		Set<String> configSettings = mountYml.getConfigurationSection("Config").getKeys(false);
		notify = mountYml.getBoolean("Config.Notify");
		cooldownDelay = mountYml.getLong("Config.Cooldown");

		if (configSettings.contains("WindowName")) {
			windowName = mountYml.getString("Config.WindowName");
		} else {
			mountYml.set("Config.WindowName", "RPG_MOUNTS");
			windowName = "RPG_MOUNTS";
			mountYmlChanged = true;
		}

		for (String name : mountYml.getConfigurationSection("Mounts").getKeys(false)){
			Material item = Material.getMaterial(mountYml.getString("Mounts." + name + ".Item"));
			String displayname = convertToMColors(mountYml.getString("Mounts." + name + ".DisplayName"));
			double speed = mountYml.getDouble("Mounts." + name + ".Speed");
			double jump = mountYml.getDouble("Mounts." + name + ".Jump");
			double hp = mountYml.getDouble("Mounts." + name + ".Health");
			List<String> lore = convertToMColors(mountYml.getStringList("Mounts." + name + ".Lore"));
			String armor = null;
			if (mountYml.contains("Mounts." + name + ".Armor")) {
				armor = mountYml.getString("Mounts." + name + ".Armor");
			}
			String variant = "HORSE";
			if (mountYml.contains("Mounts." + name + ".Variant")) {
				variant = mountYml.getString("Mounts." + name + ".Variant");
			}
			String color = "BROWN";
			if (mountYml.contains("Mounts." + name + ".Color")) {
				color = mountYml.getString("Mounts." + name + ".Color");
			}
			System.out.println("4");
			String style = "NONE";
			if (mountYml.contains("Mounts." + name + ".Style")) {
				style = mountYml.getString("Mounts." + name + ".Style");
			}

			Mount m = new Mount(
					displayname,
					item,
					lore,
					speed,
					jump,
					hp,
					armor,
					variant,
					color,
					style
			);

			ALL_MOUNTS.put(name, m);
		}
		
		if(mountYmlChanged){
			try {
				mountYml.save(mount);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
		
	@Override
	public void onDisable(){
		printErrorLine("Has been disabled");
		
		for(AbstractHorse p : activeHorses.values()){
			p.remove();
		}
		
	}
	
	void printLine(String line){
		console.sendMessage(ChatColor.GREEN + "[LorsMounts] : " + line);
	}
	
	void printErrorLine(String line){
		console.sendMessage(ChatColor.RED + "[LorsMounts] : " + line);
	}
	
	void printWarningLine(String line){
		console.sendMessage(ChatColor.YELLOW + "[LorsMounts] : " + line);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void OnHorseHit(EntityDamageEvent e){
		if(e.getEntity() instanceof AbstractHorse){
			if(activeHorses.containsValue(e.getEntity())){
				activeHorses.remove(e.getEntity().getPassenger());
				((AbstractHorse)e.getEntity()).getInventory().clear();
				e.setDamage(0);
				e.setCancelled(true);
				e.getEntity().remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void OnHorseDeath(EntityDeathEvent e){
		if(e.getEntity() instanceof AbstractHorse){
			if(activeHorses.containsValue(e.getEntity())){
				activeHorses.remove(e.getEntity().getPassenger());
				e.setDroppedExp(0);
				e.getDrops().clear();
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (commandLabel.equalsIgnoreCase("mounts")) {
				MountWindow window = new MountWindow(this, getAvailableMounts(player), player);
				window.showPlayer();
			}
		}
		return false;
	}
	
	@EventHandler
	public void OnWindowClick(InventoryClickEvent event){
		if (event.getInventory().getName().equals(convertToMColors(windowName))) {
			try {
				Player p = (Player)event.getWhoClicked();
				Mount m = ALL_MOUNTS.get(getAvailableMounts(p).get(event.getSlot()));
				if(m != null && !mountCooldowns.contains(p)){
					if (notify) {
						p.sendMessage(ChatColor.GRAY + "[Mount] " + ChatColor.YELLOW + "You have mounted " + m.getName());
					}
					activeHorses.put(p, m.spawn(p));
					p.closeInventory();
					mountCooldowns.add(p);
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
						@Override
						public void run() {
							mountCooldowns.remove(p);
						}
					}, cooldownDelay);
				}
			}
			catch(IndexOutOfBoundsException error){
				//pass
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void OnHorseInventoryOpen(InventoryOpenEvent e){
		if(activeHorses.containsKey(e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void OnHorseDismount(VehicleExitEvent event){
		if(event.getVehicle() instanceof AbstractHorse){
			Entity ent = event.getExited();
			if (ent instanceof Player) {
				AbstractHorse h = activeHorses.get(ent);
				if (h != null) {
					activeHorses.remove(ent);
					if(notify){
						ent.sendMessage(ChatColor.GRAY + "[Mount] " + ChatColor.YELLOW + "[Mount] You have dismounted!");
					}
					h.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void OnPlayerHit(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(activeHorses.containsKey(player)){
				AbstractHorse h = activeHorses.get(player);
				
				activeHorses.remove(player);
				
				if(notify){
					player.sendMessage(ChatColor.GRAY + "[Mount] " + ChatColor.YELLOW + "You have dismounted from combat!");
				}
				
				h.remove();
			}
		}
	}
	
	@EventHandler
	public void OnEntityDamage(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player){
			Player player = (Player) event.getDamager();
			if(activeHorses.containsKey(player)){
				AbstractHorse h = activeHorses.get(player);
				
				activeHorses.remove(player);
				
				if(notify){
					player.sendMessage(ChatColor.GRAY + "[Mount] : You have dismounted from combat!");
				}
				
				h.remove();
			}
		}
	}
	
	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(activeHorses.containsKey(player)){
			AbstractHorse h = activeHorses.get(player);
			activeHorses.remove(player);
			h.remove();
		}
	}
	
	@EventHandler
	public void OnPlayerKicked(PlayerKickEvent event){
		Player player = event.getPlayer();
		if(activeHorses.containsKey(player)){
			AbstractHorse h = activeHorses.get(player);
			activeHorses.remove(player);
			h.remove();
		}
	}
	
	private List<String> getAvailableMounts(Player player){
		List<String> available = new ArrayList<>();
		for(String name : ALL_MOUNTS.keySet()){
			if(player.hasPermission("LMounts." + name)){
				available.add(name);
			}
		}
		
		return available;
	}
	
	String convertToMColors(String line){
		return line.replaceAll("&", "§");
	}
	
	private List<String> convertToMColors(List<String> lines){
		List<String> newLines = new ArrayList<String>();
		for(String line : lines){
			newLines.add(convertToMColors(line));
		}
		return newLines;
	}
	
}
