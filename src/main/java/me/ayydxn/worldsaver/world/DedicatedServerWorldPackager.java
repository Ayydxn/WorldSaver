package me.ayydxn.worldsaver.world;

import me.ayydxn.worldsaver.WorldSaverCommonMod;
import me.ayydxn.worldsaver.WorldSaverServerMod;
import me.ayydxn.worldsaver.google.GoogleDriveAPI;
import me.ayydxn.worldsaver.google.GoogleDriveFileManager;
import me.ayydxn.worldsaver.server.WorldsToSave;
import net.lingala.zip4j.ZipFile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class DedicatedServerWorldPackager extends WorldPackager
{
    @Override
    public void packageWorld(MinecraftServer server)
    {
        WorldsToSave worldsToSave = WorldSaverServerMod.getInstance().getWorldsToSave();

        if (!Files.exists(WORLD_SAVER_SAVES_DIRECTORY))
        {
            try
            {
                Files.createDirectories(WORLD_SAVER_SAVES_DIRECTORY);
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

        Path serverWorldPath = server.getSavePath(WorldSavePath.ROOT);

        for (String worldToSave : worldsToSave.worlds)
        {
            Path serverWorldSaveDirectory = Path.of(WORLD_SAVER_SAVES_DIRECTORY + "/" + worldToSave);

            if (!Files.exists(serverWorldSaveDirectory))
            {
                try
                {
                    Files.createDirectories(serverWorldSaveDirectory);
                }
                catch (IOException exception)
                {
                    exception.printStackTrace();
                }
            }

            LocalDateTime localDateTime = LocalDateTime.now();
            String worldSaveName = String.format("%s-%d.%d.%d-%d.zip", worldToSave, localDateTime.getMonth().getValue(), localDateTime.getDayOfMonth(),
                    localDateTime.getYear(), System.currentTimeMillis() / 1000L);

            try (ZipFile zipFile = new ZipFile(serverWorldSaveDirectory + "/" + worldSaveName))
            {
                zipFile.addFolder(serverWorldPath.toFile());
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void uploadPackagedWorlds()
    {
        GoogleDriveFileManager googleDriveFileManager = GoogleDriveAPI.getInstance().getGoogleDriveFileManager();

        try
        {
            for (Path worldSaveFolder : Files.list(WORLD_SAVER_SAVES_DIRECTORY).toList())
            {
                String worldSavesFolderID = googleDriveFileManager.createNestedFolder(GoogleDriveAPI.getInstance().getWorldSaverFolderID(),
                        worldSaveFolder.toFile().getName() + " (Dedicated Server)");

                for (Path worldSave : Files.list(worldSaveFolder).toList())
                {
                    if (!FilenameUtils.getExtension(worldSave.toFile().getName()).equals("zip"))
                    {
                        WorldSaverCommonMod.getLogger().warn("'{}' isn't a world save! Skipping it and not uploading it...", worldSave.toFile().getName());
                        continue;
                    }

                    googleDriveFileManager.uploadFile(worldSavesFolderID, "application/zip", worldSave.toFile());
                }
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
