package heyblack.repeatersound.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AffectedBlocks
{
    static Set<Block> affectedBlocks = new HashSet<>(Arrays.asList(
            Blocks.REPEATER,
            Blocks.COMPARATOR,
            Blocks.REDSTONE_WIRE,
            Blocks.DAYLIGHT_DETECTOR
    ));

    public static Set<Block> get()
    {
        return affectedBlocks;
    }
}
