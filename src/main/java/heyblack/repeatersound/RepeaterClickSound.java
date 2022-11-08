package heyblack.repeatersound;

import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RepeaterClickSound implements ModInitializer
{
    public static final Identifier REPEATER_CLICK = new Identifier("repeatersound:repeater_click");
    public static SoundEvent BLOCK_REPEATER_CLICK = new SoundEvent(REPEATER_CLICK);

    @Override
    public void onInitialize()
    {
        Registry.register(Registry.SOUND_EVENT, RepeaterClickSound.REPEATER_CLICK, BLOCK_REPEATER_CLICK);
    }
}
