package me.ayydxn.worldsaver;

import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import net.lingala.zip4j.ZipFile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorldPackager
{
    public static void packageWorld(MinecraftServer server)
    {
        Path worldSaverSavesDirectory = Path.of(WorldSaverConstants.WORLD_SAVER_DIRECTORY + "/packaged-worlds");
        if (!Files.exists(worldSaverSavesDirectory))
        {
            try
            {
                Files.createDirectories(worldSaverSavesDirectory);
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

        Path serverWorldPath = server.getSavePath(WorldSavePath.ROOT);
        String serverWorldName = serverWorldPath.getParent().getFileName().toString();

        try (ZipFile zipFile = new ZipFile(worldSaverSavesDirectory + "/" + serverWorldName + ".zip"))
        {
            zipFile.addFolder(serverWorldPath.toFile());
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
