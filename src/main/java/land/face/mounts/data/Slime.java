package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SlimeSize;
import org.bukkit.entity.Entity;

public class Slime extends Mount {

    private int size;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        SlimeSize slimeSizeTrait = npc.getOrAddTrait(SlimeSize.class);
        if (size > 0) slimeSizeTrait.setSize(size);
    }
}
