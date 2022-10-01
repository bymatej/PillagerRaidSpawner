package com.bymatej.minecraft.plugins.pillagerraidspawner.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bymatej.minecraft.plugins.pillagerraidspawner.event.StartRaidEvent;
import com.bymatej.minecraft.plugins.pillagerraidspawner.event.StopRaidEvent;

import static com.bymatej.minecraft.plugin.utils.logging.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.DEBUG;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.START;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.STOP;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.StartPillagerRaidCommand.Difficulty.MEDIUM;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.validator.StartPillagerRaidCommandValidator.validateCommand;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;

public class StartPillagerRaidCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            executeCommand(command, sender, args);
            return true;
        } catch (CommandException ex) {
            log(WARNING, "Error executing the command.", ex);
        }

        return false;
    }

    private void executeCommand(Command command, CommandSender sender, String[] args) throws CommandException {
        if (DEBUG) {
            log("Command is executing");
        }

        if (sender instanceof Player && sender.hasPermission("raid.control")) { // or just make yourself an op (server operator)
            validateCommand(command, sender, args);

            if (args[0].equalsIgnoreCase(START)) {
                handleStartRaidCommand(args);
            } else if (args[0].equalsIgnoreCase(STOP)) {
                handleStopRaidCommand();
            } else {
                String message = "This should never happen...";
                sender.sendMessage(message);
                log(SEVERE, message);
                throw new CommandException(message);
            }
        } else {
            String message = "You cannot execute this command. You're not a Player, or you don't have the permission.";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }
    }

    private void handleStartRaidCommand(String[] args) {
        switch (args.length) {
            case 1:
                startRaid(2, 0.05, false, MEDIUM);
                break;
            case 2:
                startRaid(parseInt(args[1]), 0.05, false, MEDIUM);
                break;
            case 3:
                startRaid(parseInt(args[1]), parseDouble(args[2]), false, MEDIUM);
                break;
            case 4:
                startRaid(parseInt(args[1]), parseDouble(args[2]), parseBoolean(args[3]), MEDIUM);
                break;
            case 5:
                Difficulty difficulty = Difficulty.valueOf(args[4].toUpperCase());
                startRaid(parseInt(args[1]), parseDouble(args[2]), parseBoolean(args[3]), difficulty);
                break;
            default:
                throw new CommandException("Invalid amount of command arguments");
        }
    }

    private void handleStopRaidCommand() {
        if (isTrue(getPluginReference().isRaidStarted())) {
            stopRaid();
            getPluginReference().setRaidStarted(false);
        } else {
            getPluginReference().getServer().broadcastMessage("The raid is not started. Nothing to stop here...");
        }
    }

    private void startRaid(long periodInMinutes, double hardnessIncrement, boolean isIgnoreHardnessMultiplier, Difficulty difficulty) {
        long oneSecondInTicks = 20L;
        int numberOfSeconds = 60; // one minute
        if (DEBUG) {
            numberOfSeconds = 10; // one sixth of a minute - useful for testing and for quicker raid spawning
        }

        if (isTrue(getPluginReference().isRaidStarted())) {
            getPluginReference().getServer().broadcastMessage("The raid is already started. You cannot start a new raid!");
            return;
        }

        String message = "The Pillager raid has started! New raid will spawn every " + periodInMinutes + (periodInMinutes == 1 ? " minute" : " minutes");
        getPluginReference().getServer().broadcastMessage(message);

        int taskId = getScheduler().scheduleSyncRepeatingTask(getPluginReference(), () -> spawnRaid(hardnessIncrement, isIgnoreHardnessMultiplier, difficulty),
                                                              0L, oneSecondInTicks * numberOfSeconds * periodInMinutes);
        getPluginReference().setStartRaidSyncRepeatingTaskId(taskId);
        getPluginReference().setRaidStarted(true);
    }

    private void spawnRaid(double hardnessIncrement, boolean isIgnoreHardnessMultiplier, Difficulty difficulty) {
        StartRaidEvent startRaidEvent = new StartRaidEvent();
        startRaidEvent.setHardnessIncrement(hardnessIncrement);
        startRaidEvent.setIgnoreHardnessMultiplier(isIgnoreHardnessMultiplier);
        startRaidEvent.setDifficulty(difficulty);
        getPluginManager().callEvent(startRaidEvent);
    }

    private void stopRaid() {
        StopRaidEvent stopRaidEvent = new StopRaidEvent();
        getPluginManager().callEvent(stopRaidEvent);
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

}
