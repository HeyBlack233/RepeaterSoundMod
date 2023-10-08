package heyblack.repeatersound.mixin;

import heyblack.repeatersound.util.InteractionMode;
import heyblack.repeatersound.RepeaterSound;
import heyblack.repeatersound.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.minecraft.block.ComparatorBlock.MODE;

@Environment(value= EnvType.CLIENT)
@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin
{
    BlockState state;
    PlayerEntity player;
    BlockPos blockPos;
    World world;

    @Inject(method = "onUse", at = @At(value = "HEAD"))
    public void getData(BlockState s, World w, BlockPos pos, PlayerEntity p, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
    {
        state = s;
        player = p;
        blockPos = pos;
        world = w;
    }

    @ModifyArgs(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    public void pitch(Args args)
    {
        if (world.isClient)
        {
            ConfigManager cfg = ConfigManager.getInstance();
            float basePitch = Float.parseFloat(cfg.getConfig("basePitch"));
            float pitch = Boolean.parseBoolean(cfg.getConfig("useRandom")) ?
                    (float) (basePitch + (Math.random() - 0.5) * 0.25) :
                    (state = state.cycle(MODE)).get(MODE) == ComparatorMode.SUBTRACT ?
                            basePitch + 0.05f :
                            basePitch;
            float volume = Float.parseFloat(cfg.getConfig("volume"));
            args.set(5, pitch);
            args.set(4, volume);

            InteractionMode mode = InteractionMode.valueOf(cfg.getConfig("interactionMode"));
            if (mode == InteractionMode.ALARM)
            {
                player.sendMessage(Text.of("Clicked " + state.getBlock().toString() + " At: " + blockPos.toShortString()), false);
                args.set(2, RepeaterSound.CLICK_ALARM);
            }
        }
    }
}
