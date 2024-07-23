package me.ayydxn.worldsaver.mixin;

import me.ayydxn.worldsaver.events.WorldSaveEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
    // TODO: (Ayydxn) Should be configurable.
    @Unique
    private int ticksUntilAutosave = 200;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", ordinal = 1))
    public void onServerTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci)
    {
        --this.ticksUntilAutosave;
        if (this.ticksUntilAutosave <= 0)
        {
            WorldSaveEvents.AUTO_SAVE.invoker().onAutoSave((MinecraftServer) (Object) this);

            this.ticksUntilAutosave = 200;
        }
    }

    @Inject(method = "shutdown", at = @At("TAIL"))
    public void onServerShutdown(CallbackInfo ci)
    {
        WorldSaveEvents.EXIT_WORLD.invoker().onWorldExit((MinecraftServer) (Object) this);
    }
}
