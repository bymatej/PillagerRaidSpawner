package com.bymatej.minecraft.plugins.pillagerraidspawner.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty;
import com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn;

public class StartRaidEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private double hardnessIncrement;

    private boolean isIgnoreHardnessMultiplier;

    private Difficulty difficulty;

    private WorldSpawn worldSpawn;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("java:S4144") // in spite of it being the same as getHandlers, it's required by Spigot/Paper
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public double getHardnessIncrement() {
        return hardnessIncrement;
    }

    public void setHardnessIncrement(double hardnessIncrement) {
        this.hardnessIncrement = hardnessIncrement;
    }

    public boolean isIgnoreHardnessMultiplier() {
        return isIgnoreHardnessMultiplier;
    }

    public void setIgnoreHardnessMultiplier(boolean ignoreHardnessMultiplier) {
        isIgnoreHardnessMultiplier = ignoreHardnessMultiplier;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public WorldSpawn getWorldSpawn() {
        return worldSpawn;
    }

    public void setWorldSpawn(WorldSpawn worldSpawn) {
        this.worldSpawn = worldSpawn;
    }
}
