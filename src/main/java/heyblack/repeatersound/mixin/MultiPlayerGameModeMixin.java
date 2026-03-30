package heyblack.repeatersound.mixin;

import heyblack.repeatersound.config.ConfigManager;
import heyblack.repeatersound.config.ConfigOption;
import heyblack.repeatersound.util.AffectedBlocks;
import heyblack.repeatersound.util.InteractionMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value = EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(method = "useItemOn", at = @At(value = "HEAD"), cancellable = true)
    public void disableInteraction(LocalPlayer player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult blockHit, CallbackInfoReturnable<InteractionResult> cir) {
        ConfigManager cfg = ConfigManager.getInstance();
        InteractionMode mode = InteractionMode.valueOf(cfg.getConfig(ConfigOption.INTERACTION_MODE.id));
        if (mode == InteractionMode.DISABLED) {
            ClientLevel world = (ClientLevel) ((Entity) player).level();
            Block block = world.getBlockState(blockHit.getBlockPos()).getBlock();
            if (AffectedBlocks.get().contains(block)) {
                player.sendSystemMessage(Component.nullToEmpty(cfg.getConfig(ConfigOption.DISABLED_MESSAGE.id)));
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
}
