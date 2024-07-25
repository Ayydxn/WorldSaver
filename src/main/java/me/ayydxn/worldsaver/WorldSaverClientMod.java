package me.ayydxn.worldsaver;

import me.ayydxn.worldsaver.events.client.WorldSaveClientEvents;
import me.ayydxn.worldsaver.world.WorldPackager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class WorldSaverClientMod implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        WorldSaveClientEvents.CLIENT_CLOSE.register(client -> WorldSaverCommonMod.getInstance().getWorldPackager().uploadPackagedWorlds());
    }
}
