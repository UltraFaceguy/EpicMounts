package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.versioned.TropicalFishTrait;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TropicalFish;

import java.util.Objects;

public class Tropical_Fish extends Mount {

    private DyeColor bodyColor;
    private TropicalFish.Pattern tropicalFishPattern;
    private DyeColor patternColor;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        TropicalFishTrait tropicalFishTrait = npc.getOrAddTrait(TropicalFishTrait.class);
        tropicalFishTrait.setBodyColor(Objects.requireNonNullElse(bodyColor, DyeColor.WHITE));
        tropicalFishTrait.setPatternColor(Objects.requireNonNullElse(patternColor, DyeColor.WHITE));
        tropicalFishTrait.setPattern(Objects.requireNonNullElse(tropicalFishPattern, TropicalFish.Pattern.values()[0]));
    }
}
