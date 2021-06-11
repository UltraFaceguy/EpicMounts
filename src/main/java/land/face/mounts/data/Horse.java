package land.face.mounts.data;

import land.face.mounts.managers.MountManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class Horse extends Mount {

    private transient ItemStack icon;
    private Material iconMaterial;
    private String variant;
    private String id;
    private String name;
    private double speed;
    private double jump;
    private double health;
    private String armor;
    private String style;
    private String color;
    private List<String> lore;

    private transient Player mountOwner;
    private transient UUID mountOwnerUUID;
    private transient org.bukkit.entity.Horse mount;

    public Horse(String id, String name, Material iconMaterial, List<String> lore, double spd, double jump, double hp, String armor, String variant, String color, String style) {
        super(EntityType.HORSE);

        this.id = id;
        this.variant = variant;
        this.name = name;
        this.speed = spd;
        this.jump = jump;
        this.health = hp;
        this.armor = armor;
        this.color = color;
        this.style = style;
        this.iconMaterial = iconMaterial;
        this.lore = lore;

        icon = new ItemStack(iconMaterial);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        icon.setItemMeta(meta);
    }

    public Horse() {
        super(EntityType.HORSE);
    }

    @Override
    public void spawnMount() {
        spawnMount(mountOwner);
    }
    public void spawnMount(Player player) {
        this.mount = (org.bukkit.entity.Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);

        mount.setCustomName(name);
        mount.setTamed(true);
        mount.setAdult();
        mount.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        mount.setHealth(health);
        mount.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(jump);
        mount.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2 * (speed / 100));

        
        mount.setColor(color);
        mount.setStyle(style);
        mount.setVariant(variant);

        if (variant == org.bukkit.entity.Horse.Variant.HORSE) {
            mount.getInventory().setArmor(new ItemStack(Material.SADDLE));
            if (armor != null) {
                mount.getInventory().setArmor(new ItemStack(Material.getMaterial(armor)));
            }
        }
        else {
            for (int i = 0; i < mount.getInventory().getSize(); i++) {
                mount.getInventory().setItem(i, new ItemStack(Material.SADDLE));
            }
        }

        mount.addPassenger(player);
    }

    public void setColor(String color) {
        try {
           setColor(org.bukkit.entity.Horse.Color.valueOf(color));
        } catch (IllegalArgumentException iae) {
            setColor(org.bukkit.entity.Horse.Color.BROWN);
        }
    }
    public void setColor(org.bukkit.entity.Horse.Color color) {

    }

    public String getId() {
        return id;
    }

    public ItemStack getIcon() {
        if (icon == null) {
            icon = new ItemStack(icon);
            ItemMeta meta = icon.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            icon.setItemMeta(meta);
        }
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public Entity getMount() {
        return mount;
    }
}
