package land.face.mounts.data;

import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.managers.MountManager;
import lombok.Data;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Controllable;
import net.citizensnpcs.trait.Gravity;
import net.citizensnpcs.trait.MountTrait;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;

import java.util.Objects;

@Data
public class Mount {

    //Useful reference for Mount children
    protected transient MountManager mountManager = EpicMountsPlugin.getInstance().getMountManager();

    private String type = getClass().getSimpleName().toUpperCase();

    private transient String id;

    private String permission;

    private String name;
    private String[] lore;

    private boolean flyable;
    private boolean hasGravity;
    private Double baseSpeed = -1D;
    private Double speedModifier = -1D;
    private Double baseHP = -1D;

    private Boolean baby = false;
    private Double jumpStrength = -1D;

    public void spawnMount(Player player) {
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type);
        } catch (Exception e) {
            System.out.println("Failed to spawn mount " + id + " due to null entity type");
            return;
        }

        MountManager manager = EpicMountsPlugin.getInstance().getMountManager();
        NPC npc = manager.getNPCRegistry().createNPC(entityType, id);
        npc.spawn(player.getLocation(), SpawnReason.PLUGIN);
        Entity entity = npc.getEntity();

        //Forced Attributes
        entity.setCustomNameVisible(false);
        manager.applyMountMetadata(entity, id);
        npc.getOrAddTrait(MountTrait.class);
        npc.getOrAddTrait(Controllable.class).setEnabled(true);

        //Generic Attributes
        npc.setFlyable(flyable);
        npc.getOrAddTrait(Gravity.class).setEnabled(hasGravity);
        npc.getNavigator().getDefaultParameters().baseSpeed(baseSpeed.floatValue());
        npc.getNavigator().getDefaultParameters().speedModifier(speedModifier.floatValue());
        if (baseHP > 0 && entity instanceof Attributable && entity instanceof Damageable) {
            Objects.requireNonNull(((Attributable) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(baseHP);
            ((Damageable) entity).setHealth(baseHP);
        }
        if (entity instanceof Ageable) {
            if (baby != null && baby) ((Ageable) entity).setBaby();
            else ((Ageable) entity).setAdult();
        }
        if (entity instanceof AbstractHorse) {
            ((AbstractHorse) entity).setJumpStrength(jumpStrength);
        }

        entity.addPassenger(player);
        applyAttributes(entity);
        //new PathSpeedTask(entity);
    }

    public void applyAttributes(Entity entity) {

    }
}
