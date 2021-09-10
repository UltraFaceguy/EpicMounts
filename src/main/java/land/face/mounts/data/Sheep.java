package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SheepTrait;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Sheep extends Mount {

    private DyeColor color;
    private boolean isSheared;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        SheepTrait sheepTrait = npc.getOrAddTrait(SheepTrait.class);
        sheepTrait.setSheared(isSheared);
        sheepTrait.setColor(Objects.requireNonNullElse(color, DyeColor.WHITE));
    }
}
