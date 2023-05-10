# Repeater Sound Mod

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/vt4lfXNC?color=%2300AF5C&logo=modrinth&style=flat-square)](https://modrinth.com/mod/repeater-sound)
[![GitHub all releases](https://img.shields.io/github/downloads/HeyBlack233/RepeaterSoundMod/total?color=191970&logo=github&logoColor=181717&style=flat-square)](https://github.com/HeyBlack233/RepeaterSoundMod/releases)
![Mod Version](https://img.shields.io/badge/Version-1.2.0-orange?style=flat-square)
![MC Version](https://img.shields.io/badge/Minecraft-1.16%20--%201.19-blue?style=flat-square)

## Description

RepeaterSound is a simple client side mod, the mod added sound events when some blocks are clicked. Requires [Fabric API](https://modrinth.com/mod/fabric-api)

Currently supports repeater, redstone wire, daylight detector. The pitch of comparator sound can also be adjusted by the mod.

### Repeater

Repeater will play a sound when it got clicked(delay changed), this sound is displayed as "Repeater clicks" in subtitle.

### Redstone Wire

Redstone wire will play a sound when it got clicked and the shape is changed between dot and cross, this sound is displayed as "Redstone Wire clicks" in subtitle.

## Configuration

Pitch and volume of sounds can be changed by using a command added by this mod, or you can edit the config file manually.
There's another option to apply a random offset to the base pitch.

### Command

Command `/repeatersound` allows you to edit those options in-game, there are three arguments you can use in this command:

- `/repeatersound setBasePitch <pitch>` is used for the adjustment of the base pitch that will be applied, `<pitch>` requires a `float` number
- `/repeatersound useRandomPitch <random>` is used to set if the random offset will be applied to the base pitch, `<random>` requires a `boolean` value
- `/repeatersound setVolume <volume>` is used to set the volume of sounds, `volume` requires a `float` number

### Config File

After the first initialize of the mod, a config file `repeatersound.json5` will be created in the game's `config` directory. You can edit this file manually if you want.
