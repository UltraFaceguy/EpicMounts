package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.PhantomTrait;
import org.bukkit.entity.Entity;

public class Phantom extends Mount {

    private int size;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        PhantomTrait phantomTrait = npc.getOrAddTrait(PhantomTrait.class);
        if (size > 0) phantomTrait.setSize(size);
    }
}
