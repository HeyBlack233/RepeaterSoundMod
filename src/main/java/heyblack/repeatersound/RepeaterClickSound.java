package heyblack.repeatersound;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class RepeaterClickSound implements ClientModInitializer
{
    public static final Identifier REPEATER_CLICK = new Identifier("repeatersound:repeater_click");
    public static SoundEvent BLOCK_REPEATER_CLICK = new SoundEvent(REPEATER_CLICK);

    public static final Identifier REDSTONE_WIRE_CLICK = new Identifier("repeatersound:redstone_wire_click");
    public static SoundEvent BLOCK_REDSTONE_WIRE_CLICK = new SoundEvent(REDSTONE_WIRE_CLICK);

    @Override
    public void onInitializeClient()
    {
        Registry.register(Registry.SOUND_EVENT, RepeaterClickSound.REPEATER_CLICK, BLOCK_REPEATER_CLICK);
        Registry.register(Registry.SOUND_EVENT, RepeaterClickSound.REDSTONE_WIRE_CLICK, BLOCK_REDSTONE_WIRE_CLICK);
    }
}
