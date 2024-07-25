package me.ayydxn.worldsaver;

import me.ayydxn.worldsaver.events.server.WorldSaveServerEvents;
import me.ayydxn.worldsaver.server.WorldsToSave;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class WorldSaverServerMod implements DedicatedServerModInitializer
{
    private static WorldSaverServerMod INSTANCE;

    private WorldsToSave worldsToSave;

    @Override
    public void onInitializeServer()
    {
        INSTANCE = this;

        this.worldsToSave = WorldsToSave.load();

        WorldSaveServerEvents.SERVER_CLOSE.register(server -> WorldSaverCommonMod.getInstance().getWorldPackager().uploadPackagedWorlds());
    }

    public static WorldSaverServerMod getInstance()
    {
        if (INSTANCE == null)
            throw new IllegalStateException("Tried to access an instance of World Saver on a dedicated server before one was available!");

        return INSTANCE;
    }

    public WorldsToSave getWorldsToSave()
    {
        return this.worldsToSave;
    }
}
