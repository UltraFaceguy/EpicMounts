package me.lorinth.mounts;

import java.util.List;

import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class Mount {

	private ItemStack displayItem;

	private String variant;

	private String id;
	private String name;
	private double speed;
	private double jump;
	private double health;

	private String armor;
	private String style;
	private String color;
	
	Mount(String id, String name, Material icon, List<String> lore, double spd, double jump, double hp, String armor, String variant, String color, String style){
		this.id = id;
		this.variant = variant;
		this.name = name;
		this.speed = spd;
		this.jump = jump;
		this.health = hp;
		this.armor = armor;
		this.color = color;
		this.style = style;
		
		ItemStack display = new ItemStack(icon);

		ItemMeta meta = display.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		display.setItemMeta(meta);

		displayItem = display;
	}
	
	String getName(){
		return name;
	}
	
	AbstractHorse spawn(Player player){
		AbstractHorse genericHorse = null;

		switch (variant.toUpperCase()) {
			case "HORSE":
				Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
				horse.setColor(stringToColor(color));
				horse.setStyle(stringToStyle(style));
				horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
				if (armor != null) {
					horse.getInventory().setArmor(new ItemStack(Material.getMaterial(armor)));
				}
				genericHorse = horse;
				break;
			case "DONKEY":
				Donkey donkey = (Donkey) player.getWorld().spawnEntity(player.getLocation(), EntityType.DONKEY);
				for (int i = 0; i < donkey.getInventory().getSize(); i++) {
					donkey.getInventory().setItem(i, new ItemStack(Material.SADDLE));
				}
				genericHorse = donkey;
				break;
			case "MULE":
				Mule mule = (Mule) player.getWorld().spawnEntity(player.getLocation(), EntityType.MULE);
				for (int i = 0; i < mule.getInventory().getSize(); i++) {
					mule.getInventory().setItem(i, new ItemStack(Material.SADDLE));
				}
				genericHorse = mule;
				break;
			case "ZOMBIE_HORSE":
				ZombieHorse zombieHorse = (ZombieHorse) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE_HORSE);
				for (int i = 0; i < zombieHorse.getInventory().getSize(); i++) {
					zombieHorse.getInventory().setItem(i, new ItemStack(Material.SADDLE));
				}
				genericHorse = zombieHorse;
				break;
			case "SKELETON_HORSE":
				SkeletonHorse skeletonHorse = (SkeletonHorse) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
				for (int i = 0; i < skeletonHorse.getInventory().getSize(); i++) {
					skeletonHorse.getInventory().setItem(i, new ItemStack(Material.SADDLE));
				}
				genericHorse = skeletonHorse;
				break;
			default:
				System.out.println("YA FUCKED UP, THIS VARIANT DOESN'T EXIST!");
		}

		if (genericHorse == null) {
			return null;
		}

		genericHorse.setCustomName(name);
		genericHorse.setTamed(true);
		genericHorse.setAdult();

		genericHorse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		genericHorse.setHealth(health);
		genericHorse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(jump);
		genericHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2 * (speed / 100));

		genericHorse.setPassenger(player);
		if (LorinthsMountsMain.cachedDisguises.containsKey(id)) {
			DisguiseAPI.disguiseToAll(genericHorse, LorinthsMountsMain.cachedDisguises.get(id));
		}
		return genericHorse;
	}
	
	ItemStack getDisplayItem(){
		return displayItem;
	}

	private Color stringToColor(String color) {
		switch (color.toUpperCase()) {
			case "BROWN":
				return Color.BROWN;
			case "BLACK":
				return Color.BLACK;
			case "WHITE":
				return Color.WHITE;
			case "GRAY":
				return Color.GRAY;
			case "CREAMY":
				return Color.CREAMY;
			case "DARK_BROWN":
				return Color.DARK_BROWN;
			default:
				return Color.CHESTNUT;
		}
	}

	private Style stringToStyle(String style) {
		switch (style.toUpperCase()) {
			case "WHITE":
				return Style.WHITE;
			case "BLACK_DOTS":
				return Style.BLACK_DOTS;
			case "WHITE_DOTS":
				return Style.WHITE_DOTS;
			case "WHITEFIELD":
				return Style.WHITEFIELD;
			default:
				return Style.NONE;
		}
	}
	
}
