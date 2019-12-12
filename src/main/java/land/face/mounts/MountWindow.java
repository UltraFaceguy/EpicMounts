package land.face.mounts;

import static land.face.mounts.EpicMountsPlugin.ALL_MOUNTS;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

class MountWindow {

	private EpicMountsPlugin main;
	private Player player;
	private Inventory inv;
	private List<String> playerMounts;
	
	MountWindow(EpicMountsPlugin main, List<String> ownedMounts, Player player){
		this.main = main;
		this.playerMounts = ownedMounts;
		this.player = player;
		
		createWindow();
		showPlayer();
	}
	
	private void createWindow(){
		String name = main.convertToMColors(main.windowName);

		double rows = Math.ceil((double) playerMounts.size() / 9);
		inv = Bukkit.getServer().createInventory(null, (int)(9 * rows), name);
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
