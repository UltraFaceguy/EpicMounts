package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.WitherTrait;
import org.bukkit.entity.Entity;

public class Wither extends Mount {

    private boolean isPowered;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        WitherTrait witherTrait = npc.getOrAddTrait(WitherTrait.class);
        witherTrait.setCharged(isPowered);
    }
}
