package heyblack.repeatersound.mixin;

import heyblack.repeatersound.util.ServerCloseCallback;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin
{
    @Inject(method = "stopServer", at = @At(value = "HEAD"))
    private void serverCloseCallback(CallbackInfo ci)
    {
        ServerCloseCallback.EVENT.invoker().saveConfig();
    }
}
