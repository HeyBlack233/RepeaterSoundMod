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
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraft.block.ComparatorBlock.MODE;

@Environment(value = EnvType.CLIENT)
@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin {
    @Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    public void playSound(World world, PlayerEntity player, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch,
            BlockState state, World world2, BlockPos blockPos, PlayerEntity player2, BlockHitResult hit) {
        ConfigManager cfg = ConfigManager.getInstance();
        float basePitch = Float.parseFloat(cfg.getConfig(ConfigOption.BASE_PITCH.id));
        float finalPitch = Boolean.parseBoolean(cfg.getConfig(ConfigOption.USE_RANDOM.id))
                ? (float) (basePitch + (Math.random() - 0.5) * 0.25)
                : (state = state.cycle(MODE)).get(MODE) == ComparatorMode.SUBTRACT ? basePitch + 0.05f : basePitch;
        float finalVolume = Float.parseFloat(cfg.getConfig(ConfigOption.VOLUME.id));

        InteractionMode mode = InteractionMode.valueOf(cfg.getConfig(ConfigOption.INTERACTION_MODE.id));
        if (mode == InteractionMode.ALARM) {
            player2.sendMessage(Text.literal(cfg.getAlarmMessage(state, blockPos)), false);
            world.playSound(player2, pos, RepeaterSound.CLICK_ALARM, category, finalVolume, finalPitch);
        } else {
            world.playSound(player2, pos, sound, category, finalVolume, finalPitch);
        }
    }
}
