package me.ayydxn.worldsaver;

import me.ayydxn.worldsaver.utils.WorldSaverConstants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class WorldSaverCommonMod implements ModInitializer
{
    private static final Logger LOGGER = (Logger) LogManager.getLogger("World Saver");

    @Override
    public void onInitialize()
    {
        LOGGER.info("Initializing World Saver... (Version: {})", FabricLoader.getInstance().getModContainer(WorldSaverConstants.MOD_ID).orElseThrow()
                .getMetadata().getVersion().getFriendlyString());
    }
}
