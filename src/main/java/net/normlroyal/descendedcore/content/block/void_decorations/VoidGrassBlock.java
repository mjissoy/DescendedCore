package net.normlroyal.descendedcore.content.block.void_decorations;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.normlroyal.descendedcore.content.block.CoreBlocks;
import net.normlroyal.descendedcore.util.CoreTags;

public class VoidGrassBlock extends BushBlock implements BonemealableBlock {
    public VoidGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(CoreBlocks.VOID_CAVE_WALL.get())
                || state.is(CoreTags.VOID_DECORATION_SUPPORT)
                || state.isFaceSturdy(level, pos, net.minecraft.core.Direction.UP);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean clientSide) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    }
}