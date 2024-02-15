package heyblack.repeatersound.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import heyblack.repeatersound.util.ServerCloseCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;

@Environment(value= EnvType.CLIENT)
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin 
{
    @Inject(method = "shutdown", at = @At(value = "HEAD"))
    private void serverCloseCallback(CallbackInfo ci) 
    {
        ServerCloseCallback.EVENT.invoker().saveConfig();
    }    
}
