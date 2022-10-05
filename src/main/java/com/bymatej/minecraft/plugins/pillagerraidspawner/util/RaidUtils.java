package com.bymatej.minecraft.plugins.pillagerraidspawner.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Raider;

import com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty;

import static com.bymatej.minecraft.plugin.utils.entity.NearPlayerEntitySpawner.spawnEntityNearPlayer;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.bukkit.Sound.ENTITY_PILLAGER_CELEBRATE;
import static org.bukkit.Sound.EVENT_RAID_HORN;

public class RaidUtils {

    private RaidUtils() {}

    public static void spawnRaid(Player player, Difficulty difficulty) {
        List<Entity> raiders = new ArrayList<>();

        for (int i = 0; i < getNumberOfRaidersToSpawn(difficulty); i++) {
            Entity raider;

            if (isSetCustomNameForRaider(difficulty)) {
                EntityType entityType = difficulty.getPossibleRaiders().next();
                raider = spawnEntityNearPlayer(player,
                                               entityType,
                                               difficulty.getMinBlocksAwayToSpawn(),
                                               difficulty.getMaxBlocksAwayToSpawn(),
                                               getCustomRaiderName(player, difficulty, entityType),
                                               false);
            } else {
                raider = spawnEntityNearPlayer(player,
                                               difficulty.getPossibleRaiders().next(),
                                               difficulty.getMinBlocksAwayToSpawn(),
                                               difficulty.getMaxBlocksAwayToSpawn(),
                                               false);
            }

            increaseRaiderSpeed(raider, difficulty);
            raiders.add(raider);
        }

        playSoundAfterRaidSpawns(player, raiders.get(getPluginReference().getRandom().nextInt(raiders.size())));
        getPluginReference().getServer().broadcastMessage("Spawned " + raiders.size() + " near " + player.getName());
    }

    private static String getCustomRaiderName(Player player, Difficulty difficulty, EntityType entityType) {
        if (isFalse(isSetCustomNameForRaider(difficulty))) {
            return EMPTY;
        }

        switch (entityType) {
            case PILLAGER:
                return getPillagerCustomName(player);
            case VINDICATOR:
                return getVindicatorCustomName(player);
            case WITCH:
                return getWitchCustomName(player);
            default:
                return "Killer";

            // RAVAGER, EVOKER, and ILLUSIONER spawn only in HARD difficulty, and there are no custom names in HARD difficulty
        }
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
        switch (difficulty) {
            case EASY:
                return true;
            case HARD:
                return false;
            default:
                return getPluginReference().getRandom().nextBoolean(); // It's 50:50 the Raider will have a name in Medium difficulty
        }
    }

    private static void increaseRaiderSpeed(Entity raider, Difficulty difficulty) {
        try {
            double baseSpeed = Objects.requireNonNull(((Raider) raider).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).getBaseValue();
            Objects.requireNonNull(((Raider) raider).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(baseSpeed + difficulty.getSpeedIncrement());
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
