package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.ShulkerTrait;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Shulker extends Mount {

    private int shulkerPeek;
    private DyeColor color;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        ShulkerTrait shulkerTrait = npc.getOrAddTrait(ShulkerTrait.class);
        shulkerTrait.setPeek(shulkerPeek);
        shulkerTrait.setColor(Objects.requireNonNullElse(color, DyeColor.WHITE));
    }
}
