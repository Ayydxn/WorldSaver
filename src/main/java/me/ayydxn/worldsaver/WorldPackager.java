package me.ayydxn.worldsaver;

import me.ayydxn.worldsaver.google.GoogleDriveAPI;
import me.ayydxn.worldsaver.google.GoogleDriveFileManager;
import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import net.lingala.zip4j.ZipFile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class WorldPackager
{
    private static final Path WORLD_SAVER_SAVES_DIRECTORY = Path.of(WorldSaverConstants.WORLD_SAVER_DIRECTORY + "/world-saves");

    public static void packageWorld(MinecraftServer server)
    {
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
        String serverWorldName = serverWorldPath.getParent().getFileName().toString();
        Path worldSaveDirectory = Path.of(WORLD_SAVER_SAVES_DIRECTORY + "/" + serverWorldName);

        if (!Files.exists(worldSaveDirectory))
        {
            try
            {
                Files.createDirectories(worldSaveDirectory);
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        String worldSaveName = String.format("%s-%d.%d.%d-%d.zip", serverWorldName, localDateTime.getMonth().getValue(), localDateTime.getDayOfMonth(),
                localDateTime.getYear(), System.currentTimeMillis() / 1000L);

        try (ZipFile zipFile = new ZipFile(worldSaveDirectory + "/" + worldSaveName))
        {
            zipFile.addFolder(serverWorldPath.toFile());
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public static void uploadPackagedWorlds() throws IOException
    {
        GoogleDriveFileManager googleDriveFileManager = GoogleDriveAPI.getInstance().getGoogleDriveFileManager();

        for (Path worldSaveFolder : Files.list(WORLD_SAVER_SAVES_DIRECTORY).toList())
        {
            String worldSavesFolderID = googleDriveFileManager.createNestedFolder(GoogleDriveAPI.getInstance().getWorldSaverFolderID(),
                    worldSaveFolder.toFile().getName());

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
}
