package me.ayydxn.worldsaver.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import me.ayydxn.worldsaver.gui.WorldSaverOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen
{
    public OptionsScreenMixin()
    {
        super(Text.literal("World Saver Options Screen Mixin"));
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 9, shift = At.Shift.AFTER))
    public void addWorldSaverOptionsScreenButton(CallbackInfo ci, @Local GridWidget.Adder adder)
    {
        Screen worldSaverOptionsScreen = new WorldSaverOptionsScreen(this).getHandle();

        adder.add(new ButtonWidget.Builder(Text.translatable("worldSaver.gui.optionsButtonText"), (button) -> this.client.setScreen(worldSaverOptionsScreen))
                .build());
    }
}
