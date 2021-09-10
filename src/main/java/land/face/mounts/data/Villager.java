package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.VillagerProfession;
import net.citizensnpcs.trait.versioned.VillagerTrait;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Villager extends Mount {

    private org.bukkit.entity.Villager.Profession villagerProfession;
    private org.bukkit.entity.Villager.Type villagerType;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        VillagerProfession villagerProfessionTrait = npc.getOrAddTrait(VillagerProfession.class);
        villagerProfessionTrait.setProfession(Objects.requireNonNullElse(villagerProfession, org.bukkit.entity.Villager.Profession.values()[0]));
        VillagerTrait villagerTrait = npc.getOrAddTrait(VillagerTrait.class);
        villagerTrait.setType(Objects.requireNonNullElse(villagerType, org.bukkit.entity.Villager.Type.values()[0]));
    }
}
