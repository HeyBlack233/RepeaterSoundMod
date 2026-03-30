package heyblack.repeatersound.mixin;

import heyblack.repeatersound.RepeaterSound;
import heyblack.repeatersound.config.ConfigManager;
import heyblack.repeatersound.config.ConfigOption;
import heyblack.repeatersound.util.InteractionMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value = EnvType.CLIENT)
@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {
    @Shadow
    private static boolean isCross(BlockState state) {
        return false;
    }

    @Inject(method = "useWithoutItem", at = @At(value = "RETURN", ordinal = 1))
    public void playSound(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.isClientSide()) {
            ConfigManager cfg = ConfigManager.getInstance();
            float basePitch = Float.parseFloat(cfg.getConfig(ConfigOption.BASE_PITCH.id));
            float pitch = Boolean.parseBoolean(cfg.getConfig(ConfigOption.USE_RANDOM.id))
                    ? (float) (basePitch + (Math.random() - 0.5) * 0.25)
                    : isCross(state) ? basePitch : basePitch + 0.05f;
            float volume = Float.parseFloat(cfg.getConfig(ConfigOption.VOLUME.id));

            InteractionMode mode = InteractionMode.valueOf(cfg.getConfig(ConfigOption.INTERACTION_MODE.id));
            switch (mode) {
                case NORMAL:
                    level.playSound(player, pos, RepeaterSound.BLOCK_REDSTONE_WIRE_CLICK, SoundSource.BLOCKS, volume,
                            pitch);
                    break;
                case ALARM:
                    level.playSound(player, pos, RepeaterSound.CLICK_ALARM, SoundSource.BLOCKS, volume, pitch);
                    player.sendSystemMessage(Component.literal(cfg.getAlarmMessage(state, pos)));
                    break;
            }
        }
    }
}
