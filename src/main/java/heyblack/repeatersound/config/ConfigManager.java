package heyblack.repeatersound.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.prismwork.prismconfig.api.PrismConfig;
import io.github.prismwork.prismconfig.api.config.DefaultDeserializers;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigManager
{
    Config cfg = new Config();
    static Gson gson = new Gson();
    public File cfgFile = FabricLoader.getInstance().getConfigDir().resolve("repeatersound.json5").toFile();

    private static ConfigManager instance = new ConfigManager();
    public static ConfigManager getInstance()
    {
        return instance;
    }

    public void loadConfig()
    {
        if(!cfgFile.exists())
        {
            try
            {
                cfgFile.createNewFile();
                cfg.setDefault();
                PrismConfig.getInstance().deserializeAndWrite(Config.class, cfg, DefaultDeserializers.getInstance().json5(Config.class), cfgFile);
                return;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        cfg = getConfigFromFile();
    }

    public void save(Config cfg)
    {
        PrismConfig.getInstance().deserializeAndWrite(Config.class, cfg, DefaultDeserializers.getInstance().json5(Config.class), cfgFile);
    }

    public Config getConfigFromFile()
    {
        try
        {
            JsonReader reader = new JsonReader(new FileReader(cfgFile));
            return gson.fromJson(reader, Config.class);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}