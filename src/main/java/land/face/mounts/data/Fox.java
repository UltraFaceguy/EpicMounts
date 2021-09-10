package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.FoxTrait;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Fox extends Mount {

    private boolean isSitting;
    private org.bukkit.entity.Fox.Type foxType;
    private boolean isCrouching;
    private boolean isSleeping;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        FoxTrait foxTrait = npc.getOrAddTrait(FoxTrait.class);
        foxTrait.setSitting(isSitting);
        foxTrait.setCrouching(isCrouching);
        foxTrait.setSleeping(isSleeping);
        foxTrait.setType(Objects.requireNonNullElse(foxType, org.bukkit.entity.Fox.Type.values()[0]));
    }
}
