package me.lorinth.mounts;

import static me.lorinth.mounts.LorinthsMountsMain.ALL_MOUNTS;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

class MountWindow {

	private LorinthsMountsMain main;
	private Player player;
	private Inventory inv;
	private List<String> playerMounts;
	
	MountWindow(LorinthsMountsMain main, List<String> ownedMounts, Player player){
		this.main = main;
		this.playerMounts = ownedMounts;
		this.player = player;
		
		createWindow();
		showPlayer();
	}
	
	private void createWindow(){
		String name = main.convertToMColors(main.windowName);

		int rows = (1 + playerMounts.size()) / 9;
		inv = Bukkit.getServer().createInventory(null, 9 * rows, name);
		int slot = 0;
		for (String mount : playerMounts) {
			inv.setItem(slot, ALL_MOUNTS.get(mount).getDisplayItem());
			slot += 1;
		}
	}
	
	void showPlayer(){
		player.openInventory(inv);
	}
	
}
