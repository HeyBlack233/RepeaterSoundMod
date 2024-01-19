package heyblack.repeatersound.mixin;

import heyblack.repeatersound.config.ConfigManager;
import heyblack.repeatersound.util.AffectedBlocks;
import heyblack.repeatersound.util.InteractionMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value= EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin
{
    @Inject(method = "interactBlock", at = @At(value = "HEAD"), cancellable = true)
    public void disableInteraction(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir)
    {
        ConfigManager cfg = ConfigManager.getInstance();
        InteractionMode mode = InteractionMode.valueOf(cfg.getConfig("interactionMode"));
        if (mode == InteractionMode.DISABLED)
        {
            Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();
            if (AffectedBlocks.get().contains(block))
            {
                player.sendMessage(Text.of(cfg.getConfig("disabledMessage")), true);
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }
}
