# Repeater Sound Mod

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/vt4lfXNC?color=%2300AF5C&logo=modrinth&style=flat-square)](https://modrinth.com/mod/repeater-sound)
[![GitHub all releases](https://img.shields.io/github/downloads/HeyBlack233/RepeaterSoundMod/total?color=191970&logo=github&logoColor=181717&style=flat-square)](https://github.com/HeyBlack233/RepeaterSoundMod/releases)
![Mod Version](https://img.shields.io/badge/Version-1.3.0-orange?style=flat-square)
![MC Version](https://img.shields.io/badge/Minecraft-1.16%20--%201.20-blue?style=flat-square)

## Features

- Adding sounds to these redstone components when they are clicked.
  - Repeater
  - Redstone wire
  - Daylight detector
- The sounds (including comparator sound) are adjustable in these aspects.
  - Pitch of the sound
  - Volume of the sound
  - Random offset to the pitch of the sound
- Different [interaction modes](#interaction-modes) to adjust the behavior of blocks when clicked

### Behavior of Affected Blocks

- **Repeater**
  - A sound will be played when it got clicked (delay changed), the pitch of the sound will change with the delay of that repeater (when not using random pitch)
- **Comparator**
  - The sound and interaction of comparator can be modified by the mod
- **Redstone Wire**
  - A sound will be played when it got clicked (shape changed), the pitch of the sound will change depends on the shape of it, similar to comparator (when not using random pitch)
- **Daylight Detector**
  - A sound will be played when it got clicked (mode changed), the pitch of the sound will change depends on the mode of it, similar to comparator (when not using random pitch)

### Interaction Modes

There are three Interaction Modes to change the behavior of blocks when they are clicked

- `NORMAL`: The standard behavior of both vanilla Minecraft and the RepeaterSound mod.

- `ALARM`: A more noticeable sound will be played and a chat message will be sent to you when you interact with a block affected by this mod. 
  - The block will still be "clicked", and its state will change accordingly.
  - The alarm message can be configured using a [command](#command) provided by this mod

- `DISABLED`: This mode disables interactions with blocks affected by this mod.
  - The alarm message can be configured using a [command](#command) provided by this mod

You can switch between interaction modes using the command `/repeatersound interactionMode <mode>`. The arguments are: `NORMAL`, `ALARM`, `DISABLED`. <br>Please note that these arguments are case-sensitive and must be in uppercase.

## Configuration

### Command

Command `/repeatersound` allows you to edit the options in-game, here is a list of arguments you can use in this command:

- `/repeatersound setBasePitch <pitch>`
  - Used to adjust the base pitch that will be applied, `<pitch>` requires a `float` number
- `/repeatersound useRandomPitch <random>`
  - Used to set if the random offset will be applied to the base pitch, `<random>` requires a `boolean` value
- `/repeatersound setVolume <volume>`
  - Used to set the volume of sounds, `volume` requires a `float` number
- `/repeatersound interactionMode <mode>`
  - Used to switch interaction modes
- `/repeatersound alarmMessage <message>`
  - Used to set the message that will be sent to you in `ALARM` interaction mode, use `{Block}` and `{Pos}` to get the block and its pos
  - For example `/repeatersound alarmMessage "c: {Block}, p: {Pos}"` and click a repeater at 0 0 0 will send this message: `c: Block{minecraft:repeater}, p: 0, 0, 0`
  - Please note that the quotes outside of `<message>` are necessary.
- `/repeatersound disabledMessage <message>`
  - Used to set the message that will display on screen in `DISABLED` interaction mode.
  - Please note that the quotes outside of `<message>` are necessary.

### Config File

After the first initialize of the mod, a config file `repeatersound[version].json` will be created in the game's `config` directory. You can edit this file manually if you want.
