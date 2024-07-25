package me.ayydxn.worldsaver;

import me.ayydxn.worldsaver.config.WorldSaverGameOptions;
import me.ayydxn.worldsaver.events.WorldSaveEvents;
import me.ayydxn.worldsaver.google.GoogleDriveAPI;
import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.IOException;

public class WorldSaverCommonMod implements ModInitializer
{
    private static WorldSaverCommonMod INSTANCE;

    private static final Logger LOGGER = (Logger) LogManager.getLogger("World Saver");

    private WorldSaverGameOptions gameOptions;

    @Override
    public void onInitialize()
    {
        INSTANCE = this;

        LOGGER.info("Initializing World Saver... (Version: {})", FabricLoader.getInstance().getModContainer(WorldSaverConstants.MOD_ID).orElseThrow()
                .getMetadata().getVersion().getFriendlyString());

        this.gameOptions = WorldSaverGameOptions.load();

        GoogleDriveAPI.initialize();

        WorldSaveEvents.EXIT_WORLD.register(WorldPackager::packageWorld);
        WorldSaveEvents.AUTO_SAVE.register(WorldPackager::packageWorld);
        WorldSaveEvents.CLIENT_CLOSE.register(client ->
        {
            try
            {
                WorldPackager.uploadPackagedWorlds();
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        });
    }

    public static WorldSaverCommonMod getInstance()
    {
        if (INSTANCE == null)
            throw new IllegalStateException("Tried to access an instance of World Saver before one was available!");

        return INSTANCE;
    }

    public static Logger getLogger()
    {
        return LOGGER;
    }

    public WorldSaverGameOptions getGameOptions()
    {
        return this.gameOptions;
    }
}
