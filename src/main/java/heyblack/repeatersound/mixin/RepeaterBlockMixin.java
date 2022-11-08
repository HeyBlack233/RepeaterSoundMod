package heyblack.repeatersound.mixin;

import heyblack.repeatersound.RepeaterClickSound;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepeaterBlock.class)
public class RepeaterBlockMixin
{
    @Inject(at = @At("TAIL"), method = "onUse")
    private void playSound(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
    {
        world.playSound(player, pos, RepeaterClickSound.BLOCK_REPEATER_CLICK, SoundCategory.BLOCKS, 0.3f, 0.5f);
    }
}
