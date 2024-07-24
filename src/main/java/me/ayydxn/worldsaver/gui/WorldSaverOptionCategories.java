package me.ayydxn.worldsaver.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import dev.isxander.yacl3.gui.controllers.slider.LongSliderController;
import me.ayydxn.worldsaver.WorldSaverCommonMod;
import me.ayydxn.worldsaver.config.WorldSaverGameOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.concurrent.TimeUnit;

public class WorldSaverOptionCategories
{
    private static final WorldSaverGameOptions DEFAULT_GAME_OPTIONS = WorldSaverGameOptions.defaults();
    private static final WorldSaverGameOptions GAME_OPTIONS = WorldSaverCommonMod.getInstance().getGameOptions();

    public static ConfigCategory worldSaver()
    {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("worldSaver.options.category.worldSaver"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("worldSaver.options.worldSaver.enableWorldSaving"))
                        .description(OptionDescription.of(Text.translatable("worldSaver.options.worldSaver.enableWorldSaving.description")))
                        .binding(DEFAULT_GAME_OPTIONS.enableWorldSaving, () -> GAME_OPTIONS.enableWorldSaving, newValue -> GAME_OPTIONS.enableWorldSaving = newValue)
                        .customController(BooleanController::new)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("worldSaver.options.worldSaver.enableAutosave"))
                        .description(OptionDescription.of(Text.translatable("worldSaver.options.worldSaver.enableAutosave.description")))
                        .binding(DEFAULT_GAME_OPTIONS.enableAutosave, () -> GAME_OPTIONS.enableAutosave, newValue -> GAME_OPTIONS.enableAutosave = newValue)
                        .customController(BooleanController::new)
                        .available(GAME_OPTIONS.enableWorldSaving)
                        .build())
                .option(Option.<Long>createBuilder()
                        .name(Text.translatable("worldSaver.options.worldSaver.autosaveInterval"))
                        .description(OptionDescription.of(Text.translatable("worldSaver.options.worldSaver.autosaveInterval.description")))
                        .binding(DEFAULT_GAME_OPTIONS.autosaveInterval, () -> GAME_OPTIONS.autosaveInterval, newValue -> GAME_OPTIONS.autosaveInterval = newValue)
                        .customController(option -> new LongSliderController(option, 60, TimeUnit.HOURS.toSeconds(24L), 1, WorldSaverOptionCategories::getAutosaveIntervalText))
                        .available(GAME_OPTIONS.enableWorldSaving && GAME_OPTIONS.enableAutosave && !MinecraftClient.getInstance().isInSingleplayer())
                        .build())
                .build();
    }

    // (Ayydxn) Thank You, Google Gemini <3
    private static Text getAutosaveIntervalText(long autosaveInterval)
    {
        long hours = TimeUnit.SECONDS.toHours(autosaveInterval);
        long minutes = TimeUnit.SECONDS.toMinutes(autosaveInterval - TimeUnit.HOURS.toSeconds(hours));

        StringBuilder resultTextString = new StringBuilder();

        if (hours > 0)
        {
            resultTextString.append(hours)
                    .append(hours == 1 ? " Hour" : " Hours");

            if (minutes > 0)
                resultTextString.append(" and ");
        }

        if (minutes > 0)
        {
            resultTextString.append(minutes)
                    .append(minutes == 1 ? " Minute" : " Minutes");
        }

        return Text.literal(resultTextString.toString());
    }
}
