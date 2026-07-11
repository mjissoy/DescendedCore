package net.normlroyal.descendedcore.content.block.void_decorations;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.normlroyal.descendedcore.content.block.CoreBlocks;
import net.normlroyal.descendedcore.util.CoreTags;


public class VoidPoppyBlock extends BushBlock {
    public VoidPoppyBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(CoreBlocks.VOID_CAVE_WALL.get())
                || state.is(CoreTags.VOID_DECORATION_SUPPORT)
                || state.isFaceSturdy(level, pos, net.minecraft.core.Direction.UP);
    }
}