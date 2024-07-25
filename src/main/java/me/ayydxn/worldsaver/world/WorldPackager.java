package me.ayydxn.worldsaver.world;

import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class WorldPackager
{
    protected static final Path WORLD_SAVER_SAVES_DIRECTORY = Path.of(WorldSaverConstants.WORLD_SAVER_DIRECTORY + "/world-saves");

    public static WorldPackager createInstance()
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

        return switch (FabricLoader.getInstance().getEnvironmentType())
        {
            case CLIENT -> new SingleplayerWorldPackager();
            case SERVER -> new DedicatedServerWorldPackager();
        };
    }

    public abstract void packageWorld(MinecraftServer server);

    public abstract void uploadPackagedWorlds();
}
