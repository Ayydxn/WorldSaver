package me.ayydxn.worldsaver.mixin.client;

import me.ayydxn.worldsaver.events.client.WorldSaveClientEvents;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    @Inject(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;close()V", shift = At.Shift.AFTER))
    public void onClientStop(CallbackInfo ci)
    {
        WorldSaveClientEvents.CLIENT_CLOSE.invoker().onClientClose((MinecraftClient) (Object) this);
    }
}
