package me.ayydxn.worldsaver.config;

import com.google.gson.JsonSyntaxException;
import me.ayydxn.worldsaver.WorldSaverCommonMod;
import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class WorldSaverGameOptions
{
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("world-saver-settings.json");

    public boolean enableWorldSaving = true;
    public boolean enableAutosave = true;
    public long autosaveInterval = TimeUnit.MINUTES.toSeconds(5L);

    public static WorldSaverGameOptions defaults()
    {
        return new WorldSaverGameOptions();
    }

    public static WorldSaverGameOptions load()
    {
        if (Files.exists(CONFIG_FILE))
        {
            StringBuilder configFileContents = new StringBuilder();

            try
            {
                configFileContents.append(FileUtils.readFileToString(CONFIG_FILE.toFile(), StandardCharsets.UTF_8));
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }

            WorldSaverGameOptions worldSaverGameOptions = null;

            try
            {
                worldSaverGameOptions = WorldSaverConstants.GSON.fromJson(configFileContents.toString(), WorldSaverGameOptions.class);
            }
            catch (JsonSyntaxException exception)
            {
                exception.printStackTrace();
            }

            return worldSaverGameOptions;
        }
        else
        {
            WorldSaverCommonMod.getLogger().warn("Failed to load World Saver's options! Loading defaults...");

            WorldSaverGameOptions defaultWorldSaverGameOptions = WorldSaverGameOptions.defaults();
            defaultWorldSaverGameOptions.save();

            return defaultWorldSaverGameOptions;
        }
    }

    public void save()
    {
        try
        {
            FileUtils.writeStringToFile(CONFIG_FILE.toFile(), WorldSaverConstants.GSON.toJson(this), StandardCharsets.UTF_8);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
