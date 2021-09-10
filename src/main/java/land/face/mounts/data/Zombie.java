package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;

public class Zombie extends Mount {
    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
    }
}
