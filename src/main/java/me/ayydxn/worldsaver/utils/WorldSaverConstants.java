package me.ayydxn.worldsaver.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class WorldSaverConstants
{
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create();

    public static final Path WORLD_SAVER_DIRECTORY = FabricLoader.getInstance().getGameDir().resolve("world-saver");

    public static final String MOD_ID = "world-saver";
}
