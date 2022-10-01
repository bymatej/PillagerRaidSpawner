package com.bymatej.minecraft.plugins.pillagerraidspawner.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class CommandConstants {

    private CommandConstants() {}

    public static class Raid {
        private Raid() {}

        // General
        public static final int MINIMUM_ARGUMENTS_NUMBER = 1;

        public static final int MAXIMUM_ARGUMENTS_NUMBER = 5;

        // Main command
        public static final String COMMAND_NAME = "raid";

        // Start parameter
        public static final String START = "start";

        // Stop parameter
        public static final String STOP = "stop";

        // Difficulty parameters
        public static final String EASY = "easy";

        public static final String MEDIUM = "medium";

        public static final String HARD = "hard";

        // For validation
        public static final List<String> VALID_FIRST_PARAMETERS = asList(START,
                                                                         STOP);

        public static final List<String> VALID_DIFFICULTY_PARAMETERS = asList(EASY,
                                                                              MEDIUM,
                                                                              HARD);

        public static final List<String> FORBIDDEN_KEYWORDS = joinLists(VALID_FIRST_PARAMETERS,
                                                                        VALID_DIFFICULTY_PARAMETERS,
                                                                        asList(COMMAND_NAME, ""));
    }

    public static <T> List<T> joinLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }

}
