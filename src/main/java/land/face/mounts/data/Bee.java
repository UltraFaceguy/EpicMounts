package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;

public class Bee extends Mount {

    private boolean angry;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        assert entity instanceof Bee;
        org.bukkit.entity.Bee bee = (org.bukkit.entity.Bee) entity;
        bee.setAnger(Integer.MAX_VALUE);
    }
}
