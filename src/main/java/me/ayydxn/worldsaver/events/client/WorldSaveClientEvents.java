package me.ayydxn.worldsaver.events.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public final class WorldSaveClientEvents
{
    /**
     * Called when the Minecraft client is closed. I know a similar event already exists within the Fabric API, but it doesn't run after the integrated server
     * stops which is where I need it to.
     */
    public static final Event<ClientClose> CLIENT_CLOSE = EventFactory.createArrayBacked(ClientClose.class, (listeners) -> (client) ->
    {
        for (ClientClose clientCloseListener : listeners)
            clientCloseListener.onClientClose(client);
    });

    @FunctionalInterface
    public interface ClientClose
    {
        void onClientClose(MinecraftClient client);
    }
}
