package com.bymatej.minecraft.plugins.pillagerraidspawner.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Raider;

import com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty;

import static com.bymatej.minecraft.plugin.utils.entity.NearPlayerEntitySpawner.spawnEntityNearPlayer;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static java.util.Arrays.asList;
import static net.kyori.adventure.text.Component.text;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.bukkit.Sound.ENTITY_PILLAGER_CELEBRATE;
import static org.bukkit.Sound.EVENT_RAID_HORN;
import static org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED;

public class RaidUtils {

    private RaidUtils() {}

    public static void spawnRaid(Player player, Difficulty difficulty) {
        List<Entity> raiders = new ArrayList<>();

        for (int i = 0; i < getNumberOfRaidersToSpawn(difficulty); i++) {
            Entity raider;
            EntityType entityType = difficulty.getPossibleRaiders().next();

            if (isSetCustomNameForRaider(difficulty)) {
                raider = spawnEntityNearPlayer(player,
                                               entityType,
                                               difficulty.getMinBlocksAwayToSpawn(),
                                               difficulty.getMaxBlocksAwayToSpawn(),
                                               getCustomRaiderName(player, difficulty, entityType),
                                               false);
            } else {
                raider = spawnEntityNearPlayer(player,
                                               entityType,
                                               difficulty.getMinBlocksAwayToSpawn(),
                                               difficulty.getMaxBlocksAwayToSpawn(),
                                               false);
                raider.setCustomNameVisible(false);
            }

            increaseRaiderSpeed(raider, difficulty);
            raiders.add(raider);
        }

        playSoundAfterRaidSpawns(player, raiders.get(getPluginReference().getRandom().nextInt(raiders.size())));
        getPluginReference().getServer().broadcast(text("Spawned " + raiders.size() + " near " + player.getName()));
    }

    private static String getCustomRaiderName(Player player, Difficulty difficulty, EntityType entityType) {
        if (isFalse(isSetCustomNameForRaider(difficulty))) {
            return EMPTY;
        }

        // RAVAGER, EVOKER, and ILLUSIONER spawn only in HARD difficulty, and there are no custom names in HARD difficulty
        return switch (entityType) {
            case PILLAGER -> getPillagerCustomName(player);
            case VINDICATOR -> getVindicatorCustomName(player);
            case WITCH -> getWitchCustomName(player);
            default -> "Killer";
        };
    }

    private static int getNumberOfRaidersToSpawn(Difficulty difficulty) {
        int min = difficulty.getMinimumNumberOfRaiders();
        int max = difficulty.getMaximumNumberOfRaiders();
        return getPluginReference().getRandom().nextInt(max - min + 1) + min;
    }

    private static String getPillagerCustomName(Player player) {
        return getRandomName(asList("Archon",
                                    "Kragoth",
                                    "Alarik",
                                    "Drimacus",
                                    "Kill all Players",
                                    "Destroyer",
                                    "x" + player.getName() + "x",
                                    player.getName() + "=DEAD",
                                    "AAAaAaAAaAAaAaaaa"));
    }

    private static String getVindicatorCustomName(Player player) {
        return getRandomName(asList("Destroyer",
                                    "Destroyer of " + player.getName(),
                                    player.getName() + " killer",
                                    "Axe man"));
    }

    private static String getWitchCustomName(Player player) {
        return getRandomName(asList("Sick witch",
                                    player.getName() + "'s END",
                                    "Candy seller"));
    }

    private static String getRandomName(List<String> validNames) {
        return validNames.get(getPluginReference().getRandom().nextInt(validNames.size())); // random item from the list
    }

    private static boolean isSetCustomNameForRaider(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> true;
            case HARD -> false;
            // It's 50:50 the Raider will have a name in Medium difficulty
            default -> getPluginReference().getRandom().nextBoolean();
        };
    }

    private static void increaseRaiderSpeed(Entity raider, Difficulty difficulty) {
        try {
            AttributeInstance attribute = Objects.requireNonNull(((Raider) raider).getAttribute(GENERIC_MOVEMENT_SPEED));
            double baseSpeed = attribute.getBaseValue();
            attribute.setBaseValue(baseSpeed + difficulty.getSpeedIncrement());
        } catch (Exception e) {
            // catch silently
        }
    }

    /**
     * Play the raid horn, and pillager celebrate sound to a player at the location of any of the spawned Raiders
     *
     * @param player current player
     * @param entity entity at whose location the sound will play
     */
    private static void playSoundAfterRaidSpawns(Player player, Entity entity) {
        player.playSound(entity.getLocation(), EVENT_RAID_HORN, 3, 0);
        player.playSound(entity.getLocation(), ENTITY_PILLAGER_CELEBRATE, 5, -1);
    }

}
