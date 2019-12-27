package land.face.mounts.commands;

import com.tealcube.minecraft.bukkit.TextUtils;
import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.managers.MountManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MountsCommand implements CommandExecutor {

    private EpicMountsPlugin plugin;
    private MountManager manager;

    public MountsCommand(EpicMountsPlugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getMountManager();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (manager.getAvailableMounts(player).isEmpty()) {
            player.sendMessage(TextUtils.color(manager.getPrefix() + manager.getNoMountsMessage()));
            return true;
        }
        if (manager.hasCooldown(player)) {
            player.sendMessage(TextUtils.color(manager.getPrefix() + manager.getCooldownMessage()));
            return true;
        }
        if (!manager.canMount(player)) {
            player.sendMessage(TextUtils.color(manager.getPrefix() + manager.getInvalidLocationMessage()));
            return true;
        }
        manager.showGUI(player);
        return true;
    }
}
