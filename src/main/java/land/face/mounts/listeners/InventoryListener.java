package land.face.mounts.listeners;

import com.tealcube.minecraft.bukkit.TextUtils;
import land.face.mounts.data.Mount;
import land.face.mounts.managers.MountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryListener implements Listener {

    private MountManager mountManager;

    public InventoryListener(MountManager manager) {
        this.mountManager = manager;
    }

    @EventHandler
    public void OnWindowClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(TextUtils.color(mountManager.getWindowName()))) {
            try {
                Player player = (Player) event.getWhoClicked();
                Mount mount = mountManager.getAvailableMounts(player).get(event.getSlot());
                if (mount != null) {
                    player.closeInventory();
                    mount.setMountOwner(player);
                    mountManager.equipMount(player, mount);
                }
            } catch (IndexOutOfBoundsException error) {
                //pass
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnHorseInventoryOpen(InventoryOpenEvent event) {
        if (mountManager.hasMount((Player) event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
