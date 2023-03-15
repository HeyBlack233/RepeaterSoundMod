# Repeater Sound Mod

## Description

RepeaterSound is a simple client side mod, the mod added sound events when some blocks are clicked.

Currently supports repeater and redstone wire.

Requires [Fabric API](https://modrinth.com/mod/fabric-api) for 1.19.3 and above

### Repeater

Repeater will play a sound when it got clicked(delay changed), this sound is displayed as "Repeater clicked" in subtitle.

### Redstone Wire

Redstone wire will play a sound when it got clicked and the shape is changed between dot and cross, this sound is displayed as "Redstone Wire clicked" in subtitle.

## Configuration

The pitch of repeater and redstone wire sound can be changed by using a command added by this mod, or you can edit the config file manually.
There's another option to apply a random offset to the base pitch.

### Command

Command `/repeatersound` allows you to edit those options in-game, there are two arguments you can use in this command:
- `/repeatersound setBasePitch <pitch>` is used for the adjustment of the base pitch that will be applied on both repeater and redstone wire, `<pitch>` requires a `float` number
- `/repeatersound useRandomPitch <random>` is used to set if the random offset will be applied to the base pitch, `<random>` requires a `boolean` value

### Config File

After the first initialize of the mod, a config file `repeatersound.json5` will be created in the game's `config` directory. You can edit this file manually if you want.
