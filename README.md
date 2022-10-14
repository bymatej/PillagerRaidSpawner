# Pillager Raid Spawner

This is a Paper (not Spigot) plugin that spawns some Raiders every X minutes (where X can be a whole number between 1 and 60, inclusively).  
Raids are spawned for each player on the server, somewhere around the player at a random (but visible) location.

# Dependencies and versions

This was developed and tested using Paper 1.19.2 API (`io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT`). During the development Java 17 was used.
During testing (on local Paper server) Java 18 was used. Minecraft 1.19.2 was also used in testing.

This project also depends on [my minecraft plugin utils](https://github.com/bymatej/minecraft-plugin-utils).

# How to install?

Just put the Jar file into plugins folder.  
The JAR file can be found in the [Releases](https://github.com/bymatej/PillagerRaidSpawner/releases).  
Look for the latest release.

## Are there any permissions needed?

Only `raid.control` permission is needed.  
If you don't care about the permissions, just make yourself an OP on the server.

# How it works?
You have one command that's used to start and stop the raid. Once the start command is executed, the raid is spawned every X minutes. X is the number of minutes, and it is defined in the command itself. Omitting the amounts of minutes defaults to 2 minutes.

## Command
Example command: `/raid start 5`  
This would start a Raid every 5 minutes.

### Command arguments
Command is `/raid` and the arguments are as follows:

#### Start raid command
| Argument position | Command argument                                                   | Optional? | Default value | Description                                                    |
| ----------------- | ------------------------------------------------------------------ | --------- | ------------- | -------------------------------------------------------------- |
| 1st argument      | `start`                                                            | false     | `-`           | Starts the raid                                                |
| 2nd argument      | from `1` to `60`                                                   | true      | `2`           | Number of minutes after every raid will spawn                  |
| 3rd argument      | `easy`, `medium`, or `hard`                                        | true      | `medium`      | Difficulty level                                               |
| 4th argument      | `always`, `overworld_only`, `overworld_nether`, or `overworld_end` | true      | `always`      | Location (world dimension) where the Raid can be spawned       |
| 5th argument      | from `0.01` to `0.99`                                              | true      | `0.05`        | Increment of difficulty (hardness) - increments on every spawn |
| 6th argument      | `true`, or `false`                                                 | true      | `false`       | Flag to ignore the increments of difficulty (hardness)         |

Starting the raid does the following: 
- Spawns Raiders
- Starts a repeating task that spawns the raid every X minutes
- Makes a sound, sends a message to each player on every raid start

##### Arguments explanation
###### 1st argument
1st argument is self-explanatory. It indicates that the raid should be started, rather than stopped.  


###### 2nd argument
2nd argument represents the number of minutes. The default value is `2` minutes. That means that every 2 minutes a new Raid will spawn around each player on the server.  


###### 3rd argument
3rd argument is the difficulty. This is not the hardness feature (from the 5th argument). This difficulty defines the difficulty of one Raid.  
It defines and encompasses many aspects of the plugin.  
The difficulty defines:
- The minimum and maximum number of Raiders that spawn (number is randomised on each Raid spawn between that range)
- The type of Raiders that spawn (Entity Types which can spawn)
- The speed of Raiders (the more difficult it is, the more speed the Raiders have)
- The range of blocks where the Raid can spawn (harder the difficulty, closer to the player they spawn)
- Determines if the Raiders will get custom names (on easy Raiders always have custom names, on medium it's 50:50 chance they won't get a name, and on hard they never get a custom name)

The default value is `medium`, and all possible values are `easy`, `medium`, and `hard`.  
Meaning of each difficulty level:

| Difficulty | Range of block where Raiders can spawn | Number of Raiders that can spawn | Possible type of Raiders that may spawn                  | Speed of raiders                | Will get custom name? |
| ---------- | -------------------------------------- | -------------------------------- | -------------------------------------------------------- | ------------------------------- | --------------------- |
| `easy`     | Between `10` and `15`                  | Between `1` and `3`              | Pillager, Vindicator                                     | `0.01` more than the base speed | Always                |
| `medium`   | Between `5` and `10`                   | Between `2` and `4`              | Pillager, Vindicator, Witch                              | `0.05` more than the base speed | In 50% cases only     |
| `hard`     | Between `4` and `8`                    | Between `3` and `8`              | Pillager, Vindicator, Witch, Ravager, Illusioner, Evoker | `0.10` more than the base speed | Never                 |


Things that are random on every spawn (for the given difficulty):  
- Raider will spawn randomly between the range of blocks
- Number of raiders that will spawn in the given range
- Type of Raider
- Name of the Raider will be picked somewhat randomly from a fixed list (this feature is pretty much useless)


###### 4th argument
4th argument is called the World Spawn. This determines where the Raid can be spawned. For example, we can set it to spawn only in the Overworld and in The Nether, but not in The End.  
This is best understood by looking at the table below: 

| World Spawn flag | Will the Raid spawn in the Overworld? | Will the Raid spawn in The Nether? | Will the Raid spawn in The End? | 
| ---------------- | ------------------------------------- | ---------------------------------- | ------------------------------- |
| always           | yes                                   | yes                                | yes                             |
| overworld_only   | yes                                   | no                                 | no                              |
| overworld_nether | yes                                   | yes                                | no                              |
| overworld_end    | yes                                   | no                                 | yes                             |

The Raid always spawns in the Overworld. This cannot be turned off.


###### 5th argument
5th argument represents the increment value for the hardness. It's called "hardness", as difficulty level is another feature.  
The starting number is always `1.0`. Every time a raid is spawned, the hardness is increased by the value specified in the command.  
If the value is `0.8`, then it will increment by `0.8` on every spawn. When the total sum (of 1.0 + 0.8 on each spawn) amounts to number with a new integer number, then this integer is taken and set as a Raiders spawn multiplier.  
We start at `1.0`. The Raiders are spawned. We increase `1.0` by `0.8`. That is now `1.8`. On the next spawn it is increased by `0.8` again. So after that spawn it's `2.6`. Now the new integer is not `2` (from `2.6`). That is our new multiplier. On the next spawn it's `3.4`, so the multiplier is `3`. And so on...  
Now, when the multiplier is greater than 1, the Raiders spawns are multiplied by that number. For example, if we get only 4 Raiders initially, when the multiplier is `3`, then we would get 4 times `3` Raiders. That means that our regular spawn amount is multiplied by our multiplier. And thus, the difficulty (or hardness) is increased!  
The default value is `0.05`, and possible values are between `0.01` and `0.99`.  


###### 6th argument
6th argument is a flag that can be used to ignore the hardness increment and spawn multiplier logic. The default value is `false`.  


##### Useful information
The Raiders that are spawned are randomized. If you noticed the "Possible type of Raiders that may spawn" column in the previous table, those are the Raiders "spawnable" based on the difficulty. And which Raider will be spawned on each Raid is random. On `medium` difficulty one Raid may be "Witch, Pillager, Pillager, Pillager", on another it can be "4 Pillagers", and on another it can be "Vindicator, Witch, Pillager, Vindicator". As said - it's random every time.  

Raiders are spawned per online player on the server. This means that if there is 1 player on the server, and the difficulty is on `medium`, 4 Raiders will spawn upon initiating the command.  
If there are 3 players on the server, 4 Raiders will spawn for each player. That means 4×3 = 12 Raiders in total.


##### Command examples
This is the command: `/raid start <1-60> <easy|medium|hard> <always|overworld_only|overworld_nether|overworld_end> <0.01-0.99> <true|false>`

- `/raid start` (start the raid with default values - same as `/raid start 5 medium always 0.05 false`)
- `/raid start 5` (start the raid with only minutes defined, while the rest of the values are the default values - same as `/raid start 5 0.05 false medium`)
- `/raid start 5 easy` (start the raid with minutes and difficulty defined, while the rest of the values are the default values - same as `/raid start 5 easy always 0.05 false`)
- `/raid start 5 easy overworld_only` (start the raid with minutes, difficulty, and world spawn defined, while the rest of the values are the default values - same as `/raid start 5 easy overworld_only 0.05 false`)
- `/raid start 5 easy overworld_only 0.20` (start the raid with minutes, difficulty, world spawn defined, and the hardness increment, while the rest of the values are the default values - same as `/raid start 5 easy overworld_only 0.20 false`)
- `/raid start 5 easy overworld_only 0.20 false` (start the raid with all values customised (non-default values))


Raid can be started only once. You cannot start multiple Raid spawns.

#### Stop raid command
| Argument position | Command argument | Optional? | Description    |
| ----------------- | ---------------- | --------- | -------------- |
| 1st argument      | `stop`           | false     | Stops the raid |

Stopping the raid removes the Bad Omen effect for all players, and kills all the Raiders in the "radius" (square) of 150/100/150 blocks from each player.  
It also resets all the increments (multiplier and hardness).  
It can only stop the raid if it's already started.

##### Command examples
- `/raid stop`

#### Pause raid command
| Argument position | Command argument | Optional? | Description     |
| ----------------- | ---------------- | --------- | --------------- |
| 1st argument      | `pause`          | false     | Pauses the raid |

Pausing the raid removes the Bad Omen effect for all players, and kills all the Raiders in the "radius" (square) of 150/100/150 blocks from each player.  
It does not reset all the increments (multiplier and hardness). It remembers them for the resume. This command is similar to `stop` in that sense.  
It can only pause the raid if it's already started.

##### Command examples
- `/raid pause`

#### Resume raid command
| Argument position | Command argument  | Optional? | Description      |
| ----------------- | ----------------- | --------- | ---------------- |
| 1st argument      | `resume`          | false     | Resumes the raid |

Resuming starts a new raid. It is similar to the `start` command, but only this time there is no need to put any additional arguments - command remembers the old arguments from the previous `start` command.  
As a result, the same raid is started again (or rather resumed) as the one that was running before running the `pause` command. 
It continues with all the increments (multiplier and hardness) from where it left off before pausing. This command is similar to `start` in that sense.

##### Command examples
- `/raid resume`


# My own private stuff for building and testing
This is not supposed to be here, but I don't care...

Cleanup, build and deploy locally (for v19)
```
./gradlew clean build && rm -rf ~/projects/private/minecraft/paper-server-1.19.2/plugins/PillagerRaidSpawner/ || true && rm ~/projects/private/minecraft/paper-server-1.19.2/plugins/PillagerRaidSpawner.jar || true && ln -s ~/projects/private/PillagerRaidSpawner/build/libs/PillagerRaidSpawner-1.2.jar ~/projects/private/minecraft/paper-server-1.19.2/plugins/PillagerRaidSpawner.jar
```

##### Debug
https://www.spigotmc.org/wiki/intellij-debug-your-plugin/ 
