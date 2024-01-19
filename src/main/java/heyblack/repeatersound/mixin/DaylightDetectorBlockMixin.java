package heyblack.repeatersound.mixin;

import heyblack.repeatersound.util.InteractionMode;
import heyblack.repeatersound.RepeaterSound;
import heyblack.repeatersound.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value= EnvType.CLIENT)
@Mixin(DaylightDetectorBlock.class)
public class DaylightDetectorBlockMixin
{
    @Shadow @Final public static BooleanProperty INVERTED;

    @Inject(method = "onUse", at = @At("HEAD"))
    public void playSound(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir)
    {
        if (world.isClient)
        {
            ConfigManager cfg = ConfigManager.getInstance();
            float basePitch = Float.parseFloat(cfg.getConfig("basePitch"));
            float pitch = Boolean.parseBoolean(cfg.getConfig("useRandom")) ?
                    (float) (basePitch + (Math.random() - 0.5) * 0.25) :
                    state.cycle(INVERTED).get(INVERTED) ?
                            basePitch :
                            basePitch + 0.05f;
            float volume = Float.parseFloat(cfg.getConfig("volume"));

            InteractionMode mode = InteractionMode.valueOf(cfg.getConfig("interactionMode"));
            switch (mode)
            {
                case NORMAL:
                    world.playSound(player, pos, RepeaterSound.BLOCK_DAYLIGHT_DETECTOR_CLICK, SoundCategory.BLOCKS, volume, pitch);
                    break;
                case ALARM:
                    world.playSound(player, pos, RepeaterSound.CLICK_ALARM, SoundCategory.BLOCKS, volume, pitch);
                    player.sendMessage(Text.of(cfg.getAlarmMessage(state, pos)), false);
                    break;
            }
        }
    }
}