package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.PolarBearTrait;
import org.bukkit.entity.Entity;

public class Polar_Bear extends Mount {

    private boolean isRearing;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        PolarBearTrait polarBearTrait = npc.getOrAddTrait(PolarBearTrait.class);
        polarBearTrait.setRearing(isRearing);
    }
}
