package land.face.mounts.menu;

import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.data.Mount;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Collection;

public class MountSelectorMenu extends ItemMenu {

    private static final EpicMountsPlugin plugin = EpicMountsPlugin.getInstance();

    public MountSelectorMenu(Collection<Mount> mounts) {
        super(plugin.getMountManager().getWindowName(), Size.fit(mounts.size()), plugin);
        int slot = 0;
        for (Mount mount : mounts) {
            setItem(slot, new MountSelector(mount));
            slot++;
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);
    }
}
