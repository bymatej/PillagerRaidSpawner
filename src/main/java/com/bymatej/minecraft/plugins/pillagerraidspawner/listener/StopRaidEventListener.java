package com.bymatej.minecraft.plugins.pillagerraidspawner.listener;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Illager;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bymatej.minecraft.plugins.pillagerraidspawner.command.StartPillagerRaidCommand;
import com.bymatej.minecraft.plugins.pillagerraidspawner.event.StopRaidEvent;

import static com.bymatej.minecraft.plugin.utils.logging.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.DEBUG;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static net.kyori.adventure.text.Component.text;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.potion.PotionEffectType.BAD_OMEN;

public class StopRaidEventListener implements Listener {

    @EventHandler
    public void onStopRaidEvent(StopRaidEvent event) {
        if (DEBUG) {
            log("Stopping the raid...");
        }

        getScheduler().cancelTask(getPluginReference().getStartRaidSyncRepeatingTaskId());

        if (event.isPauseOnly()) {
            getPluginReference().getServer().broadcast(text("The raid has been paused. Phew... (for now)"));
        } else {
            getPluginReference().setRaidHardnessMultiplier(1);
            getPluginReference().setRaidHardnessIncrement(1.);
            getPluginReference().setRaidCommand(new StartPillagerRaidCommand()); // reset
            getPluginReference().getServer().broadcast(text("The raid has been stopped. Phew..."));
        }

        getPluginReference().getServer().getOnlinePlayers().forEach(player -> {
            killNearbyRaiders(player);
            player.removePotionEffect(BAD_OMEN);
        });
    }

    private void killNearbyRaiders(Player player) {
        getNearbyRaiders(player.getWorld(), player.getLocation(), Pillager.class).forEach(Entity::remove);
        getNearbyRaiders(player.getWorld(), player.getLocation(), Vindicator.class).forEach(Entity::remove);
        getNearbyRaiders(player.getWorld(), player.getLocation(), Ravager.class).forEach(Entity::remove);
        getNearbyRaiders(player.getWorld(), player.getLocation(), Evoker.class).forEach(Entity::remove);
        getNearbyRaiders(player.getWorld(), player.getLocation(), Vex.class).forEach(Entity::remove);
        getNearbyRaiders(player.getWorld(), player.getLocation(), Illusioner.class).forEach(Entity::remove);
        getNearbyRaiders(player.getWorld(), player.getLocation(), Witch.class).forEach(Entity::remove);
        getNearbyRaiders(player.getWorld(), player.getLocation(), Illager.class).forEach(Entity::remove);
        getNearbyRaiders(player.getWorld(), player.getLocation(), Raider.class).forEach(Entity::remove);
    }

    private <T> Collection<Entity> getNearbyRaiders(World world, Location location, Class<T> clazz) {
        return world.getNearbyEntities(location, 150, 100, 150, clazz::isInstance);
    }

}
