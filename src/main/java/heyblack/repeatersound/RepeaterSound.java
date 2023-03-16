package heyblack.repeatersound;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import heyblack.repeatersound.config.Config;
import heyblack.repeatersound.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RepeaterSound implements ClientModInitializer
{
    public static final Logger logger = LogManager.getLogger();

    public static final Identifier REPEATER_CLICK = new Identifier("repeatersound:repeater_click");
    public static SoundEvent BLOCK_REPEATER_CLICK = new SoundEvent(REPEATER_CLICK);

    public static final Identifier REDSTONE_WIRE_CLICK = new Identifier("repeatersound:redstone_wire_click");
    public static SoundEvent BLOCK_REDSTONE_WIRE_CLICK = new SoundEvent(REDSTONE_WIRE_CLICK);

    ConfigManager cfgManager = ConfigManager.getInstance();

    @Override
    public void onInitializeClient()
    {
        cfgManager.loadConfig();

        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("repeatersound")
                        .then(ClientCommandManager.literal("setBasePitch")
                                .then(ClientCommandManager.argument("pitch", FloatArgumentType.floatArg())
                                        .executes(ctx -> saveConfigPitch(FloatArgumentType.getFloat(ctx, "pitch")))))
                        .then(ClientCommandManager.literal("useRandomPitch")
                                .then(ClientCommandManager.argument("useRandom", BoolArgumentType.bool())
                                        .executes(ctx -> saveConfigRandom(BoolArgumentType.getBool(ctx, "useRandom"))))));
    }

    public int saveConfigPitch(float pitch)
    {
        Config cfg = cfgManager.getConfigFromFile();
        cfg.setBasePitch(pitch);
        logger.info("basePitch is now set to: " + cfg.getBasePitch());
        cfgManager.save(cfg);

        return 1;
    }

    public int saveConfigRandom(boolean random)
    {
        Config cfg = cfgManager.getConfigFromFile();
        cfg.setRandomPitch(random);
        logger.info("useRandomPitch is now set to: " + cfg.getRandomPitch());
        cfgManager.save(cfg);

        return 1;
    }
}