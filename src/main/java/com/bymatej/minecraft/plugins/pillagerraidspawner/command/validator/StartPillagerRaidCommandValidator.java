package com.bymatej.minecraft.plugins.pillagerraidspawner.command.validator;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import static com.bymatej.minecraft.plugin.utils.logging.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.COMMAND_NAME;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.MAXIMUM_ARGUMENTS_NUMBER;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.MINIMUM_ARGUMENTS_NUMBER;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.getValidDifficultyParameters;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.getValidFirstParameters;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.getValidWorldSpawnParameters;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.logging.Level.WARNING;
import static net.kyori.adventure.text.Component.text;
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
        validateFirstParameter(sender, args);
        validateSecondParameter(sender, args);
        validateThirdParameter(sender, args);
        validateFourthParameter(sender, args);
        validateFifthParameter(sender, args);
        validateSixthParameter(sender, args);
    }

    private static void validateFirstParameter(CommandSender sender, String[] args) {
        // Validate first parameter (start/stop)
        if (args.length >= 1 && !getValidFirstParameters().contains(args[0].toLowerCase())) {
            throwCommandException(sender, getParameterNotValidMessage(args[0]) + getValidParametersMessage(getValidFirstParameters()));
        }
    }

    private static void validateSecondParameter(CommandSender sender, String[] args) {
        // Validate second parameter (number of minutes)
        if (args.length >= 2) {
            try {
                int numberOfMinutes = parseInt(args[1]);
                if (numberOfMinutes < 1 || numberOfMinutes > 60) {
                    throwCommandException(sender,
                                          "High number of minutes will cause raids to rarely spawn. This may be unnoticeable. Low number of minutes might spawn raids too often. Possible values are from 1 to 60 (inclusive).");
                }
            } catch (NumberFormatException e) {
                throwCommandException(sender, format("\"%s\" is not a whole number!", args[1]), e);
            }
        }
    }

    private static void validateThirdParameter(CommandSender sender, String[] args) {
        // Validate third parameter (difficulty)
        if (args.length == 3 && !getValidDifficultyParameters().contains(args[2].toLowerCase())) {
            throwCommandException(sender,
                                  getParameterNotValidMessage(args[2], "to specify the difficulty") + getValidParametersMessage(
                                    getValidDifficultyParameters()));
        }
    }

    private static void validateFourthParameter(CommandSender sender, String[] args) {
        // Validate fourth parameter (difficulty)
        if (args.length == 4 && !getValidWorldSpawnParameters().contains(args[3].toLowerCase())) {
            throwCommandException(sender, getParameterNotValidMessage(args[3], "to specify the world where Raiders may spawn") + getValidParametersMessage(
              getValidWorldSpawnParameters()));
        }
    }

    private static void validateFifthParameter(CommandSender sender, String[] args) {
        // Validate fifth parameter (hardness increment)
        if (args.length >= 5) {
            try {
                double hardnessIncrement = parseDouble(args[4]);
                if (hardnessIncrement < 0.01 || hardnessIncrement > 0.99) {
                    throwCommandException(sender,
                                          "Increments below 0.01 are too small and may be unnoticeable. Increments over 0.99 are too high and may kill the server (by spawning too many mobs)");
                }
            } catch (NumberFormatException e) {
                throwCommandException(sender, format("\"%s\" is not a decimal number!", args[4]), e);
            }
        }
    }

    private static void validateSixthParameter(CommandSender sender, String[] args) {
        // Validate sixth parameter (flag to ignore hardness increment and multiplier)
        if (args.length >= 6 && !args[5].equalsIgnoreCase("true") && !args[5].equalsIgnoreCase("false")) {
            sender.sendMessage(format("\"%s\" is not \"true\", nor \"false\". The value \"%s\" is considered as \"false\"!", args[5], args[5]));
        }
    }

    private static String getParameterNotValidMessage(String parameter) {
        return getParameterNotValidMessage(parameter, null);
    }

    // This is stupid, but SonarLint...
    private static String getParameterNotValidMessage(String parameter, String parameterDoesWhat) {
        String notValidMsg = "Parameter " + parameter + " is not a valid parameter";
        if (parameterDoesWhat == null) {
            notValidMsg = notValidMsg + ". ";
        } else {
            notValidMsg = notValidMsg + " " + parameterDoesWhat + ". ";
        }

        return notValidMsg;
    }

    private static String getValidParametersMessage(List<String> validParameters) {
        return "Valid parameters are: " + join(",", validParameters);
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
            getPluginReference().getServer().broadcast(text(message));
        }

        log(WARNING, message);
        if (t != null) {
            throw new CommandException(message, t);
        }

        throw new CommandException(message);
    }

}
