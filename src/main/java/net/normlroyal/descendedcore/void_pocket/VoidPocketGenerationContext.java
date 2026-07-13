package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.Set;

public final class VoidPocketGenerationContext {
    private final ServerLevel level;
    private final VoidPocketData.Pocket pocket;
    private final Random random;
    private final Set<BlockPos> protectedPositions;

    public VoidPocketGenerationContext(
            ServerLevel level,
            VoidPocketData.Pocket pocket,
            Random random,
            Set<BlockPos> protectedPositions
    ) {
        this.level = level;
        this.pocket = pocket;
        this.random = random;
        this.protectedPositions = protectedPositions;
    }

    public ServerLevel level() {
        return level;
    }

    public VoidPocketData.Pocket pocket() {
        return pocket;
    }

    public Random random() {
        return random;
    }

    public Set<BlockPos> protectedPositions() {
        return protectedPositions;
    }

    public boolean isProtected(BlockPos pos) {
        return protectedPositions.contains(pos) || VoidPocketPreservers.isPreserver(level.getBlockState(pos));
    }

    public void protect(BlockPos pos) {
        protectedPositions.add(pos.immutable());
    }

    public boolean setBlock(BlockPos pos, BlockState state, int flags) {
        if (isProtected(pos)) {
            return false;
        }
        return level.setBlock(pos, state, flags);
    }

    @Nullable
    public BlockPos findSurfaceBelow(BlockPos start, int minimumY) {
        BlockPos.MutableBlockPos mutable = start.mutable();
        for (int y = start.getY(); y >= minimumY; y--) {
            mutable.setY(y);
            BlockState state = level.getBlockState(mutable);
            if (!state.getCollisionShape(level, mutable).isEmpty()) {
                return mutable.immutable();
            }
        }
        return null;
    }
}
