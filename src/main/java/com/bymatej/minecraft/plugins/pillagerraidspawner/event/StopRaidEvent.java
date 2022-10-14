package com.bymatej.minecraft.plugins.pillagerraidspawner.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class StopRaidEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private boolean pauseOnly = false;

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

    public boolean isPauseOnly() {
        return pauseOnly;
    }

    public void setPauseOnly(boolean pauseOnly) {
        this.pauseOnly = pauseOnly;
    }
}