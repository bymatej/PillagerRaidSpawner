package com.bymatej.minecraft.plugins.pillagerraidspawner.listener;

import java.math.BigDecimal;
import java.util.Objects;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Raider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import com.bymatej.minecraft.plugins.pillagerraidspawner.command.StartPillagerRaidCommand.Difficulty;
import com.bymatej.minecraft.plugins.pillagerraidspawner.common.PossibleRaiders.PossibleEasyRaiders;
import com.bymatej.minecraft.plugins.pillagerraidspawner.common.PossibleRaiders.PossibleHardRaiders;
import com.bymatej.minecraft.plugins.pillagerraidspawner.common.PossibleRaiders.PossibleMediumRaiders;
import com.bymatej.minecraft.plugins.pillagerraidspawner.event.StartRaidEvent;

import static com.bymatej.minecraft.plugin.utils.entity.NearPlayerEntitySpawner.spawnEntityNearPlayer;
import static com.bymatej.minecraft.plugin.utils.logging.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.DEBUG;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.StartPillagerRaidCommand.Difficulty.EASY;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.StartPillagerRaidCommand.Difficulty.HARD;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.StartPillagerRaidCommand.Difficulty.MEDIUM;
import static java.lang.Integer.MAX_VALUE;
import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_UP;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Sound.ENTITY_PILLAGER_CELEBRATE;
import static org.bukkit.Sound.EVENT_RAID_HORN;
import static org.bukkit.potion.PotionEffectType.BAD_OMEN;

public class StartRaidEventListener implements Listener {

    @EventHandler
    public void onStartRaidEvent(StartRaidEvent event) {
        if (DEBUG) {
            log("Staring new raid...");
        }

        getPluginReference().getServer().broadcastMessage("New raid has started...");
        getOnlinePlayers().forEach(player -> {
            for (int i = 1; i <= getPluginReference().getRaidHardnessMultiplier(); i++) {
                switch (event.getDifficulty()) {
                    case EASY:
                        spawnEasyRaid(player, EASY);
                        break;
                    case HARD:
                        spawnHardRaid(player, HARD);
                        break;
                    default:
                        spawnMediumRaid(player, MEDIUM);
                        break;
                }
            }
            player.addPotionEffect(new PotionEffect(BAD_OMEN, MAX_VALUE, 0));
        });

        incrementHardness(event);
    }

    private void spawnEasyRaid(Player player, Difficulty difficulty) {
        increaseSpeed(spawnEntityNearPlayer(player,
                                            PossibleEasyRaiders.getRandomRaider().getEntityType(),
                                            5,
                                            10,
                                            "x" + player.getName() + "x",
                                            false),
                      difficulty);

        increaseSpeed(spawnEntityNearPlayer(player,
                                            PossibleEasyRaiders.getRandomRaider().getEntityType(),
                                            5,
                                            10,
                                            player.getName() + "=DEAD",
                                            false),
                      difficulty);

        Entity lastEntityOnEasy = spawnEntityNearPlayer(player,
                                                        PossibleEasyRaiders.getRandomRaider().getEntityType(),
                                                        5,
                                                        10,
                                                        "Destroyer",
                                                        false);
        increaseSpeed(lastEntityOnEasy, difficulty);

        playSoundAfterRaidSpawns(player, lastEntityOnEasy);
    }

    private void spawnMediumRaid(Player player, Difficulty difficulty) {
        spawnEasyRaid(player, difficulty);

        increaseSpeed(spawnEntityNearPlayer(player,
                                            PossibleMediumRaiders.getRandomRaider().getEntityType(),
                                            5,
                                            10,
                                            "Axe Man",
                                            false),
                      difficulty);
    }

    private void spawnHardRaid(Player player, Difficulty difficulty) {
        spawnMediumRaid(player, difficulty);

        increaseSpeed(spawnEntityNearPlayer(player,
                                            PossibleHardRaiders.getRandomRaider().getEntityType(),
                                            5,
                                            10,
                                            "Killer",
                                            false),
                      difficulty);

        increaseSpeed(spawnEntityNearPlayer(player,
                                            PossibleHardRaiders.getRandomRaider().getEntityType(),
                                            10,
                                            20,
                                            false),
                      difficulty);

        increaseSpeed(spawnEntityNearPlayer(player,
                                            PossibleHardRaiders.getRandomRaider().getEntityType(),
                                            5,
                                            10,
                                            player.getName() + " smasher",
                                            false),
                      difficulty);

        increaseSpeed(spawnEntityNearPlayer(player,
                                            PossibleHardRaiders.getRandomRaider().getEntityType(),
                                            10,
                                            20,
                                            "MURDER WIZARD",
                                            false),
                      difficulty);
    }

    private void increaseSpeed(Entity raider, Difficulty difficulty) {
        double speedIncrement;
        switch (difficulty) {
            case EASY:
                speedIncrement = 0.01;
                break;
            case HARD:
                speedIncrement = 0.10;
                break;
            default:
                speedIncrement = 0.05;
                break;
        }

        try {
            double baseSpeed = Objects.requireNonNull(((Raider) raider).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).getBaseValue();
            Objects.requireNonNull(((Raider) raider).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(baseSpeed + speedIncrement);
        } catch (Exception e) {
            // catch silently
        }
    }

    /**
     * Play the raid horn, and pillager celebrate sound to a player at the location of a last spawned pillager in Easy (as Easy is used in Medium and Hard too)
     *
     * @param player current player
     * @param entity last entity spawned for Easy difficulty
     */
    private void playSoundAfterRaidSpawns(Player player, Entity entity) {
        player.playSound(entity.getLocation(), EVENT_RAID_HORN, 3, 0);
        player.playSound(entity.getLocation(), ENTITY_PILLAGER_CELEBRATE, 5, -1);
    }

    private void incrementHardness(StartRaidEvent event) {
        // If the hardness multiplier is disabled, it will always be set to 1, so the above for loop will always loop only once
        if (isFalse(event.isIgnoreHardnessMultiplier())) {
            BigDecimal result = ONE.multiply(BigDecimal.valueOf(getPluginReference().getRaidHardnessIncrement())).setScale(2, HALF_UP);
            getPluginReference().setRaidHardnessMultiplier((int) result.doubleValue());
            getPluginReference().setRaidHardnessIncrement(getPluginReference().getRaidHardnessIncrement() + event.getHardnessIncrement());
        }
    }

}
