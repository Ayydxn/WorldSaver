package me.ayydxn.worldsaver.events.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public final class WorldSaveServerEvents
{
    public static final Event<ServerClose> SERVER_CLOSE = EventFactory.createArrayBacked(ServerClose.class, listeners -> server ->
    {
        for (ServerClose serverCloseListener : listeners)
            serverCloseListener.onServerClose(server);
    });

    @FunctionalInterface
    public interface ServerClose
    {
        void onServerClose(MinecraftServer server);
    }
}
