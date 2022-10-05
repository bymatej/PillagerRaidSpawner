package com.bymatej.minecraft.plugins.pillagerraidspawner.common;

import org.bukkit.entity.EntityType;

public class EntityTypePercentage {
    private EntityType entityType;

    private double percentage;

    public EntityTypePercentage(EntityType entityType, double percentage) {
        this.entityType = entityType;
        this.percentage = percentage;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
