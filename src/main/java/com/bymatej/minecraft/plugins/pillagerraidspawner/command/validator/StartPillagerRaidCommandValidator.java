package com.bymatej.minecraft.plugins.pillagerraidspawner.command.validator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import static com.bymatej.minecraft.plugin.utils.logging.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.COMMAND_NAME;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.MAXIMUM_ARGUMENTS_NUMBER;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.MINIMUM_ARGUMENTS_NUMBER;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.VALID_DIFFICULTY_PARAMETERS;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.VALID_FIRST_PARAMETERS;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.logging.Level.WARNING;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

public class StartPillagerRaidCommandValidator {

    private static final String GENERIC_MESSAGE = "Something went wrong while executing the command!";

    private StartPillagerRaidCommandValidator() {}

    public static void validateCommand(Command command, CommandSender sender, String[] args) throws CommandException {
        performGeneralValidation(command, sender, args);
        performNumberOfArgumentsValidation(sender, args);
        performArgumentsValidation(sender, args);
    }

    private static void performGeneralValidation(Command command, CommandSender sender, String[] args) {
        if (command == null || sender == null || args == null) {
            if (sender == null) {
                throwCommandException(GENERIC_MESSAGE);
            } else {
                throwCommandException(sender, GENERIC_MESSAGE);
            }
        }

        assert command != null;
        if (isFalse(COMMAND_NAME.equalsIgnoreCase(command.getName()))) {
            throwCommandException(sender, GENERIC_MESSAGE);
        }
    }

    private static void performNumberOfArgumentsValidation(CommandSender sender, String[] args) {
        String message;
        if (args.length < MINIMUM_ARGUMENTS_NUMBER) {
            message = "Less than " + MINIMUM_ARGUMENTS_NUMBER + " parameters given. Unrecognized request";
            throwCommandException(sender, message);
        }

        if (args.length > MAXIMUM_ARGUMENTS_NUMBER) {
            message = "More than " + MAXIMUM_ARGUMENTS_NUMBER + " parameters given. Unrecognized request";
            throwCommandException(sender, message);
        }
    }

    private static void performArgumentsValidation(CommandSender sender, String[] args) {
        String message;

        // Validate first parameter (start/stop)
        if (args.length >= 1 && !VALID_FIRST_PARAMETERS.contains(args[0].toLowerCase())) {
            message = "Parameter " + args[0] + " is not a valid first parameter. Valid parameters are: " + join(",", VALID_FIRST_PARAMETERS);
            throwCommandException(sender, message);
        }

        // Validate second parameter (number of minutes)
        if (args.length >= 2) {
            try {
                int numberOfMinutes = parseInt(args[1]);
                if (numberOfMinutes < 1 || numberOfMinutes > 60) {
                    message = "High number of minutes will cause raids to rarely spawn. This may be unnoticeable. Low number of minutes might spawn raids too often. Possible values are from 1 to 60 (inclusive).";
                    throwCommandException(sender, message);
                }
            } catch (NumberFormatException e) {
                message = format("Parameter \"%s\" is not a whole number!", args[1]);
                throwCommandException(sender, message, e);
            }
        }

        // Validate third parameter (hardness increment)
        if (args.length >= 3) {
            try {
                double hardnessIncrement = parseDouble(args[2]);
                if (hardnessIncrement < 0.01 || hardnessIncrement > 0.99) {
                    message = "Increments below 0.01 are too small and may be unnoticeable. Increments over 0.99 are too high and may kill the server (by spawning too many mobs)";
                    throwCommandException(sender, message);
                }
            } catch (NumberFormatException e) {
                message = format("Parameter \"%s\" is not a decimal number!", args[2]);
                throwCommandException(sender, message, e);
            }
        }

        // Validate fourth parameter (flag to ignore hardness increment and multiplier)
        if (args.length >= 4 && !args[3].equalsIgnoreCase("true") && !args[3].equalsIgnoreCase("false")) {
            message = format("Parameter \"%s\" is not \"true\", nor \"false\". The value \"%s\" is considered as \"false\"!", args[3], args[3]);
            sender.sendMessage(message);
        }

        // Validate fifth parameter (difficulty)
        if (args.length == 5 && !VALID_DIFFICULTY_PARAMETERS.contains(args[4].toLowerCase())) {
            message = "Parameter " + args[4] + " is not a valid parameter to specify the difficulty. Valid parameters are: " + join(",",
                                                                                                                                    VALID_DIFFICULTY_PARAMETERS);
            throwCommandException(sender, message);
        }
    }

    private static void throwCommandException(String message) {
        throwCommandException(null, message);
    }

    private static void throwCommandException(CommandSender sender, String message) {
        throwCommandException(sender, message, null);
    }

    private static void throwCommandException(CommandSender sender, String message, Throwable t) {
        if (sender != null) {
            sender.sendMessage(message);
        } else {
            getPluginReference().getServer().broadcastMessage(message);
        }

        log(WARNING, message);
        if (t != null) {
            throw new CommandException(message, t);
        }

        throw new CommandException(message);
    }

}
