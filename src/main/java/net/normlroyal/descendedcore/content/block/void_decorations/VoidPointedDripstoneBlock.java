package net.normlroyal.descendedcore.content.block.void_decorations;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.normlroyal.descendedcore.content.block.CoreBlocks;
import net.normlroyal.descendedcore.util.CoreTags;
import org.jetbrains.annotations.Nullable;

public class VoidPointedDripstoneBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape TIP_MERGE_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape FRUSTUM_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape MIDDLE_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    private static final VoxelShape BASE_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public VoidPointedDripstoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TIP_DIRECTION, Direction.UP)
                .setValue(THICKNESS, DripstoneThickness.TIP)
                .setValue(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        Direction direction;
        if (context.getClickedFace() == Direction.DOWN) {
            direction = Direction.DOWN;
        } else if (context.getClickedFace() == Direction.UP) {
            direction = Direction.UP;
        } else {
            direction = context.getNearestLookingVerticalDirection().getOpposite();
        }

        BlockState state = this.defaultBlockState()
                .setValue(TIP_DIRECTION, direction)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);

        if (!canSurvive(state, level, pos)) {
            Direction opposite = direction.getOpposite();
            state = state.setValue(TIP_DIRECTION, opposite);
            if (!canSurvive(state, level, pos)) {
                return null;
            }
        }

        return recalculateThickness(level, pos, state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(TIP_DIRECTION);
        BlockPos supportPos = pos.relative(direction.getOpposite());
        BlockState support = level.getBlockState(supportPos);

        return isVoidPointedDripstone(support)
                || support.is(CoreBlocks.VOID_CAVE_WALL.get())
                || support.is(CoreTags.VOID_DECORATION_SUPPORT)
                || support.isFaceSturdy(level, supportPos, direction);
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
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (!canSurvive(state, level, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }

        return recalculateThickness(level, pos, state);
    }

    private BlockState recalculateThickness(LevelReader level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(TIP_DIRECTION);

        boolean sameBehind = isVoidPointedDripstoneWithDirection(level.getBlockState(pos.relative(direction.getOpposite())), direction);
        boolean sameAhead = isVoidPointedDripstoneWithDirection(level.getBlockState(pos.relative(direction)), direction);
        boolean oppositeAhead = isVoidPointedDripstoneWithDirection(level.getBlockState(pos.relative(direction)), direction.getOpposite());

        DripstoneThickness thickness;

        if (oppositeAhead) {
            thickness = DripstoneThickness.TIP_MERGE;
        } else if (!sameAhead) {
            thickness = DripstoneThickness.TIP;
        } else if (!sameBehind) {
            thickness = DripstoneThickness.BASE;
        } else {
            BlockState ahead = level.getBlockState(pos.relative(direction));
            boolean aheadIsTip = ahead.is(this)
                    && (ahead.getValue(THICKNESS) == DripstoneThickness.TIP
                    || ahead.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE);

            thickness = aheadIsTip ? DripstoneThickness.FRUSTUM : DripstoneThickness.MIDDLE;
        }

        return state.setValue(THICKNESS, thickness);
    }

    private static boolean isVoidPointedDripstone(BlockState state) {
        return state.is(CoreBlocks.VOID_POINTED_DRIPSTONE.get());
    }

    private static boolean isVoidPointedDripstoneWithDirection(BlockState state, Direction direction) {
        return isVoidPointedDripstone(state) && state.getValue(TIP_DIRECTION) == direction;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        DripstoneThickness thickness = state.getValue(THICKNESS);

        if (thickness == DripstoneThickness.TIP_MERGE) {
            return TIP_MERGE_SHAPE;
        }

        if (thickness == DripstoneThickness.TIP) {
            return state.getValue(TIP_DIRECTION) == Direction.UP ? TIP_SHAPE_UP : TIP_SHAPE_DOWN;
        }

        if (thickness == DripstoneThickness.FRUSTUM) {
            return FRUSTUM_SHAPE;
        }

        if (thickness == DripstoneThickness.MIDDLE) {
            return MIDDLE_SHAPE;
        }

        return BASE_SHAPE;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIP_DIRECTION, THICKNESS, WATERLOGGED);
    }
}