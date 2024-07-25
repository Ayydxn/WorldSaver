package me.ayydxn.worldsaver;

import me.ayydxn.worldsaver.config.WorldSaverGameOptions;
import me.ayydxn.worldsaver.events.WorldSaveEvents;
import me.ayydxn.worldsaver.google.GoogleDriveAPI;
import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import me.ayydxn.worldsaver.world.WorldPackager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class WorldSaverCommonMod implements ModInitializer
{
    private static WorldSaverCommonMod INSTANCE;

    private static final Logger LOGGER = (Logger) LogManager.getLogger("World Saver");

    private WorldSaverGameOptions gameOptions;
    private WorldPackager worldPackager;

    @Override
    public void onInitialize()
    {
        INSTANCE = this;

        String worldSaverVersion = FabricLoader.getInstance().getModContainer(WorldSaverConstants.MOD_ID).orElseThrow()
                .getMetadata().getVersion().getFriendlyString();

        LOGGER.info("Initializing World Saver... (Version: {}, Environment: {})", worldSaverVersion, FabricLoader.getInstance().getEnvironmentType());

        this.worldPackager = WorldPackager.createInstance();

        this.gameOptions = WorldSaverGameOptions.load();

        GoogleDriveAPI.initialize();

        WorldSaveEvents.EXIT_WORLD.register(this.worldPackager::packageWorld);
        WorldSaveEvents.AUTO_SAVE.register(this.worldPackager::packageWorld);
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

    public WorldPackager getWorldPackager()
    {
        return this.worldPackager;
    }
}
