package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.PandaTrait;
import org.bukkit.entity.Entity;

public class Panda extends Mount {

    private boolean isSitting;
    private org.bukkit.entity.Panda.Gene mainPandaGene;
    private org.bukkit.entity.Panda.Gene hiddenPandaGene;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        PandaTrait pandaTrait = npc.getOrAddTrait(PandaTrait.class);
        pandaTrait.setSitting(isSitting);
        if (mainPandaGene != null) pandaTrait.setMainGene(mainPandaGene);
        if (hiddenPandaGene != null) pandaTrait.setHiddenGene(hiddenPandaGene);
    }
}
