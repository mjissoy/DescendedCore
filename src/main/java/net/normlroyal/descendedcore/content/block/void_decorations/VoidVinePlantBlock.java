package net.normlroyal.descendedcore.content.block.void_decorations;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.normlroyal.descendedcore.content.block.CoreBlocks;
import net.normlroyal.descendedcore.util.CoreTags;

public class VoidVinePlantBlock extends BushBlock {
    public static final DirectionProperty GROWTH_DIRECTION =
            DirectionProperty.create("growth_direction", Direction.UP, Direction.DOWN);

    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public VoidVinePlantBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(GROWTH_DIRECTION, Direction.DOWN));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction growthDirection = state.getValue(GROWTH_DIRECTION);
        BlockPos supportPos = pos.relative(growthDirection.getOpposite());
        BlockState support = level.getBlockState(supportPos);

        return support.is(CoreBlocks.VOID_CAVE_WALL.get())
                || support.is(CoreTags.VOID_DECORATION_SUPPORT)
                || support.is(CoreBlocks.VOID_VINE.get())
                || support.is(CoreBlocks.VOID_VINE_PLANT.get())
                || support.isFaceSturdy(level, supportPos, growthDirection);
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        return this.canSurvive(state, level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GROWTH_DIRECTION);
    }
}