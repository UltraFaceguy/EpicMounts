package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.SnowmanTrait;
import org.bukkit.entity.Entity;

public class Snowman extends Mount {

    private boolean derp;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        SnowmanTrait snowmanTrait = npc.getOrAddTrait(SnowmanTrait.class);
        snowmanTrait.setDerp(derp);
    }
}
