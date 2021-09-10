package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.ArmorStandTrait;
import org.bukkit.entity.Entity;

public class Armor_Stand extends Mount {

    private boolean hasArms;
    private boolean hasBasePlate;
    private boolean isMarker;
    private boolean isSmall;
    private boolean isVisible;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        ArmorStandTrait armorStandTrait = npc.getOrAddTrait(ArmorStandTrait.class);
        armorStandTrait.setHasArms(hasArms);
        armorStandTrait.setHasBaseplate(hasBasePlate);
        armorStandTrait.setMarker(isMarker);
        armorStandTrait.setSmall(isSmall);
        armorStandTrait.setVisible(isVisible);
    }
}
