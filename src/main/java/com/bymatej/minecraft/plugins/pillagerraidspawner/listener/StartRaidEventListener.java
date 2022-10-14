package com.bymatej.minecraft.plugins.pillagerraidspawner.listener;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn;
import com.bymatej.minecraft.plugins.pillagerraidspawner.event.StartRaidEvent;

import static com.bymatej.minecraft.plugin.utils.logging.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.DEBUG;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.util.RaidUtils.spawnRaid;
import static java.lang.Integer.MAX_VALUE;
import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_UP;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.World.Environment.NETHER;
import static org.bukkit.World.Environment.NORMAL;
import static org.bukkit.World.Environment.THE_END;
import static org.bukkit.potion.PotionEffectType.BAD_OMEN;

public class StartRaidEventListener implements Listener {

    @EventHandler
    public void onStartRaidEvent(StartRaidEvent event) {
        if (DEBUG) {
            log("Staring new raid...");
        }

        AtomicInteger countOfOnlinePlayersForWhichRaidShouldNotSpawn = new AtomicInteger(0);

        getOnlinePlayers().forEach(player -> {
            if (shouldSpawnRaidForPlayer(player, event.getWorldSpawn())) {
                player.sendMessage("New raid has started...");
                for (int i = 1; i <= getPluginReference().getRaidHardnessMultiplier(); i++) {
                    spawnRaid(player, event.getDifficulty());
                }
                player.addPotionEffect(new PotionEffect(BAD_OMEN, MAX_VALUE, 0));
            } else {
                countOfOnlinePlayersForWhichRaidShouldNotSpawn.getAndIncrement();
            }
        });

        if (isFalse(getOnlinePlayers().isEmpty()) &&
            (countOfOnlinePlayersForWhichRaidShouldNotSpawn.get() == 0 ||
             countOfOnlinePlayersForWhichRaidShouldNotSpawn.get() != getOnlinePlayers().size())) {
            incrementHardness(event);
        }
    }

    private boolean shouldSpawnRaidForPlayer(Player player, WorldSpawn worldSpawn) {
        if (player.getWorld().getEnvironment().equals(NORMAL)) {
            return true;
        } else if (player.getWorld().getEnvironment().equals(NETHER)) {
            return worldSpawn.isNetherSpawnEnabled();
        } else if (player.getWorld().getEnvironment().equals(THE_END)) {
            return worldSpawn.isEndSpawnEnabled();
        } else {
            return true;
        }
    }

    private void incrementHardness(StartRaidEvent event) {
        // If the hardness multiplier is disabled, it will always be set to 1, so the above for loop will always loop only once
        if (isFalse(event.isIgnoreHardnessMultiplier())) {
            BigDecimal result = ONE.multiply(BigDecimal.valueOf(getPluginReference().getRaidHardnessIncrement())).setScale(2, HALF_UP);
            if (getPluginReference().getRaidHardnessMultiplier() < 20) { // Limit to multiplier of 20 - not to kill the server
                getPluginReference().setRaidHardnessMultiplier((int) result.doubleValue());
                getPluginReference().setRaidHardnessIncrement(getPluginReference().getRaidHardnessIncrement() + event.getHardnessIncrement());
            }
        }
    }

}
