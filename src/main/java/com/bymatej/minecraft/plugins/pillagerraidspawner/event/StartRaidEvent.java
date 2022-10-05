package com.bymatej.minecraft.plugins.pillagerraidspawner.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty;

public class StartRaidEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private double hardnessIncrement;

    private boolean isIgnoreHardnessMultiplier;

    private Difficulty difficulty;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

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
}
