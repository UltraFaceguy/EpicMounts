package land.face.mounts.menu;

import io.pixeloutlaw.minecraft.spigot.garbage.ListExtensionsKt;
import io.pixeloutlaw.minecraft.spigot.garbage.StringExtensionsKt;
import io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt;
import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.data.Mount;
import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class MountSelector extends MenuItem {

    private final Mount mount;

    public MountSelector(Mount mount) {
        super(mount.getName(), new ItemStack(Material.SADDLE), mount.getLore());
        this.mount = mount;
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        ItemStack stack = new ItemStack(Material.SADDLE);
        String displayName = mount.getName();
        List<String> lore = Arrays.asList(mount.getLore());
        ItemStackExtensionsKt.setDisplayName(stack, StringExtensionsKt.chatColorize(displayName));
        ItemStackExtensionsKt.setLore(stack, ListExtensionsKt.chatColorize(lore));
        return stack;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
        mount.spawnMount(event.getPlayer());
        EpicMountsPlugin.getInstance().getMountManager().applyCooldown(event.getPlayer());
    }
}
