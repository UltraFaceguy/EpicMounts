package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.HorseModifiers;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Horse extends Mount {

    private boolean hasSaddle;
    //private ItemStack horseArmour; TODO: PENDING ITEMSTACK GSON ADAPATER
    private boolean isCarryingChest;
    private org.bukkit.entity.Horse.Color horseColor;
    private org.bukkit.entity.Horse.Style horseStyle;


    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        HorseModifiers horseModifiers = npc.getOrAddTrait(HorseModifiers.class);
        horseModifiers.setCarryingChest(isCarryingChest);
        horseModifiers.setColor(Objects.requireNonNullElse(horseColor, org.bukkit.entity.Horse.Color.values()[0]));
        horseModifiers.setStyle(Objects.requireNonNullElse(horseStyle, org.bukkit.entity.Horse.Style.values()[0]));
        if (hasSaddle) horseModifiers.setSaddle(new ItemStack(Material.SADDLE));
    }
}
