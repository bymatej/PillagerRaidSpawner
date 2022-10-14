package com.bymatej.minecraft.plugins.pillagerraidspawner.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty;
import com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn;

import static java.util.Arrays.asList;

public class CommandConstants {

    private CommandConstants() {}

    public static class Raid {
        private Raid() {}

        // General
        public static final int MINIMUM_ARGUMENTS_NUMBER = 1;

        public static final int MAXIMUM_ARGUMENTS_NUMBER = 6;

        // Main command
        public static final String COMMAND_NAME = "raid";

        // Start parameter
        public static final String START = "start";

        // Stop parameter
        public static final String STOP = "stop";

        // Pause parameter
        public static final String PAUSE = "pause";

        // Resume parameter
        public static final String RESUME = "resume";

        // Difficulty parameters
        public static final String EASY = Difficulty.EASY.name().toLowerCase();

        public static final String MEDIUM = Difficulty.MEDIUM.name().toLowerCase();

        public static final String HARD = Difficulty.HARD.name().toLowerCase();

        // Nether and End Raid spawn control parameters
        public static final String ALWAYS = WorldSpawn.ALWAYS.name().toLowerCase();

        public static final String OVERWORLD_ONLY = WorldSpawn.OVERWORLD_ONLY.name().toLowerCase();

        public static final String OVERWORLD_NETHER = WorldSpawn.OVERWORLD_NETHER.name().toLowerCase();

        public static final String OVERWORLD_END = WorldSpawn.OVERWORLD_END.name().toLowerCase();

        // For validation
        public static List<String> getValidFirstParameters() {
            return asList(START,
                          STOP,
                          PAUSE,
                          RESUME);
        }

        public static List<String> getValidDifficultyParameters() {
            return asList(EASY,
                          MEDIUM,
                          HARD);
        }

        public static List<String> getValidWorldSpawnParameters() {
            return asList(ALWAYS,
                          OVERWORLD_ONLY,
                          OVERWORLD_NETHER,
                          OVERWORLD_END);
        }

        public static List<String> getForbiddenKeywords() {
            return joinLists(getValidFirstParameters(),
                             getValidDifficultyParameters(),
                             getValidWorldSpawnParameters(),
                             asList(COMMAND_NAME, ""));
        }
    }

    @SafeVarargs
    @SuppressWarnings("java:S3398") // Intentionally outside of Raid class
    private static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }

}
