package heyblack.repeatersound.mixin;

import heyblack.repeatersound.RepeaterSound;
import heyblack.repeatersound.config.ConfigManager;
import heyblack.repeatersound.config.ConfigOption;
import heyblack.repeatersound.util.InteractionMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.minecraft.world.level.block.ComparatorBlock.MODE;

@Environment(value= EnvType.CLIENT)
@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin
{
    @ModifyArgs(method = "useWithoutItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    public void pitch(Args args, BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult)
    {
        if (level.isClientSide())
        {
            ConfigManager cfg = ConfigManager.getInstance();
            float basePitch = Float.parseFloat(cfg.getConfig(ConfigOption.BASE_PITCH.id));
            float pitch = Boolean.parseBoolean(cfg.getConfig(ConfigOption.USE_RANDOM.id)) ?
                    (float) (basePitch + (Math.random() - 0.5) * 0.25) :
                    (state = state.cycle(MODE)).getValue(MODE) == ComparatorMode.SUBTRACT ?
                    basePitch + 0.05f :
                    basePitch;
            float volume = Float.parseFloat(cfg.getConfig(ConfigOption.VOLUME.id));
            args.set(5, pitch);
            args.set(4, volume);

            InteractionMode mode = InteractionMode.valueOf(cfg.getConfig(ConfigOption.INTERACTION_MODE.id));
            if (mode == InteractionMode.ALARM)
            {
                player.sendSystemMessage(Component.literal(cfg.getAlarmMessage(state, pos)));
                args.set(2, RepeaterSound.CLICK_ALARM);
            }
        }
    }
}
