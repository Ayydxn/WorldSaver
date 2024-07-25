package me.ayydxn.worldsaver.mixin;

import me.ayydxn.worldsaver.WorldSaverCommonMod;
import me.ayydxn.worldsaver.config.WorldSaverGameOptions;
import me.ayydxn.worldsaver.events.WorldSaveEvents;
import me.ayydxn.worldsaver.events.server.WorldSaveServerEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
    @Unique
    private final WorldSaverGameOptions gameOptions = WorldSaverCommonMod.getInstance().getGameOptions();

    @Unique
    private long ticksUntilAutosave = this.gameOptions.autosaveInterval * 20L;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", ordinal = 1))
    public void onServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci)
    {
        if (!this.gameOptions.enableWorldSaving)
            return;

        if (!this.gameOptions.enableAutosave)
            return;

        --this.ticksUntilAutosave;
        if (this.ticksUntilAutosave <= 0L)
        {
            WorldSaveEvents.AUTO_SAVE.invoker().onAutoSave((MinecraftServer) (Object) this);

            this.ticksUntilAutosave = this.gameOptions.autosaveInterval * 20L;
        }
    }

    @Inject(method = "shutdown", at = @At("TAIL"))
    public void onServerShutdown(CallbackInfo ci)
    {
        if (!this.gameOptions.enableWorldSaving)
            return;

        WorldSaveServerEvents.SERVER_CLOSE.invoker().onServerClose((MinecraftServer) (Object) this);
        WorldSaveEvents.EXIT_WORLD.invoker().onWorldExit((MinecraftServer) (Object) this);
    }
}
