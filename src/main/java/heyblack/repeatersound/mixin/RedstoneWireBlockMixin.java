package heyblack.repeatersound.mixin;

import heyblack.repeatersound.RepeaterClickSound;
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

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin
{
    @Shadow
    protected static boolean isFullyConnected(BlockState state)
    {
        return false;
    }

    @Inject(at = @At(value = "RETURN", ordinal = 1), method = "onUse")
    @Environment(value= EnvType.CLIENT)
    public void playSound(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
    {
        float pitch = this.isFullyConnected(state) ? 0.5f : 0.55f;
        world.playSound(player, pos, RepeaterClickSound.BLOCK_REDSTONE_WIRE_CLICK, SoundCategory.BLOCKS, 0.3f, pitch);
    }
}