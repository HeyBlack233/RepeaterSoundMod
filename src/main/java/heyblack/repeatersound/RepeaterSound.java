package heyblack.repeatersound;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import heyblack.repeatersound.config.Config;
import heyblack.repeatersound.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class RepeaterSound implements ClientModInitializer
{

    public static final Identifier REPEATER_CLICK = new Identifier("repeatersound:repeater_click");
    public static SoundEvent BLOCK_REPEATER_CLICK = new SoundEvent(REPEATER_CLICK);

    public static final Identifier REDSTONE_WIRE_CLICK = new Identifier("repeatersound:redstone_wire_click");
    public static SoundEvent BLOCK_REDSTONE_WIRE_CLICK = new SoundEvent(REDSTONE_WIRE_CLICK);

    public static final Identifier DAYLIGHT_DETECTOR_CLICK = new Identifier("repeatersound:daylight_detector_click");
    public static SoundEvent BLOCK_DAYLIGHT_DETECTOR_CLICK = new SoundEvent(DAYLIGHT_DETECTOR_CLICK);

    ConfigManager cfgManager = ConfigManager.getInstance();
    static Config config;

    @Override
    public void onInitializeClient()
    {
        config = cfgManager.loadConfig();

        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("repeatersound")
                        .then(ClientCommandManager.literal("setBasePitch")
                                .then(ClientCommandManager.argument("pitch", FloatArgumentType.floatArg())
                                        .executes(ctx -> saveConfigPitch(FloatArgumentType.getFloat(ctx, "pitch")))))
                        .then(ClientCommandManager.literal("useRandomPitch")
                                .then(ClientCommandManager.argument("useRandom", BoolArgumentType.bool())
                                        .executes(ctx -> saveConfigRandom(BoolArgumentType.getBool(ctx, "useRandom")))))
                        .then(ClientCommandManager.literal("setVolume")
                                .then(ClientCommandManager.argument("volume", FloatArgumentType.floatArg())
                                        .executes(ctx -> saveConfigVolume(FloatArgumentType.getFloat(ctx, "volume"))))));
    }

    public static Config getConfig()
    {
        return config;
    }

    public int saveConfigPitch(float pitch)
    {
        config.basePitch = pitch;
        cfgManager.save(config);

        return 1;
    }

    public int saveConfigRandom(boolean random)
    {
        config.useRandomPitch = random;
        cfgManager.save(config);

        return 1;
    }

    public int saveConfigVolume(float volume)
    {
        config.volume = volume;
        cfgManager.save(config);

        return 1;
    }
}