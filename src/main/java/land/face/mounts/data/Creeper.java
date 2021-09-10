package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;

public class Creeper extends Mount {

    private boolean isPowered;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        assert entity instanceof Creeper;
        ((org.bukkit.entity.Creeper) entity).setPowered(isPowered);
    }
}
