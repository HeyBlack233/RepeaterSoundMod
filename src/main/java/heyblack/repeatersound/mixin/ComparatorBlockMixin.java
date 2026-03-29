package heyblack.repeatersound.mixin;

import heyblack.repeatersound.RepeaterSound;
import heyblack.repeatersound.config.ConfigManager;
import heyblack.repeatersound.config.ConfigOption;
import heyblack.repeatersound.util.InteractionMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.minecraft.block.ComparatorBlock.MODE;

@Environment(value= EnvType.CLIENT)
@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin
{
    @ModifyArgs(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    public void pitch(Args args, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit)
    {
        if (world.isClient())
        {
            ConfigManager cfg = ConfigManager.getInstance();
            float basePitch = Float.parseFloat(cfg.getConfig(ConfigOption.BASE_PITCH.id));
            float pitch = Boolean.parseBoolean(cfg.getConfig(ConfigOption.USE_RANDOM.id)) ?
                    (float) (basePitch + (Math.random() - 0.5) * 0.25) :
                    (state = state.cycle(MODE)).get(MODE) == ComparatorMode.SUBTRACT ?
                    basePitch + 0.05f :
                    basePitch;
            float volume = Float.parseFloat(cfg.getConfig(ConfigOption.VOLUME.id));
            args.set(5, pitch);
            args.set(4, volume);

            InteractionMode mode = InteractionMode.valueOf(cfg.getConfig(ConfigOption.INTERACTION_MODE.id));
            if (mode == InteractionMode.ALARM)
            {
                player.sendMessage(Text.literal(cfg.getAlarmMessage(state, pos)), false);
                args.set(2, RepeaterSound.CLICK_ALARM);
            }
        }
    }
}
