package heyblack.repeatersound;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import heyblack.repeatersound.config.Config;
import heyblack.repeatersound.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated) -> dispatcher.register(literal("repeatersound")
                        .then(literal("setBasePitch")
                                .then(argument("pitch", FloatArgumentType.floatArg())
                                        .executes(ctx -> saveConfigPitch(ctx.getSource(), FloatArgumentType.getFloat(ctx, "pitch")))))
                        .then(literal("useRandomPitch")
                                .then(argument("useRandom", BoolArgumentType.bool())
                                        .executes(ctx -> saveConfigRandom(ctx.getSource(), BoolArgumentType.getBool(ctx, "useRandom")))))));
    }

    public int saveConfigPitch(ServerCommandSource source, float pitch)
    {
        Config cfg = cfgManager.getConfigFromFile();
        cfg.setBasePitch(pitch);
        logger.info("basePitch is now set to: " + cfg.getBasePitch());
        cfgManager.save(cfg);

        return 1;
    }

    public int saveConfigRandom(ServerCommandSource source, boolean random)
    {
        Config cfg = cfgManager.getConfigFromFile();
        cfg.setRandomPitch(random);
        logger.info("useRandomPitch is now set to: " + cfg.getRandomPitch());
        cfgManager.save(cfg);

        return 1;
    }
}