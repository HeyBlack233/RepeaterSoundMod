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
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.ComparatorBlock.MODE;

@Environment(value = EnvType.CLIENT)
@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin {
    @Inject(method = "onUse", at = @At("TAIL"))
    public void playSound(BlockState state, World world, BlockPos pos, PlayerEntity player,
            BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient()) {
            ConfigManager cfg = ConfigManager.getInstance();
            float basePitch = Float.parseFloat(cfg.getConfig(ConfigOption.BASE_PITCH.id));
            float pitch = Boolean.parseBoolean(cfg.getConfig(ConfigOption.USE_RANDOM.id))
                    ? (float) (basePitch + (Math.random() - 0.5) * 0.25)
                    : (state = state.cycle(MODE)).get(MODE) == ComparatorMode.SUBTRACT ? basePitch + 0.05f : basePitch;
            float volume = Float.parseFloat(cfg.getConfig(ConfigOption.VOLUME.id));

            InteractionMode mode = InteractionMode.valueOf(cfg.getConfig(ConfigOption.INTERACTION_MODE.id));
            switch (mode) {
                case NORMAL:
                    world.playSound(player, pos, RepeaterSound.BLOCK_REPEATER_CLICK, SoundCategory.BLOCKS, volume, pitch);
                    break;
                case ALARM:
                    world.playSound(player, pos, RepeaterSound.CLICK_ALARM, SoundCategory.BLOCKS, volume, pitch);
                    player.sendMessage(Text.literal(cfg.getAlarmMessage(state, pos)), false);
                    break;
            }
        }
    }
}
