package heyblack.repeatersound.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class ConfigUpdater
{
    static Logger logger = LogManager.getLogger();

    static Path dir = FabricLoader.getInstance().getConfigDir();

    private static final Path CONFIG_OLD = dir.resolve("repeatersound.json5");
    private static final String CONFIG_DIR = dir.toString();
    private static final Pattern CONFIG_PATTERN = Pattern.compile("repeatersound\\d+\\.\\d+\\.\\d+\\.json5");

    public static Path findConfigFile()
    {
        try
        {
            return StreamSupport.stream(Files.newDirectoryStream(Paths.get(CONFIG_DIR)).spliterator(), false)
                    .filter(path -> CONFIG_PATTERN.matcher(path.getFileName().toString()).matches())
                    .findFirst()
                    .orElse(null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> update()
    {
        Map<String, String> cfg = new HashMap<>();

        // version <= 1.2.0
        if (Files.exists(CONFIG_OLD))
        {
            try
            {
                logger.info("[RepeaterSound] Updating outdated config file (version below 1.3.0)");
                JsonReader reader = new JsonReader(new FileReader(CONFIG_OLD.toFile()));
                Config cfg_old = new Gson().fromJson(reader, Config.class);
                String pitch = String.valueOf(cfg_old.getBasePitch());
                String volume = String.valueOf(cfg_old.getVolume());
                String random = String.valueOf(cfg_old.getRandomPitch());
                cfg.put("basePitch", pitch);
                cfg.put("volume", volume);
                cfg.put("useRandomPitch", random);

                reader.close();
                Files.delete(CONFIG_OLD);

                return cfg;
            }
            catch (IOException e)
            {
                logger.error("[RepeaterSound] Error occurred when updating config file!");
                throw new RuntimeException(e);
            }
        }

//        // version >= 1.3.0
//        Path path = findConfigFile();
//        if (path != null)
//        {
//
//        }

        return cfg;
    }
}
