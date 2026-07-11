package net.normlroyal.descendedcore.content.block.void_decorations;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
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
import org.jetbrains.annotations.Nullable;

public class VoidVineBlock extends BushBlock implements BonemealableBlock {
    public static final DirectionProperty GROWTH_DIRECTION =
            DirectionProperty.create("growth_direction", Direction.UP, Direction.DOWN);

    private static final int MAX_LENGTH = 12;
    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public VoidVineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(GROWTH_DIRECTION, Direction.DOWN));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();

        if (clickedFace == Direction.UP) {
            return this.defaultBlockState().setValue(GROWTH_DIRECTION, Direction.UP);
        }

        if (clickedFace == Direction.DOWN) {
            return this.defaultBlockState().setValue(GROWTH_DIRECTION, Direction.DOWN);
        }

        Direction preferred = context.getNearestLookingVerticalDirection().getOpposite();
        if (preferred == Direction.UP || preferred == Direction.DOWN) {
            return this.defaultBlockState().setValue(GROWTH_DIRECTION, preferred);
        }

        return this.defaultBlockState();
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
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) != 0) {
            return;
        }

        grow(level, pos, state);
    }

    private void grow(ServerLevel level, BlockPos pos, BlockState state) {
        Direction growthDirection = state.getValue(GROWTH_DIRECTION);
        BlockPos nextPos = pos.relative(growthDirection);

        if (!level.isEmptyBlock(nextPos)) {
            return;
        }

        if (countLength(level, pos, growthDirection) >= MAX_LENGTH) {
            return;
        }

        BlockState body = CoreBlocks.VOID_VINE_PLANT.get()
                .defaultBlockState()
                .setValue(VoidVinePlantBlock.GROWTH_DIRECTION, growthDirection);

        BlockState head = this.defaultBlockState()
                .setValue(GROWTH_DIRECTION, growthDirection);

        level.setBlock(pos, body, 3);
        level.setBlock(nextPos, head, 3);
    }

    private int countLength(LevelReader level, BlockPos headPos, Direction growthDirection) {
        int length = 1;
        BlockPos.MutableBlockPos mutable = headPos.mutable();

        for (int i = 0; i < MAX_LENGTH + 1; i++) {
            mutable.move(growthDirection.getOpposite());
            BlockState state = level.getBlockState(mutable);

            if (state.is(CoreBlocks.VOID_VINE.get()) || state.is(CoreBlocks.VOID_VINE_PLANT.get())) {
                length++;
            } else {
                break;
            }
        }

        return length;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean clientSide) {
        Direction growthDirection = state.getValue(GROWTH_DIRECTION);
        return level.getBlockState(pos.relative(growthDirection)).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        grow(level, pos, state);
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