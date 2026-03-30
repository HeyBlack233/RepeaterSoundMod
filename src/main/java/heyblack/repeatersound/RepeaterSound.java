package heyblack.repeatersound;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import heyblack.repeatersound.config.ConfigManager;
import heyblack.repeatersound.config.ConfigOption;
import heyblack.repeatersound.util.InteractionMode;
import heyblack.repeatersound.util.ServerCloseCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RepeaterSound implements ClientModInitializer
{
    public static final String MOD_ID = "repeatersound";
    public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

    public static final SoundEvent BLOCK_REPEATER_CLICK = register("repeatersound:repeater_click");
    public static final SoundEvent BLOCK_REDSTONE_WIRE_CLICK = register("repeatersound:redstone_wire_click");
    public static final SoundEvent BLOCK_DAYLIGHT_DETECTOR_CLICK = register("repeatersound:daylight_detector_click");
    public static final SoundEvent CLICK_ALARM = register("repeatersound:click_alarm");

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient()
    {
        ConfigManager cfg = ConfigManager.getInstance();

        ClientCommandManager.DISPATCHER.register(
                ClientCommandManager.literal("repeatersound")
                        .then(ClientCommandManager.literal("setBasePitch")
                                .then(ClientCommandManager.argument(ConfigOption.BASE_PITCH.id, FloatArgumentType.floatArg())
                                        .executes(ctx -> cfg.setConfigCommand(
                                                ConfigOption.BASE_PITCH.id,
                                                String.valueOf(FloatArgumentType.getFloat(ctx, ConfigOption.BASE_PITCH.id)),
                                                ctx.getSource().getPlayer()
                                        ))))

                        .then(ClientCommandManager.literal("useRandomPitch")
                                .then(ClientCommandManager.argument(ConfigOption.USE_RANDOM.id, BoolArgumentType.bool())
                                        .executes(ctx -> cfg.setConfigCommand(
                                                ConfigOption.USE_RANDOM.id,
                                                String.valueOf(BoolArgumentType.getBool(ctx, ConfigOption.USE_RANDOM.id)),
                                                ctx.getSource().getPlayer()
                                        ))))

                        .then(ClientCommandManager.literal("setVolume")
                                .then(ClientCommandManager.argument(ConfigOption.VOLUME.id, FloatArgumentType.floatArg())
                                        .executes(ctx -> cfg.setConfigCommand(
                                                ConfigOption.VOLUME.id,
                                                String.valueOf(FloatArgumentType.getFloat(ctx, ConfigOption.VOLUME.id)),
                                                ctx.getSource().getPlayer()
                                        ))))

                        .then(ClientCommandManager.literal("interactionMode")
                                .then(ClientCommandManager.argument(ConfigOption.INTERACTION_MODE.id, StringArgumentType.string())
                                        .suggests(
                                                (ctx, builder) ->
                                                {
                                                    for (InteractionMode type : InteractionMode.values())
                                                    {
                                                        builder.suggest(type.id);
                                                    }

                                                    return builder.buildFuture();
                                                }
                                        )
                                        .executes(ctx -> cfg.setConfigCommand(
                                                ConfigOption.INTERACTION_MODE.id,
                                                StringArgumentType.getString(ctx, ConfigOption.INTERACTION_MODE.id),
                                                ctx.getSource().getPlayer()
                                        ))))

                        .then(ClientCommandManager.literal("alarmMessage")
                                .then(ClientCommandManager.argument(ConfigOption.ALARM_MESSAGE.id, StringArgumentType.string())
                                        .executes(ctx -> cfg.setConfigCommand(
                                                ConfigOption.ALARM_MESSAGE.id,
                                                String.valueOf(StringArgumentType.getString(ctx, ConfigOption.ALARM_MESSAGE.id)),
                                                ctx.getSource().getPlayer()
                                        ))))

                        .then(ClientCommandManager.literal("disabledMessage")
                                .then(ClientCommandManager.argument(ConfigOption.DISABLED_MESSAGE.id, StringArgumentType.string())
                                        .executes(ctx -> cfg.setConfigCommand(
                                                ConfigOption.DISABLED_MESSAGE.id,
                                                String.valueOf(StringArgumentType.getString(ctx, ConfigOption.DISABLED_MESSAGE.id)),
                                                ctx.getSource().getPlayer()
                                        ))))
        );

        ServerCloseCallback.EVENT.register(cfg);
    }

    private static SoundEvent register(String id) {
        return (SoundEvent)Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new Identifier(id)));
    }

    public static void info(String s) {
        LOGGER.info("[RepeaterSound] " + s);
    }

    public static void warn(String s) {
        LOGGER.warn("[RepeaterSound] " + s);
    }

    public static void error(String s) {
        LOGGER.error("[RepeaterSound] " + s);
    }
}