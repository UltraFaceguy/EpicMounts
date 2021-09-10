package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.CatTrait;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Cat extends Mount {

    private DyeColor collarColor;
    private boolean isLaying;
    private boolean isSitting;
    private org.bukkit.entity.Cat.Type catType;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        CatTrait catTrait = npc.getOrAddTrait(CatTrait.class);
        catTrait.setCollarColor(collarColor);
        catTrait.setLyingDown(isLaying);
        catTrait.setSitting(isSitting);
        catTrait.setType(Objects.requireNonNullElse(catType, org.bukkit.entity.Cat.Type.values()[0]));
    }
}
