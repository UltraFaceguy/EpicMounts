package land.face.mounts.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class Mount {

    private transient ItemStack displayItem;
    private Material icon;
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
    private transient Entity entity;

    public Mount(String id, String name, Material icon, List<String> lore, double spd, double jump, double hp, String armor, String variant, String color, String style) {
        this.id = id;
        this.variant = variant;
        this.name = name;
        this.speed = spd;
        this.jump = jump;
        this.health = hp;
        this.armor = armor;
        this.color = color;
        this.style = style;
        this.icon = icon;
        this.lore = lore;
        ItemStack display = new ItemStack(icon);
        ItemMeta meta = display.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        display.setItemMeta(meta);
        displayItem = display;
    }

    public String getId() {
        return id;
    }

    public String getVariant() {
        return variant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getJump() {
        return jump;
    }

    public void setJump(double jump) {
        this.jump = jump;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public String getArmor() {
        return armor;
    }

    public void setArmor(String armor) {
        this.armor = armor;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ItemStack getDisplayItem() {
        if (displayItem == null) {
            ItemStack display = new ItemStack(icon);
            ItemMeta meta = display.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            display.setItemMeta(meta);
            displayItem = display;
        }
        return displayItem;
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public UUID getMountOwnerUUID() {
        return mountOwnerUUID;
    }

    public Player getMountOwner() {
        return mountOwner;
    }

    public void setMountOwner(Player player) {
        this.mountOwner = player;
        this.mountOwnerUUID = player.getUniqueId();
    }

    public void setMountOwner(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) {
            this.mountOwner = Bukkit.getPlayer(uuid);
        }
        this.mountOwnerUUID = uuid;
    }

    public void spawn(Player player) {
        AbstractHorse entity = null;
        switch (variant.toUpperCase()) {
            case "HORSE":
                Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
                try {
                    horse.setColor(Horse.Color.valueOf(color));
                } catch (IllegalArgumentException e) {
                    horse.setColor(Horse.Color.CHESTNUT);
                }
                try {
                    horse.setStyle(Horse.Style.valueOf(style));
                } catch (IllegalArgumentException e) {
                    horse.setStyle(Horse.Style.NONE);
                }
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                if (armor != null) {
                    horse.getInventory().setArmor(new ItemStack(Material.getMaterial(armor)));
                }
                entity = horse;
                break;
            case "DONKEY":
                Donkey donkey = (Donkey) player.getWorld().spawnEntity(player.getLocation(), EntityType.DONKEY);
                for (int i = 0; i < donkey.getInventory().getSize(); i++) {
                    donkey.getInventory().setItem(i, new ItemStack(Material.SADDLE));
                }
                entity = donkey;
                break;
            case "MULE":
                Mule mule = (Mule) player.getWorld().spawnEntity(player.getLocation(), EntityType.MULE);
                for (int i = 0; i < mule.getInventory().getSize(); i++) {
                    mule.getInventory().setItem(i, new ItemStack(Material.SADDLE));
                }
                entity = mule;
                break;
            case "ZOMBIE_HORSE":
                ZombieHorse zombieHorse = (ZombieHorse) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE_HORSE);
                for (int i = 0; i < zombieHorse.getInventory().getSize(); i++) {
                    zombieHorse.getInventory().setItem(i, new ItemStack(Material.SADDLE));
                }
                entity = zombieHorse;
                break;
            case "SKELETON_HORSE":
                SkeletonHorse skeletonHorse = (SkeletonHorse) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
                for (int i = 0; i < skeletonHorse.getInventory().getSize(); i++) {
                    skeletonHorse.getInventory().setItem(i, new ItemStack(Material.SADDLE));
                }
                entity = skeletonHorse;
                break;
            default:
                System.out.println("[Epic Mounts] Invalid Horse Type");
        }
        entity.setCustomName(name);
        entity.setTamed(true);
        entity.setAdult();
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        entity.setHealth(health);
        entity.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(jump);
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2 * (speed / 100));
        entity.setPassenger(player);
        this.entity = entity;
    }

    public void spawn() {
        if (mountOwner.isOnline()) {
            spawn(mountOwner);
        }
    }

    public void respawn() {
        Player player = mountOwner.isOnline() ? mountOwner : (Player) entity.getPassenger();
        entity.remove();
        if (player != null) {
            spawn(player);
        }
    }

    public void remove() {
        entity.remove();
    }

    public Entity getMount() {
        return entity;
    }
}
