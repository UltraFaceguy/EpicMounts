package land.face.mounts.menu;

import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.data.Mount;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MountSelector extends MenuItem {

    private final Mount mount;

    public MountSelector(Mount mount) {
        super(mount.getId(), new ItemStack(Material.SADDLE), "uhh", "yeah", "hard coded this");
        this.mount = mount;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
        mount.spawnMount(event.getPlayer());
        EpicMountsPlugin.getInstance().getMountManager().applyCooldown(event.getPlayer());
    }
}
