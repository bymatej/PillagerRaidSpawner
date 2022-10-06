package com.bymatej.minecraft.plugins.pillagerraidspawner.command;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty;
import com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn;
import com.bymatej.minecraft.plugins.pillagerraidspawner.event.StartRaidEvent;
import com.bymatej.minecraft.plugins.pillagerraidspawner.event.StopRaidEvent;

import static com.bymatej.minecraft.plugin.utils.logging.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.DEBUG;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.PillagerRaidSpawner.getPluginReference;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.PAUSE;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.RESUME;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.START;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.CommandConstants.Raid.STOP;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.command.validator.StartPillagerRaidCommandValidator.validateCommand;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty.EASY;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty.HARD;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.common.Difficulty.MEDIUM;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn.ALWAYS;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn.OVERWORLD_END;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn.OVERWORLD_NETHER;
import static com.bymatej.minecraft.plugins.pillagerraidspawner.common.WorldSpawn.OVERWORLD_ONLY;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static java.util.stream.IntStream.rangeClosed;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;

public class StartPillagerRaidCommandExecutor implements TabExecutor {

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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Cannot use StartPillagerRaidCommand object here yet... :(
        switch (args.length) {
            case 1: // /raid <start|stop|pause|resume>
                return asList(START, STOP, PAUSE, RESUME);

            case 2: // /raid <start|stop|pause|resume> <1-60>
                if (isNotStartCommand(args[0])) {
                    return emptyList();
                } else {
                    return rangeClosed(1, 60)
                             .boxed()
                             .sorted()
                             .map(Object::toString)
                             .collect(Collectors.toList());
                }

            case 3: // /raid <start|stop|pause|resume> <1-60> <easy|medium|hard>
                if (isNotStartCommand(args[0])) {
                    return emptyList();
                } else {
                    return asList(EASY.name().toLowerCase(),
                                  MEDIUM.name().toLowerCase(),
                                  HARD.name().toLowerCase());
                }

            case 4: // /raid <start|stop|pause|resume> <1-60> <easy|medium|hard> <always|overworld_only|overworld_nether|overworld_end>
                return asList(ALWAYS.name().toLowerCase(),
                              OVERWORLD_ONLY.name().toLowerCase(),
                              OVERWORLD_NETHER.name().toLowerCase(),
                              OVERWORLD_END.name().toLowerCase());

            case 5: // /raid <start|stop|pause|resume> <1-60> <easy|medium|hard> <always|overworld_only|overworld_nether|overworld_end> <0.01-0.99>
                if (isNotStartCommand(args[0])) {
                    return emptyList();
                } else {
                    return rangeClosed(1, 99)
                             .boxed()
                             .sorted()
                             .map(n -> (double) n / 100)
                             .map(Object::toString)
                             .collect(Collectors.toList());
                }

            case 6: // /raid <start|stop|pause|resume> <1-60> <easy|medium|hard> <always|overworld_only|overworld_nether|overworld_end> <0.01-0.99> <true|false>
                if (isNotStartCommand(args[0])) {
                    return emptyList();
                } else {
                    return asList("true", "false");
                }

            default:
                return emptyList();
        }
    }

    private boolean isNotStartCommand(String argument) {
        return argument.equalsIgnoreCase(STOP) ||
               argument.equalsIgnoreCase(PAUSE) ||
               argument.equalsIgnoreCase(RESUME);
    }

    private void executeCommand(Command command, CommandSender sender, String[] args) throws CommandException {
        if (DEBUG) {
            log("Command is executing");
        }

        if (sender instanceof Player && sender.hasPermission("raid.control")) { // or just make yourself an op (server operator)
            validateCommand(command, sender, args);
            StartPillagerRaidCommand raidCommand = generateRaidCommand(args);

            if (raidCommand.getCommandSwitch().equalsIgnoreCase(START)) {
                handleStartRaidCommand(raidCommand);
            } else if (raidCommand.getCommandSwitch().equalsIgnoreCase(STOP)) {
                handleStopRaidCommand();
            } else if (raidCommand.getCommandSwitch().equalsIgnoreCase(PAUSE)) {
                handlePauseRaidCommand();
            } else if (raidCommand.getCommandSwitch().equalsIgnoreCase(RESUME)) {
                handleResumeRaidCommand();
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

    private StartPillagerRaidCommand generateRaidCommand(String[] args) {
        StartPillagerRaidCommand raidCommand = new StartPillagerRaidCommand();

        // Set 1st argument to command object
        if (isNotBlank(args[0])) {
            raidCommand.setCommandSwitch(args[0]);
        } else {
            raidCommand.setCommandSwitch(null);
        }

        // Set 2nd argument to command object
        if (args.length > 2 && isNotBlank(args[1])) {
            raidCommand.setNumberOfMinutes(args[1]);
        } else {
            raidCommand.setNumberOfMinutes(2);
        }

        // Set 3rd argument to command object
        if (args.length > 3 && isNotBlank(args[2])) {
            raidCommand.setDifficulty(args[2]);
        } else {
            raidCommand.setDifficulty(MEDIUM);
        }

        // Set 4th argument to command object
        if (args.length > 4 && isNotBlank(args[3])) {
            raidCommand.setWorldSpawn(args[3]);
        } else {
            raidCommand.setWorldSpawn(ALWAYS);
        }

        // Set 5th argument to command object
        if (args.length > 5 && isNotBlank(args[4])) {
            raidCommand.setHardnessIncrement(args[4]);
        } else {
            raidCommand.setHardnessIncrement(0.05);
        }

        // Set 6th argument to command object
        if (args.length > 6 && isNotBlank(args[5])) {
            raidCommand.setIgnoreHardnessFlag(args[5]);
        } else {
            raidCommand.setIgnoreHardnessFlag(false);
        }

        return raidCommand;
    }

    private void handleStartRaidCommand(StartPillagerRaidCommand command) {
        if (command == null || isBlank(command.getCommandSwitch())) {
            throw new CommandException("An error occurred trying to execute the command");
        }

        startRaid(command.getNumberOfMinutes(),
                  command.getDifficulty(),
                  command.getWorldSpawn(),
                  command.getHardnessIncrement(),
                  command.isIgnoreHardnessFlag());

        getPluginReference().setRaidCommand(command); // Store it
    }

    private void handleStopRaidCommand() {
        if (isTrue(getPluginReference().isRaidStarted())) {
            stopRaid(false);
            getPluginReference().setRaidStarted(false);
        } else {
            getPluginReference().getServer().broadcastMessage("The raid is not started. Nothing to stop here...");
        }
    }

    private void handlePauseRaidCommand() {
        if (isTrue(getPluginReference().isRaidStarted())) {
            pauseRaid();
            getPluginReference().setRaidStarted(false);
        } else {
            getPluginReference().getServer().broadcastMessage("The raid is not started. Nothing to pause here...");
        }
    }

    private void handleResumeRaidCommand() {
        StartPillagerRaidCommand command = getPluginReference().getRaidCommand();

        if (isFalse(getPluginReference().isRaidStarted()) &&
            command != null &&
            isNotBlank(command.getCommandSwitch())) {
            handleStartRaidCommand(command);
        } else {
            getPluginReference().getServer()
                                .broadcastMessage("The raid is already started, or was never run and paused. Nothing to resume here... Start a new raid");
        }
    }

    private void startRaid(long periodInMinutes, Difficulty difficulty, WorldSpawn worldSpawn, double hardnessIncrement, boolean isIgnoreHardnessMultiplier) {
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

        int taskId = getScheduler().scheduleSyncRepeatingTask(getPluginReference(),
                                                              () -> spawnRaid(hardnessIncrement, isIgnoreHardnessMultiplier, difficulty, worldSpawn),
                                                              0L, oneSecondInTicks * numberOfSeconds * periodInMinutes);
        getPluginReference().setStartRaidSyncRepeatingTaskId(taskId);
        getPluginReference().setRaidStarted(true);
    }

    private void spawnRaid(double hardnessIncrement, boolean isIgnoreHardnessMultiplier, Difficulty difficulty, WorldSpawn worldSpawn) {
        StartRaidEvent startRaidEvent = new StartRaidEvent();
        startRaidEvent.setHardnessIncrement(hardnessIncrement);
        startRaidEvent.setIgnoreHardnessMultiplier(isIgnoreHardnessMultiplier);
        startRaidEvent.setDifficulty(difficulty);
        startRaidEvent.setWorldSpawn(worldSpawn);
        getPluginManager().callEvent(startRaidEvent);
    }

    private void stopRaid(boolean isPause) {
        StopRaidEvent stopRaidEvent = new StopRaidEvent();
        stopRaidEvent.setPauseOnly(isPause);
        getPluginManager().callEvent(stopRaidEvent);
    }

    private void pauseRaid() {
        stopRaid(true);
    }

}
