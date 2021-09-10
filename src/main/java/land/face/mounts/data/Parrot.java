package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.ParrotTrait;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Parrot extends Mount {

    private org.bukkit.entity.Parrot.Variant parrotVariant;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        ParrotTrait parrotTrait = npc.getOrAddTrait(ParrotTrait.class);
        parrotTrait.setVariant(Objects.requireNonNullElse(parrotVariant, org.bukkit.entity.Parrot.Variant.values()[0]));
    }
}
