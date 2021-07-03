package land.face.mounts.commands;

import com.tealcube.minecraft.bukkit.TextUtils;
import com.tealcube.minecraft.bukkit.shade.acf.BaseCommand;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.*;
import land.face.mounts.data.Mount;
import land.face.mounts.managers.MountManager;
import org.bukkit.entity.Player;

@CommandAlias("mount|mounts")
public class MountsCommand extends BaseCommand {

    private final MountManager manager;

    public MountsCommand(MountManager manager) {
        this.manager = manager;
    }

    @Default
    public void onDefault(Player player) {
        if (manager.hasCooldown(player)) {
            player.sendMessage(TextUtils.color(manager.getPrefix() + manager.getCooldownMessage()));
        }
        if (!manager.canMount(player)) {
            player.sendMessage(TextUtils.color(manager.getPrefix() + manager.getInvalidLocationMessage()));
        }
        manager.showGUI(player);
    }

    @CommandPermission("EpicMounts.save")
    @Subcommand("save")
    public void onSave() {
        manager.saveMounts();
    }

    @CommandPermission("EpicMounts.load")
    @Subcommand("load")
    public void onLoad() {
        manager.loadMounts();
    }

    @CommandPermission("EpicMounts.debug")
    @CommandCompletion("@mount-ids")
    @Subcommand("debug")
    public void onDebug(Player player, String mountID) {
        Mount m = manager.getMount(mountID);
        if (m == null) {
            player.sendMessage("mount is null ya dummy");
            return;
        }
        m.spawnMount(player);
    }
}
