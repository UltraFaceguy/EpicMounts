package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.LlamaTrait;
import org.bukkit.entity.Entity;

public class Llama extends Mount {

    private org.bukkit.entity.Llama.Color llamaColor;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        super.applyAttributes(entity);
        LlamaTrait llamaTrait = npc.getOrAddTrait(LlamaTrait.class);
        llamaTrait.setColor(llamaColor);
    }
}
