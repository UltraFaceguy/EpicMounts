package land.face.mounts.data;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Rabbit extends Mount {

    private org.bukkit.entity.Rabbit.Type rabbitType;

    @Override
    public void applyAttributes(Entity entity) {
        NPC npc = mountManager.getAsNPC(entity);
        if (npc == null) return;
        assert entity instanceof Rabbit;
        org.bukkit.entity.Rabbit rabbit = ((org.bukkit.entity.Rabbit) entity);
        rabbit.setRabbitType(Objects.requireNonNullElse(rabbitType, org.bukkit.entity.Rabbit.Type.values()[0]));
    }
}
