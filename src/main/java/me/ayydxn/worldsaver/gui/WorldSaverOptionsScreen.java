package me.ayydxn.worldsaver.gui;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import me.ayydxn.worldsaver.WorldSaverCommonMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class WorldSaverOptionsScreen
{
    private final Screen parentScreen;

    public WorldSaverOptionsScreen(@Nullable Screen parentScreen)
    {
        this.parentScreen = parentScreen;
    }

    public Screen getHandle()
    {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("World Saver Options"))
                .category(WorldSaverOptionCategories.worldSaver())
                .save(() ->
                {
                    WorldSaverCommonMod.getInstance().getGameOptions().save();

                    // (Ayydxn) Hack to refresh the options screen.
                    MinecraftClient.getInstance().setScreen(new WorldSaverOptionsScreen(null).getHandle());
                })
                .build()
                .generateScreen(this.parentScreen);
    }
}
