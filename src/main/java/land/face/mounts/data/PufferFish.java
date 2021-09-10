package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.PufferFishTrait;
import org.bukkit.entity.Entity;

public class PufferFish extends Mount {

    private int puffState;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        PufferFishTrait pufferFishTrait = npc.getOrAddTrait(PufferFishTrait.class);
        pufferFishTrait.setPuffState(puffState);
    }
}
