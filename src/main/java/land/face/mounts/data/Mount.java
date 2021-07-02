package land.face.mounts.data;

import land.face.mounts.EpicMountsPlugin;
import land.face.mounts.managers.MountManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.*;
import net.citizensnpcs.trait.versioned.*;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Objects;

@Data @AllArgsConstructor
public class Mount {

    private String id;
    private EntityType entityType;
    private boolean flyable;
    private boolean hasGravity;
    private float speedModifier;
    private double baseHP;
    private boolean baby;
    private boolean angry; //Bee, Enderman, Wolf
    private DyeColor collarColor; //Cat, Wolf
    private boolean isLaying; //Cat
    private boolean isSitting; //Cat, Fox, Ocelot, Panda, Wolf
    private double jumpStrength;
    private int size; //Phantom, Slime, MagmaCube
    private DyeColor color; //Sheep, Shulker

    //ArmorStand
    private boolean hasArms;
    private boolean hasBasePlate;
    private boolean isMarker;
    private boolean isSmall;
    private boolean isVisible;

    //Cat
    private Cat.Type catType;

    //Enderman
    private Material carriedBlockType; //TODO: SWAP TO ITEMSTACK (PENDING GSON ADAPTER)

    //Fox
    private Fox.Type foxType;
    private boolean isCrouching;
    private boolean isSleeping;

    //Horse
    //private ItemStack horseArmour; TODO: PENDING ITEMSTACK GSON ADAPATER
    private boolean isCarryingChest;
    private Horse.Color horseColor;
    private Horse.Style horseStyle;
    private boolean hasSaddle;

    //Llama
    private Llama.Color llamaColor;

    //Mooshroom
    private MushroomCow.Variant mooshroomType;

    //Panda
    private Panda.Gene mainPandaGene;
    private Panda.Gene hiddenPandaGene;

    //Parrot
    private Parrot.Variant parrotVariant;

    //Polar Bear
    private boolean isRearing;

    //Creeper
    private boolean isPowered;

    //Pufferfish
    private int puffState;

    //Rabbit
    private Rabbit.Type rabbitType;

    //Sheep
    private boolean isSheared;

    //Shulker
    private int shulkerPeek;

    //Snowman
    private boolean derp;

    //Strider
    private boolean shouldShiver;

    //Tropical Fish
    private DyeColor bodyColor;
    private TropicalFish.Pattern tropicalFishPattern;
    private DyeColor patternColor;

    //Villager
    private Villager.Profession villagerProfession;
    private Villager.Type villagerType;

    //Wolf
    private boolean isTamed;

    public void spawnMount(Player player) {
        MountManager manager = EpicMountsPlugin.getInstance().getMountManager();
        NPC npc = manager.getNPCRegistry().createNPC(entityType, id);
        npc.spawn(player.getLocation(), SpawnReason.PLUGIN);
        Entity entity = npc.getEntity();

        entity.addPassenger(player);

        //Forced Attributes
        entity.setCustomNameVisible(false);
        manager.applyMountMetadata(entity, id);
        npc.getOrAddTrait(MountTrait.class);
        npc.getOrAddTrait(Controllable.class).setEnabled(true);

        //Generic Attributes
        npc.setFlyable(flyable);
        npc.getOrAddTrait(Gravity.class).setEnabled(hasGravity);
        if (speedModifier > 0) {
            npc.getNavigator().getDefaultParameters().speedModifier(speedModifier);
        }
        if (baseHP > 0 && entity instanceof Attributable && entity instanceof Damageable) {
            Objects.requireNonNull(((Attributable) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(baseHP);
            ((Damageable) entity).setHealth(baseHP);
        }
        if (entity instanceof Ageable) {
            if (baby) ((Ageable) entity).setBaby();
            else ((Ageable) entity).setAdult();
        }
        if (entity instanceof AbstractHorse) {
            ((AbstractHorse) entity).setJumpStrength(jumpStrength);
        }

        //Mob Specific Attributes
        switch (entityType) {
            case ARMOR_STAND:
                ArmorStandTrait armorStandTrait = npc.getOrAddTrait(ArmorStandTrait.class);
                armorStandTrait.setHasArms(hasArms);
                armorStandTrait.setHasBaseplate(hasBasePlate);
                armorStandTrait.setMarker(isMarker);
                armorStandTrait.setSmall(isSmall);
                armorStandTrait.setVisible(isVisible);
                break;
            case CAT:
                CatTrait catTrait = npc.getOrAddTrait(CatTrait.class);
                catTrait.setCollarColor(collarColor);
                catTrait.setLyingDown(isLaying);
                catTrait.setSitting(isSitting);
                catTrait.setType(Objects.requireNonNullElse(catType, Cat.Type.values()[0]));
                break;
            case ENDERMAN:
                assert entity instanceof Enderman;
                EndermanTrait endermanTrait = npc.getOrAddTrait(EndermanTrait.class);
                if (angry && !endermanTrait.isAngry()) endermanTrait.toggleAngry();
                if (carriedBlockType != null) ((Enderman) entity).setCarriedMaterial(new MaterialData(carriedBlockType));
                break;
            case FOX:
                FoxTrait foxTrait = npc.getOrAddTrait(FoxTrait.class);
                foxTrait.setSitting(isSitting);
                foxTrait.setCrouching(isCrouching);
                foxTrait.setSleeping(isSleeping);
                foxTrait.setType(Objects.requireNonNullElse(foxType, Fox.Type.values()[0]));
                break;
            case SKELETON_HORSE:
            case ZOMBIE_HORSE:
            case DONKEY:
            case HORSE:
                HorseModifiers horseModifiers = npc.getOrAddTrait(HorseModifiers.class);
                horseModifiers.setCarryingChest(isCarryingChest);
                horseModifiers.setColor(horseColor);
                horseModifiers.setStyle(horseStyle);
                if (hasSaddle) horseModifiers.setSaddle(new ItemStack(Material.SADDLE));
                break;
            case LLAMA:
                LlamaTrait llamaTrait = npc.getOrAddTrait(LlamaTrait.class);
                llamaTrait.setColor(llamaColor);
                break;
            case MUSHROOM_COW:
                MushroomCowTrait mushroomCowTrait = npc.getOrAddTrait(MushroomCowTrait.class);
                mushroomCowTrait.setVariant(Objects.requireNonNullElse(mooshroomType, MushroomCow.Variant.RED));
                break;
            case PANDA:
                PandaTrait pandaTrait = npc.getOrAddTrait(PandaTrait.class);
                pandaTrait.setSitting(isSitting);
                if (mainPandaGene != null) pandaTrait.setMainGene(mainPandaGene);
                if (hiddenPandaGene != null) pandaTrait.setHiddenGene(hiddenPandaGene);
                break;
            case PARROT:
                ParrotTrait parrotTrait = npc.getOrAddTrait(ParrotTrait.class);
                parrotTrait.setVariant(parrotVariant);
                break;
            case POLAR_BEAR:
                PolarBearTrait polarBearTrait = npc.getOrAddTrait(PolarBearTrait.class);
                polarBearTrait.setRearing(isRearing);
                break;
            case CREEPER:
                assert entity instanceof Creeper;
                ((Creeper) entity).setPowered(isPowered);
                break;
            case WITHER:
                WitherTrait witherTrait = npc.getOrAddTrait(WitherTrait.class);
                witherTrait.setCharged(isPowered);
                break;
            case PUFFERFISH:
                PufferFishTrait pufferFishTrait = npc.getOrAddTrait(PufferFishTrait.class);
                pufferFishTrait.setPuffState(puffState);
                break;
            case RABBIT:
                assert entity instanceof Rabbit;
                Rabbit rabbit = ((Rabbit) entity);
                rabbit.setRabbitType(Objects.requireNonNullElse(rabbitType, Rabbit.Type.values()[0]));
                break;
            case SHEEP:
                SheepTrait sheepTrait = npc.getOrAddTrait(SheepTrait.class);
                sheepTrait.setSheared(isSheared);
                sheepTrait.setColor(Objects.requireNonNullElse(color, DyeColor.WHITE));
                break;
            case SHULKER:
                ShulkerTrait shulkerTrait = npc.getOrAddTrait(ShulkerTrait.class);
                shulkerTrait.setPeek(shulkerPeek);
                shulkerTrait.setColor(Objects.requireNonNullElse(color, DyeColor.WHITE));
                break;
            case SNOWMAN:
                SnowmanTrait snowmanTrait = npc.getOrAddTrait(SnowmanTrait.class);
                snowmanTrait.setDerp(derp);
                break;
            case TROPICAL_FISH:
                TropicalFishTrait tropicalFishTrait = npc.getOrAddTrait(TropicalFishTrait.class);
                tropicalFishTrait.setBodyColor(Objects.requireNonNullElse(bodyColor, DyeColor.WHITE));
                tropicalFishTrait.setPatternColor(Objects.requireNonNullElse(patternColor, DyeColor.WHITE));
                tropicalFishTrait.setPattern(Objects.requireNonNullElse(tropicalFishPattern, TropicalFish.Pattern.values()[0]));
                break;
            case ZOMBIE_VILLAGER:
            case VILLAGER:
                VillagerProfession villagerProfessionTrait = npc.getOrAddTrait(VillagerProfession.class);
                villagerProfessionTrait.setProfession(villagerProfession);
                VillagerTrait villagerTrait = npc.getOrAddTrait(VillagerTrait.class);
                villagerTrait.setType(villagerType);
                break;
            case WOLF:
                assert entity instanceof Wolf;
                WolfModifiers wolfModifiers = npc.getOrAddTrait(WolfModifiers.class);
                wolfModifiers.setAngry(angry);
                wolfModifiers.setSitting(isSitting);
                wolfModifiers.setTamed(isTamed);
                wolfModifiers.setCollarColor(Objects.requireNonNullElse(collarColor, DyeColor.RED));
                break;
            case PHANTOM:
                PhantomTrait phantomTrait = npc.getOrAddTrait(PhantomTrait.class);
                if (size > 0) phantomTrait.setSize(size);
                break;
            case SLIME:
            case MAGMA_CUBE:
                SlimeSize slimeSizeTrait = npc.getOrAddTrait(SlimeSize.class);
                if (size > 0) slimeSizeTrait.setSize(size);
                break;
            case STRIDER:
                assert entity instanceof Strider;
                ((Strider) entity).setShivering(shouldShiver);
                break;
        }
    }
}
