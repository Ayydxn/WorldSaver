package me.ayydxn.worldsaver.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

/**
 * Events which represent different ways that could trigger World Saver to save a world to Google Drive.
 */
public final class WorldSaveEvents
{
    /**
     * Called when the server shutdowns and done saving the world. This is called when the game or server crashes.
     */
    public static final Event<ExitWorld> EXIT_WORLD = EventFactory.createArrayBacked(ExitWorld.class, (listeners) -> (server) ->
    {
        for (ExitWorld exitWorldListener : listeners)
            exitWorldListener.onWorldExit(server);
    });

    /**
     * Called when World Saver performs an auto-save, if the setting within the mod is enabled.
     */
    public static final Event<AutoSave> AUTO_SAVE = EventFactory.createArrayBacked(AutoSave.class, (listeners) -> (server) ->
    {
        for (AutoSave autoSaveListener : listeners)
            autoSaveListener.onAutoSave(server);
    });

    @FunctionalInterface
    public interface ExitWorld
    {
        void onWorldExit(MinecraftServer server);
    }

    @FunctionalInterface
    public interface AutoSave
    {
        void onAutoSave(MinecraftServer server);
    }
}
