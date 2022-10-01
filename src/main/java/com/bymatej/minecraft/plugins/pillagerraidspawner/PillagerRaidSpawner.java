package com.bymatej.minecraft.plugins.pillagerraidspawner;

import java.util.Random;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bymatej.minecraft.plugins.pillagerraidspawner.command.StartPillagerRaidCommand;
import com.bymatej.minecraft.plugins.pillagerraidspawner.listener.StartRaidEventListener;
import com.bymatej.minecraft.plugins.pillagerraidspawner.listener.StopRaidEventListener;

import static com.bymatej.minecraft.plugin.utils.logging.LoggingUtils.log;
import static java.util.Objects.requireNonNull;

public final class PillagerRaidSpawner extends JavaPlugin {

    private static PillagerRaidSpawner plugin;

    private static final Random RANDOM = new Random();

    public static final boolean DEBUG = false; // set to false to reduce the amount of logs, and set to true to get more info in logs and faster raid spawns

    private int startRaidSyncRepeatingTaskId; // ID of the task responsible for periodic raid spawns

    private double raidHardnessIncrement; // Stores the value that increments on every spawn, and when it reaches the next whole number it sets the hardness multiplier to that whole number

    private int raidHardnessMultiplier; // raid hardness - grows over time (based on raidHardnessIncrementor). Formula: spawn mobs for raid * raidHardnessMultiplier

    private boolean isRaidStarted; // flag to prevent starting multiple raid-starter tasks

    @Override
    public void onEnable() {
        setPluginReference(this);
        registerConfig();
        registerCommands();
        registerEventListeners();

        setStartRaidSyncRepeatingTaskId(0);
        setRaidHardnessIncrement(1.0);
        setRaidHardnessMultiplier(1);
        setRaidStarted(false);

        if (DEBUG) {
            log("Plugin PillagerRaidSpawner loaded!");
        }
    }

    private void registerConfig() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void registerCommands() {
        requireNonNull(getCommand("raid")).setExecutor(new StartPillagerRaidCommand());
    }

    private void registerEventListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new StartRaidEventListener(), this);
        pluginManager.registerEvents(new StopRaidEventListener(), this);
    }

    @Override
    public void onDisable() {
        if (DEBUG) {
            log("Plugin PillagerRaidSpawner un-loaded!");
        }
    }

    public static PillagerRaidSpawner getPluginReference() {
        return plugin;
    }

    private static void setPluginReference(PillagerRaidSpawner pluginReference) {
        plugin = pluginReference;
    }

    public Random getRandom() {
        return RANDOM;
    }

    public int getStartRaidSyncRepeatingTaskId() {
        return startRaidSyncRepeatingTaskId;
    }

    public void setStartRaidSyncRepeatingTaskId(int startRaidSyncRepeatingTaskId) {
        this.startRaidSyncRepeatingTaskId = startRaidSyncRepeatingTaskId;
    }

    public double getRaidHardnessIncrement() {
        return raidHardnessIncrement;
    }

    public void setRaidHardnessIncrement(double raidHardnessIncrement) {
        this.raidHardnessIncrement = raidHardnessIncrement;
    }

    public int getRaidHardnessMultiplier() {
        return raidHardnessMultiplier;
    }

    public void setRaidHardnessMultiplier(int raidHardnessMultiplier) {
        this.raidHardnessMultiplier = raidHardnessMultiplier;
    }

    public boolean isRaidStarted() {
        return isRaidStarted;
    }

    public void setRaidStarted(boolean raidStarted) {
        isRaidStarted = raidStarted;
    }
}
