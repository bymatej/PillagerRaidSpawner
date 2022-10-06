package com.bymatej.minecraft.plugins.pillagerraidspawner.command;

import com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty;
import com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class StartPillagerRaidCommand {

    private String commandSwitch;

    private int numberOfMinutes;

    private Difficulty difficulty;

    private WorldSpawn worldSpawn;

    private double hardnessIncrement;

    private boolean ignoreHardnessFlag;

    public String getCommandSwitch() {
        return commandSwitch;
    }

    public void setCommandSwitch(String commandSwitch) {
        this.commandSwitch = commandSwitch;
    }

    public int getNumberOfMinutes() {
        return numberOfMinutes;
    }

    public void setNumberOfMinutes(int numberOfMinutes) {
        this.numberOfMinutes = numberOfMinutes;
    }

    public void setNumberOfMinutes(String numberOfMinutes) {
        this.numberOfMinutes = parseInt(numberOfMinutes);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = Difficulty.valueOf(difficulty.toUpperCase());
    }

    public WorldSpawn getWorldSpawn() {
        return worldSpawn;
    }

    public void setWorldSpawn(WorldSpawn worldSpawn) {
        this.worldSpawn = worldSpawn;
    }

    public void setWorldSpawn(String worldSpawn) {
        this.worldSpawn = WorldSpawn.valueOf(worldSpawn.toUpperCase());
    }

    public double getHardnessIncrement() {
        return hardnessIncrement;
    }

    public void setHardnessIncrement(double hardnessIncrement) {
        this.hardnessIncrement = hardnessIncrement;
    }

    public void setHardnessIncrement(String hardnessIncrement) {
        this.hardnessIncrement = parseDouble(hardnessIncrement);
    }

    public boolean isIgnoreHardnessFlag() {
        return ignoreHardnessFlag;
    }

    public void setIgnoreHardnessFlag(boolean ignoreHardnessFlag) {
        this.ignoreHardnessFlag = ignoreHardnessFlag;
    }

    public void setIgnoreHardnessFlag(String ignoreHardnessFlag) {
        this.ignoreHardnessFlag = parseBoolean(ignoreHardnessFlag);
    }

}
