package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;

public class Strider extends Mount {

    private boolean shouldShiver;
    private boolean hasSaddle;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        assert entity instanceof Strider;
        ((org.bukkit.entity.Strider) entity).setShivering(shouldShiver);
        ((org.bukkit.entity.Strider) entity).setSaddle(hasSaddle);
    }

    public boolean isShouldShiver() {
        return shouldShiver;
    }
}
