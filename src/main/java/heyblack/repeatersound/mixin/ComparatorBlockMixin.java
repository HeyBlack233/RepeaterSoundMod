package heyblack.repeatersound.mixin;

import heyblack.repeatersound.config.Config;
import heyblack.repeatersound.config.ConfigManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.ComparatorBlock.MODE;

@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin
{
    BlockState state;
    @Inject(method = "onUse", at = @At(value = "HEAD"))
    public void getState(BlockState s, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
    {
        state = s;
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"), index = 5)
    public float pitch(float f)
    {
        Config config = ConfigManager.getInstance().getConfigFromFile();
        float basePitch = config.getBasePitch();
        float pitch = (config.getRandomPitch()) ?
                (float) (basePitch + (Math.random() - 0.5) * 0.25) :
                (state = state.cycle(MODE)).get(MODE) == ComparatorMode.SUBTRACT ? basePitch + 0.05f : basePitch;
        return pitch;
    }
}
