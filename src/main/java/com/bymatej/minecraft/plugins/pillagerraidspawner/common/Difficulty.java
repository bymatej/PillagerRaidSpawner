package com.bymatej.minecraft.plugins.pillagerraidspawner.common;

import org.bukkit.entity.EntityType;

import static org.bukkit.entity.EntityType.EVOKER;
import static org.bukkit.entity.EntityType.ILLUSIONER;
import static org.bukkit.entity.EntityType.PILLAGER;
import static org.bukkit.entity.EntityType.RAVAGER;
import static org.bukkit.entity.EntityType.VINDICATOR;
import static org.bukkit.entity.EntityType.WITCH;

public enum Difficulty {
    EASY(getEasyPossibleRaidersList(),
         10,
         15,
         1,
         3,
         0.01),

    MEDIUM(getMediumPossibleRaidersList(),
           5,
           10,
           2,
           4,
           0.05),

    HARD(getHardPossibleRaidersList(),
         4,
         8,
         3,
         8,
         0.10);

    private final RandomCollection<EntityType> possibleRaiders;

    private final int minBlocksAwayToSpawn;

    private final int maxBlocksAwayToSpawn;

    private final int minimumNumberOfRaiders;

    private final int maximumNumberOfRaiders;

    private final double speedIncrement;

    Difficulty(RandomCollection<EntityType> possibleRaiders,
               int minBlocksAwayToSpawn,
               int maxBlocksAwayToSpawn,
               int minimumNumberOfRaiders,
               int maximumNumberOfRaiders,
               double speedIncrement) {
        this.possibleRaiders = possibleRaiders;
        this.minBlocksAwayToSpawn = minBlocksAwayToSpawn;
        this.maxBlocksAwayToSpawn = maxBlocksAwayToSpawn;
        this.minimumNumberOfRaiders = minimumNumberOfRaiders;
        this.maximumNumberOfRaiders = maximumNumberOfRaiders;
        this.speedIncrement = speedIncrement;
    }

    private static RandomCollection<EntityType> getEasyPossibleRaidersList() {
        RandomCollection<EntityType> possibleEasyRaiders = new RandomCollection<>();
        possibleEasyRaiders.add(66.67, PILLAGER);
        possibleEasyRaiders.add(33.33, VINDICATOR);
        return possibleEasyRaiders;
    }

    private static RandomCollection<EntityType> getMediumPossibleRaidersList() {
        RandomCollection<EntityType> possibleEasyRaiders = new RandomCollection<>();
        possibleEasyRaiders.add(50.00, PILLAGER);
        possibleEasyRaiders.add(40.00, VINDICATOR);
        possibleEasyRaiders.add(10.00, WITCH);
        return possibleEasyRaiders;
    }

    private static RandomCollection<EntityType> getHardPossibleRaidersList() {
        RandomCollection<EntityType> possibleEasyRaiders = new RandomCollection<>();
        possibleEasyRaiders.add(47.00, PILLAGER);
        possibleEasyRaiders.add(35.00, VINDICATOR);
        possibleEasyRaiders.add(10.00, WITCH);
        possibleEasyRaiders.add(5.00, RAVAGER);
        possibleEasyRaiders.add(2.00, EVOKER);
        possibleEasyRaiders.add(1.00, ILLUSIONER);
        return possibleEasyRaiders;
    }

    public RandomCollection<EntityType> getPossibleRaiders() {
        return possibleRaiders;
    }

    public int getMinBlocksAwayToSpawn() {
        return minBlocksAwayToSpawn;
    }

    public int getMaxBlocksAwayToSpawn() {
        return maxBlocksAwayToSpawn;
    }

    public int getMinimumNumberOfRaiders() {
        return minimumNumberOfRaiders;
    }

    public int getMaximumNumberOfRaiders() {
        return maximumNumberOfRaiders;
    }

    public double getSpeedIncrement() {
        return speedIncrement;
    }
}