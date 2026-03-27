package heyblack.repeatersound.config;

import heyblack.repeatersound.util.InteractionMode;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class ConfigScreen {
    private static final ConfigManager CONFIG_MANAGER = ConfigManager.getInstance();

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("repeatersound.config.title"));

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("repeatersound.config.main"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startFloatField(
                new TranslatableText("repeatersound.config.option.base_pitch"),
                Float.parseFloat(CONFIG_MANAGER.getConfig(ConfigOption.BASE_PITCH.id))
        )
        .setDefaultValue(Float.valueOf(ConfigOption.BASE_PITCH.defaultValue))
        .setSaveConsumer(newValue -> CONFIG_MANAGER.setConfigScreen(ConfigOption.BASE_PITCH.id, String.valueOf(newValue)))
        .build());

        general.addEntry(entryBuilder.startFloatField(
                new TranslatableText("repeatersound.config.option.volume"),
                Float.parseFloat(CONFIG_MANAGER.getConfig(ConfigOption.VOLUME.id))
        )
        .setDefaultValue(Float.valueOf(ConfigOption.VOLUME.defaultValue))
        .setSaveConsumer(newValue -> CONFIG_MANAGER.setConfigScreen(ConfigOption.VOLUME.id, String.valueOf(newValue)))
        .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                new TranslatableText("repeatersound.config.option.use_random"),
                Boolean.parseBoolean(CONFIG_MANAGER.getConfig(ConfigOption.USE_RANDOM.id))
        )
        .setDefaultValue(Boolean.parseBoolean(ConfigOption.USE_RANDOM.defaultValue))
        .setSaveConsumer(newValue -> CONFIG_MANAGER.setConfigScreen(ConfigOption.USE_RANDOM.id, String.valueOf(newValue)))
        .build());

        general.addEntry(entryBuilder.startEnumSelector(
                new TranslatableText("repeatersound.config.option.interaction_mode"),
                InteractionMode.class,
                InteractionMode.valueOf(CONFIG_MANAGER.getConfig(ConfigOption.INTERACTION_MODE.id))
        )
        .setDefaultValue(InteractionMode.valueOf(ConfigOption.INTERACTION_MODE.defaultValue))
        .setSaveConsumer(newValue -> CONFIG_MANAGER.setConfigScreen(ConfigOption.INTERACTION_MODE.id, String.valueOf(newValue)))
        .build());

        general.addEntry(entryBuilder.startStrField(
                new TranslatableText("repeatersound.config.option.alarm_message"),
                CONFIG_MANAGER.getConfig(ConfigOption.ALARM_MESSAGE.id)
        )
        .setDefaultValue(ConfigOption.ALARM_MESSAGE.defaultValue)
        .setSaveConsumer(newValue -> CONFIG_MANAGER.setConfigScreen(ConfigOption.ALARM_MESSAGE.id, newValue))
        .build());

        general.addEntry(entryBuilder.startStrField(
                        new TranslatableText("repeatersound.config.option.disabled_message"),
                        CONFIG_MANAGER.getConfig(ConfigOption.DISABLED_MESSAGE.id)
                )
                .setDefaultValue(ConfigOption.DISABLED_MESSAGE.defaultValue)
                .setSaveConsumer(newValue -> CONFIG_MANAGER.setConfigScreen(ConfigOption.DISABLED_MESSAGE.id, newValue))
                .build());

        return builder.build();
    }
}
