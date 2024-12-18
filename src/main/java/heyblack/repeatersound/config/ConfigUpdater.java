package heyblack.repeatersound.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import heyblack.repeatersound.RepeaterSound;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class ConfigUpdater
{
    private static final Path DIR = FabricLoader.getInstance().getConfigDir();

    private static final Path CONFIG_OLD = DIR.resolve("repeatersound.json5");
    private static final String CONFIG_DIR = DIR.toString();
    private static final Pattern CONFIG_PATTERN = Pattern.compile("repeatersound\\d+\\.\\d+\\.\\d+\\.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Path findConfigFile()
    {
        try
        {
            Path cfgPath = StreamSupport.stream(Files.newDirectoryStream(Paths.get(CONFIG_DIR)).spliterator(), false)
                    .filter(path -> CONFIG_PATTERN.matcher(path.getFileName().toString()).matches())
                    .findFirst()
                    .orElse(null);

            if(cfgPath != null)
                RepeaterSound.info("Found old config file, updating to current version");
            else
                RepeaterSound.info("Missing config file, creating a new one");

            return cfgPath;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            RepeaterSound.error("Error occurred when getting config file!");
            return null;
        }
    }

    public static Map<String, String> update()
    {
        Map<String, String> cfg = new LinkedHashMap<>();
        JsonReader reader;

        // version <= 1.2.0
        if (Files.exists(CONFIG_OLD))
        {
            try
            {
                RepeaterSound.info("Updating outdated config file (version below 1.3.0)");
                reader = new JsonReader(new FileReader(CONFIG_OLD.toFile()));
                Config cfg_old = GSON.fromJson(reader, Config.class);
                String pitch = String.valueOf(cfg_old.getBasePitch());
                String volume = String.valueOf(cfg_old.getVolume());
                String random = String.valueOf(cfg_old.getRandomPitch());
                cfg.put(ConfigOption.BASE_PITCH.id, pitch);
                cfg.put(ConfigOption.VOLUME.id, volume);
                cfg.put(ConfigOption.USE_RANDOM.id, random);

                reader.close();
                Files.delete(CONFIG_OLD);

                return cfg;
            }
            catch (IOException e)
            {
                RepeaterSound.error("Error occurred when updating config file!");
                throw new RuntimeException(e);
            }
        }

        Path path = findConfigFile();
        if (path != null) // version from 1.3.0 to 1.5.0
        {
            RepeaterSound.info("Updating config file from version 1.3.0 - 1.5.0");
            try
            {
                reader = new JsonReader(new FileReader(path.toFile()));
                cfg = GSON.fromJson(reader, Map.class);

                Map<String, String> intermediate = new HashMap<>();

                for (String option : cfg.keySet()) {
                    switch (option) {
                        case "basePitch":
                            intermediate.put(ConfigOption.BASE_PITCH.id, cfg.get(option));
                            break;
                        case "volume":
                            intermediate.put(ConfigOption.VOLUME.id, cfg.get(option));
                            break;
                        case "useRandom":
                            intermediate.put(ConfigOption.USE_RANDOM.id, cfg.get(option));
                            break;
                        case "interactionMode":
                            intermediate.put(ConfigOption.INTERACTION_MODE.id, cfg.get(option));
                            break;
                        case "alarmMessage":
                            intermediate.put(ConfigOption.ALARM_MESSAGE.id, cfg.get(option));
                            break;
                        case "disabledMessage":
                            intermediate.put(ConfigOption.DISABLED_MESSAGE.id, cfg.get(option));
                            break;
                    }
                }

                Map<String, String> newCfg = new LinkedHashMap<>();

                for (ConfigOption option : ConfigOption.values()) {
                    Optional<String> value = Optional.ofNullable(intermediate.get(option.id));
                    newCfg.put(option.id, value.orElse(option.defaultValue));
                }

                reader.close();
                Files.delete(path);
                return newCfg;
            }
            catch (IOException e)
            {
                RepeaterSound.error("Error occurred when updating config file!");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else // version >= 1.6.0
        {
            // not yet
        }

        return cfg;
    }
}
