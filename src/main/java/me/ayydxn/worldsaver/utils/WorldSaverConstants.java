package me.ayydxn.worldsaver.utils;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class WorldSaverConstants
{
    public static final Path WORLD_SAVER_DIRECTORY = FabricLoader.getInstance().getGameDir().resolve("world-saver");

    public static final String MOD_ID = "world-saver";
}
