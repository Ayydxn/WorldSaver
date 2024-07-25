package me.ayydxn.worldsaver.server;

import com.google.api.client.util.Lists;
import com.google.gson.JsonSyntaxException;
import me.ayydxn.worldsaver.WorldSaverCommonMod;
import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class WorldsToSave
{
    private static final Path WORLDS_TO_SAVE_FILE = FabricLoader.getInstance().getGameDir().resolve("worlds-to-save.json");

    public List<String> worlds = this.getDefaultWorldsToSave();

    public static WorldsToSave defaults()
    {
        return new WorldsToSave();
    }

    public static WorldsToSave load()
    {
        if (Files.exists(WORLDS_TO_SAVE_FILE))
        {
            StringBuilder worldsToSaveFileContents = new StringBuilder();

            try
            {
                worldsToSaveFileContents.append(FileUtils.readFileToString(WORLDS_TO_SAVE_FILE.toFile(), StandardCharsets.UTF_8));
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }

            WorldsToSave worldsToSave = null;

            try
            {
                worldsToSave = WorldSaverConstants.GSON.fromJson(worldsToSaveFileContents.toString(), WorldsToSave.class);
            }
            catch (JsonSyntaxException exception)
            {
                exception.printStackTrace();
            }

            return worldsToSave;
        }
        else
        {
            WorldSaverCommonMod.getLogger().warn("Failed to load the worlds to save! Loading defaults...");

            WorldsToSave defaultWorldsToSave = WorldsToSave.defaults();

            try
            {
                FileUtils.writeStringToFile(WORLDS_TO_SAVE_FILE.toFile(), WorldSaverConstants.GSON.toJson(defaultWorldsToSave), StandardCharsets.UTF_8);
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }

            return defaultWorldsToSave;
        }
    }

    private List<String> getDefaultWorldsToSave()
    {
        Path serverPropertiesFile = FabricLoader.getInstance().getGameDir().resolve("server.properties");
        if (!Files.exists(serverPropertiesFile))
            return Lists.newArrayList();

        Properties serverProperties = new Properties();

        try
        {
            serverProperties.load(new FileReader(serverPropertiesFile.toFile()));
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        List<String> defaultWorlds = Lists.newArrayList();
        defaultWorlds.add(serverProperties.getProperty("level-name"));

        return defaultWorlds;
    }
}
