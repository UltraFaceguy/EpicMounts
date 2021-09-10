package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.MushroomCowTrait;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MushroomCow;

import java.util.Objects;

public class Mushroom_Cow extends Mount {

    private MushroomCow.Variant mooshroomType;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        MushroomCowTrait mushroomCowTrait = npc.getOrAddTrait(MushroomCowTrait.class);
        mushroomCowTrait.setVariant(Objects.requireNonNullElse(mooshroomType, MushroomCow.Variant.RED));
    }
}
