package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.WolfModifiers;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Wolf extends Mount {

    private boolean angry;
    private DyeColor collarColor;
    private boolean isSitting;
    private boolean isTamed;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        assert entity instanceof Wolf;
        WolfModifiers wolfModifiers = npc.getOrAddTrait(WolfModifiers.class);
        wolfModifiers.setAngry(angry);
        wolfModifiers.setSitting(isSitting);
        wolfModifiers.setTamed(isTamed);
        wolfModifiers.setCollarColor(Objects.requireNonNullElse(collarColor, DyeColor.RED));
    }
}
