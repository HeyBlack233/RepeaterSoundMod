package heyblack.repeatersound.mixin;

import heyblack.repeatersound.RepeaterSound;
import heyblack.repeatersound.config.Config;
import heyblack.repeatersound.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value= EnvType.CLIENT)
@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin
{
    @Shadow
    protected static boolean isFullyConnected(BlockState state)
    {
        return false;
    }
    @Inject( method = "onUse", at = @At(value = "RETURN", ordinal = 1))
    public void playSound(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
    {
        Config config = RepeaterSound.getConfig();
        float basePitch = config.getBasePitch();
        float pitch = config.getRandomPitch() ?
                (float) (basePitch + (Math.random() - 0.5) * 0.25) :
                this.isFullyConnected(state) ? basePitch : basePitch + 0.05f;
        float volume = config.getVolume();
        world.playSound(player, pos, RepeaterSound.BLOCK_REDSTONE_WIRE_CLICK, SoundCategory.BLOCKS, volume, pitch);
    }
}