package land.face.mounts.listeners;

import com.tealcube.minecraft.bukkit.TextUtils;
import land.face.mounts.managers.MountManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Arrays;
import java.util.List;

public class ChunkUnloadListener implements Listener {

    private MountManager manager;

    public ChunkUnloadListener(MountManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onChunkUnload (ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        List<Entity> entities = Arrays.asList(chunk.getEntities());
        for (Entity entity : entities) {
            if (manager.isMount(entity)) {
                manager.getMount(entity).getMountOwner().sendMessage(TextUtils.color(manager.getPrefix() + manager.getDespawnMessage()));
                manager.removeMount(entity);
            }
        }
    }
}
