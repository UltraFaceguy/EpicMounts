package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.EndermanTrait;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.material.MaterialData;

public class Enderman extends Mount {

    private boolean angry;
    private Material carriedBlockType; //TODO: SWAP TO ITEMSTACK (PENDING GSON ADAPTER)

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        assert entity instanceof Enderman;
        EndermanTrait endermanTrait = npc.getOrAddTrait(EndermanTrait.class);
        if (angry && !endermanTrait.isAngry()) endermanTrait.toggleAngry();
        if (carriedBlockType != null) ((org.bukkit.entity.Enderman) entity).setCarriedMaterial(new MaterialData(carriedBlockType));
    }
}
