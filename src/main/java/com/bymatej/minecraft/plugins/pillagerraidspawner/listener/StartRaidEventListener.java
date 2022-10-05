package com.bymatej.minecraft.plugins.pillagerraidspawner.listener;

import java.math.BigDecimal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

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
                spawnRaid(player, event.getDifficulty());
            }
            player.addPotionEffect(new PotionEffect(BAD_OMEN, MAX_VALUE, 0));
        });

        incrementHardness(event);
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
