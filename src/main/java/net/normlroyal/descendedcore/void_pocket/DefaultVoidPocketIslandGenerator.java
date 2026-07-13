package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.phys.AABB;
import net.normlroyal.descendedcore.content.block.CoreBlocks;
import net.normlroyal.descendedcore.content.block.void_decorations.VoidPointedDripstoneBlock;
import net.normlroyal.descendedcore.content.block.void_decorations.VoidVineBlock;
import net.normlroyal.descendedcore.content.block.void_decorations.VoidVinePlantBlock;

public final class DefaultVoidPocketIslandGenerator {
    private DefaultVoidPocketIslandGenerator() {
    }

    public static void generate(ServerLevel level, VoidPocketData.Pocket pocket) {
        BlockState voidBlock = CoreBlocks.VOID_CAVE_WALL.get().defaultBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();

        VoidPocketData data = VoidPocketData.get(level.getServer());

        java.util.HashSet<BlockPos> protectedAnchors = new java.util.HashSet<>();
        for (VoidPocketData.Anchor anchor : data.anchorsInPocket(pocket)) {
            protectedAnchors.add(anchor.pos);
        }

        clearPocketVolume(level, pocket, protectedAnchors);

        long seed = pocket.id.getMostSignificantBits() ^ pocket.id.getLeastSignificantBits();
        java.util.Random random = new java.util.Random(seed);
        VoidPocketGenerationContext context = new VoidPocketGenerationContext(level, pocket, random, protectedAnchors);

        VoidPocketIslandGenerators.generateStage(VoidPocketGenerationStage.BEFORE_TERRAIN, context);
        buildMainVoidIsland(level, pocket, voidBlock, protectedAnchors, random);
        buildSideIslands(level, pocket, voidBlock, protectedAnchors, random);
        buildEntryPlatform(level, pocket, voidBlock, air, protectedAnchors);
        VoidPocketIslandGenerators.generateStage(VoidPocketGenerationStage.AFTER_TERRAIN, context);
        carveSurfaceDepressions(level, pocket, air, protectedAnchors, random);

        decorateVoidCrystalSpikes(level, pocket, protectedAnchors, random);
        decorateVoidPocketFlora(level, pocket, protectedAnchors, random);
        VoidPocketIslandGenerators.generateStage(VoidPocketGenerationStage.AFTER_DECORATION, context);

        VoidPocketManager.restorePocketPreservers(level, pocket);

        pocket.generated = true;
        data.setDirty();
    }

    private static void clearPocketVolume(
            ServerLevel level,
            VoidPocketData.Pocket pocket,
            java.util.Set<BlockPos> protectedAnchors
    ) {
        BlockState air = Blocks.AIR.defaultBlockState();
        AABB bounds = pocket.bounds();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = Mth.floor(bounds.minX); x < Mth.floor(bounds.maxX); x++) {
            for (int y = Mth.floor(bounds.minY); y < Mth.floor(bounds.maxY); y++) {
                for (int z = Mth.floor(bounds.minZ); z < Mth.floor(bounds.maxZ); z++) {
                    mutable.set(x, y, z);
                    if (isProtected(level, mutable, protectedAnchors)) {
                        continue;
                    }
                    level.setBlock(mutable, air, 3);
                }
            }
        }
    }

    private static void buildMainVoidIsland(
            ServerLevel level,
            VoidPocketData.Pocket pocket,
            BlockState block,
            java.util.Set<BlockPos> protectedAnchors,
            java.util.Random random
    ) {
        BlockPos center = pocket.center;

        placeIslandBlob(level, center.offset(0, 0, 0), 18, 7, 14, block, protectedAnchors, random);
        placeIslandBlob(level, center.offset(7, -1, 3), 10, 5, 8, block, protectedAnchors, random);
        placeIslandBlob(level, center.offset(-8, -1, -4), 9, 5, 7, block, protectedAnchors, random);
        placeIslandBlob(level, center.offset(2, 1, -8), 8, 4, 6, block, protectedAnchors, random);

        for (int i = 0; i < 18; i++) {
            int x = center.getX() + random.nextInt(29) - 14;
            int z = center.getZ() + random.nextInt(25) - 12;
            int length = 2 + random.nextInt(5);

            for (int y = center.getY() - 5; y > center.getY() - 5 - length; y--) {
                BlockPos pos = new BlockPos(x, y, z);
                if (!isProtected(level, pos, protectedAnchors)) {
                    level.setBlock(pos, CoreBlocks.VOID_CAVE_WALL.get().defaultBlockState(), 3);
                }
            }
        }
    }

    private static void buildSideIslands(
            ServerLevel level,
            VoidPocketData.Pocket pocket,
            BlockState block,
            java.util.Set<BlockPos> protectedAnchors,
            java.util.Random random
    ) {
        int count = 1 + random.nextInt(3);

        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            int distance = 20 + random.nextInt(8);

            int x = pocket.center.getX() + Mth.floor(Math.cos(angle) * distance);
            int z = pocket.center.getZ() + Mth.floor(Math.sin(angle) * distance);
            int y = pocket.center.getY() + random.nextInt(7) - 3;

            BlockPos sideCenter = new BlockPos(x, y, z);

            placeIslandBlob(
                    level,
                    sideCenter,
                    5 + random.nextInt(4),
                    3 + random.nextInt(2),
                    5 + random.nextInt(4),
                    block,
                    protectedAnchors,
                    random
            );
        }
    }

    private static void buildEntryPlatform(
            ServerLevel level,
            VoidPocketData.Pocket pocket,
            BlockState block,
            BlockState air,
            java.util.Set<BlockPos> protectedAnchors
    ) {
        BlockPos entry = pocket.entryPos();
        BlockPos floorCenter = entry.below();

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (x * x + z * z <= 10) {
                    BlockPos floor = floorCenter.offset(x, 0, z);
                    mutable.set(floor);

                    if (!isProtected(level, mutable, protectedAnchors)) {
                        BlockState platformBlock = Math.abs(x) <= 1 && Math.abs(z) <= 1
                                ? CoreBlocks.VOID_WALL_BRICKS.get().defaultBlockState()
                                : CoreBlocks.VOID_CAVE_WALL.get().defaultBlockState();

                        level.setBlock(mutable, platformBlock, 3);
                        level.setBlock(floor.above(), air, 3);
                        level.setBlock(floor.above(2), air, 3);
                    }
                }
            }
        }
    }

    private static BlockPos findSurfaceBelow(ServerLevel level, BlockPos start, VoidPocketData.Pocket pocket) {
        for (int y = start.getY(); y >= pocket.center.getY() - 8; y--) {
            BlockPos pos = new BlockPos(start.getX(), y, start.getZ());
            BlockPos above = pos.above();

            if (level.getBlockState(pos).is(CoreBlocks.VOID_CAVE_WALL.get()) && level.isEmptyBlock(above)) {
                return pos;
            }
        }

        return null;
    }

    private static void carveSurfaceDepressions(
            ServerLevel level,
            VoidPocketData.Pocket pocket,
            BlockState air,
            java.util.Set<BlockPos> protectedAnchors,
            java.util.Random random
    ) {
        int count = 2 + random.nextInt(3);

        for (int i = 0; i < count; i++) {
            BlockPos center = pocket.center.offset(
                    random.nextInt(21) - 10,
                    4 + random.nextInt(2),
                    random.nextInt(17) - 8
            );

            int radius = 2 + random.nextInt(3);

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z > radius * radius) {
                        continue;
                    }

                    BlockPos pos = center.offset(x, 0, z);
                    if (!isProtected(level, pos, protectedAnchors)) {
                        level.setBlock(pos, air, 3);
                    }
                }
            }
        }
    }

    private static void placeIslandBlob(
            ServerLevel level,
            BlockPos center,
            int radiusX,
            int radiusY,
            int radiusZ,
            BlockState block,
            java.util.Set<BlockPos> protectedAnchors,
            java.util.Random random
    ) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    double nx = x / (double) radiusX;
                    double ny = y / (double) radiusY;
                    double nz = z / (double) radiusZ;

                    double distance = nx * nx + ny * ny + nz * nz;

                    if (y > 2) {
                        distance += y * 0.08D;
                    }
                    if (y < -2) {
                        distance -= 0.10D;
                    }

                    double roughness = ((x * 734287 + y * 912931 + z * 438289) & 15) / 100.0D;

                    if (distance <= 1.0D - roughness) {
                        BlockPos pos = center.offset(x, y, z);
                        mutable.set(pos);
                        if (!isProtected(level, mutable, protectedAnchors)) {
                            boolean surfaceLike = y >= -1;
                            BlockState chosenBlock = chooseIslandBlock(random, y, surfaceLike);
                            level.setBlock(mutable, chosenBlock, 3);
                        }
                    }
                }
            }
        }
    }

    private static void decorateVoidCrystalSpikes(
            ServerLevel level,
            VoidPocketData.Pocket pocket,
            java.util.Set<BlockPos> protectedAnchors,
            java.util.Random random
    ) {
        AABB bounds = pocket.bounds();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = Mth.floor(bounds.minX) + 2; x < Mth.floor(bounds.maxX) - 2; x++) {
            for (int y = Mth.floor(bounds.minY) + 2; y < Mth.floor(bounds.maxY) - 2; y++) {
                for (int z = Mth.floor(bounds.minZ) + 2; z < Mth.floor(bounds.maxZ) - 2; z++) {
                    mutable.set(x, y, z);
                    BlockPos pos = mutable.immutable();

                    if (isProtected(level, pos, protectedAnchors) || pos.distSqr(pocket.entryPos()) < 36.0D) {
                        continue;
                    }

                    if (isVoidSpikeSurface(level, pos.below()) && level.isEmptyBlock(pos)) {
                        if (random.nextFloat() < 0.012F) {
                            placeVoidSpike(level, pos, Direction.UP, 1 + random.nextInt(4));
                        }
                    }

                    if (isVoidSpikeSurface(level, pos.above()) && level.isEmptyBlock(pos)) {
                        if (random.nextFloat() < 0.020F) {
                            placeVoidSpike(level, pos, Direction.DOWN, 2 + random.nextInt(5));
                        }
                    }
                }
            }
        }
    }

    private static boolean isVoidSpikeSurface(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(CoreBlocks.VOID_CAVE_WALL.get())
                || state.is(CoreBlocks.VOID_WALL_BRICKS.get())
                || state.is(CoreBlocks.SMOOTH_VOID_WALL.get());
    }

    private static void placeVoidSpike(ServerLevel level, BlockPos startPos, Direction direction, int length) {
        if (direction != Direction.UP && direction != Direction.DOWN) {
            return;
        }

        length = Mth.clamp(length, 1, 8);

        for (int i = 0; i < length; i++) {
            BlockPos pos = startPos.relative(direction, i);

            if (!level.isEmptyBlock(pos)) {
                return;
            }
        }

        for (int i = 0; i < length; i++) {
            BlockPos pos = startPos.relative(direction, i);

            BlockState state = CoreBlocks.VOID_POINTED_DRIPSTONE.get()
                    .defaultBlockState()
                    .setValue(VoidPointedDripstoneBlock.TIP_DIRECTION, direction)
                    .setValue(VoidPointedDripstoneBlock.THICKNESS, thicknessForSpikeIndex(i, length))
                    .setValue(VoidPointedDripstoneBlock.WATERLOGGED, false);

            level.setBlock(pos, state, 3);
        }
    }

    private static DripstoneThickness thicknessForSpikeIndex(int index, int length) {
        if (length <= 1) {return DripstoneThickness.TIP;}
        if (index == 0) {return DripstoneThickness.BASE;}
        if (index == length - 1) {return DripstoneThickness.TIP;}
        if (index == length - 2) {return DripstoneThickness.FRUSTUM;}
        return DripstoneThickness.MIDDLE;
    }

    private static void decorateVoidPocketFlora(
            ServerLevel level,
            VoidPocketData.Pocket pocket,
            java.util.Set<BlockPos> protectedAnchors,
            java.util.Random random
    ) {
        AABB bounds = pocket.bounds();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = Mth.floor(bounds.minX) + 2; x < Mth.floor(bounds.maxX) - 2; x++) {
            for (int y = Mth.floor(bounds.minY) + 2; y < Mth.floor(bounds.maxY) - 2; y++) {
                for (int z = Mth.floor(bounds.minZ) + 2; z < Mth.floor(bounds.maxZ) - 2; z++) {
                    mutable.set(x, y, z);

                    if (isProtected(level, mutable, protectedAnchors)) {
                        continue;
                    }

                    BlockPos pos = mutable.immutable();

                    if (pos.distSqr(pocket.entryPos()) < 25.0D) {
                        continue;
                    }

                    if (isVoidSurface(level, pos.below()) && level.isEmptyBlock(pos)) {
                        float roll = random.nextFloat();

                        if (roll < 0.055F) {
                            level.setBlock(pos, CoreBlocks.VOID_GRASS.get().defaultBlockState(), 3);
                        } else if (roll < 0.070F) {
                            level.setBlock(pos, CoreBlocks.VOID_POPPY.get().defaultBlockState(), 3);
                        } else if (roll < 0.082F) {
                            placeVoidVine(level, pos, Direction.UP, 1 + random.nextInt(4));
                        }
                    }

                    if (isVoidSurface(level, pos.above()) && level.isEmptyBlock(pos)) {
                        if (random.nextFloat() < 0.015F) {
                            placeVoidVine(level, pos, Direction.DOWN, 2 + random.nextInt(6));
                        }
                    }
                }
            }
        }
    }

    private static BlockState chooseIslandBlock(java.util.Random random, int localY, boolean surfaceLike) {
        if (surfaceLike) {
            int roll = random.nextInt(100);
            if (roll < 82) return CoreBlocks.VOID_CAVE_WALL.get().defaultBlockState();
            if (roll < 93) return CoreBlocks.VOID_WALL_BRICKS.get().defaultBlockState();
            return CoreBlocks.SMOOTH_VOID_WALL.get().defaultBlockState();
        }

        int roll = random.nextInt(100);
        if (roll < 92) return CoreBlocks.VOID_CAVE_WALL.get().defaultBlockState();
        if (roll < 97) return CoreBlocks.VOID_WALL_BRICKS.get().defaultBlockState();
        return CoreBlocks.SMOOTH_VOID_WALL.get().defaultBlockState();
    }

    private static boolean isVoidSurface(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(CoreBlocks.VOID_CAVE_WALL.get())
                || state.is(CoreBlocks.VOID_WALL_BRICKS.get())
                || state.is(CoreBlocks.SMOOTH_VOID_WALL.get());
    }

    private static void placeVoidVine(ServerLevel level, BlockPos startPos, Direction growthDirection, int length) {
        if (growthDirection != Direction.UP && growthDirection != Direction.DOWN) {
            return;
        }

        length = Mth.clamp(length, 1, 12);
        for (int i = 0; i < length; i++) {
            BlockPos pos = startPos.relative(growthDirection, i);
            if (!level.isEmptyBlock(pos)) {
                return;
            }

            boolean isHead = i == length - 1;
            if (isHead) {
                level.setBlock(
                        pos,
                        CoreBlocks.VOID_VINE.get().defaultBlockState()
                                .setValue(VoidVineBlock.GROWTH_DIRECTION, growthDirection),
                        3
                );
            } else {
                level.setBlock(
                        pos,
                        CoreBlocks.VOID_VINE_PLANT.get().defaultBlockState()
                                .setValue(VoidVinePlantBlock.GROWTH_DIRECTION, growthDirection),
                        3
                );
            }
        }
    }

    private static boolean isProtected(ServerLevel level, BlockPos pos, java.util.Set<BlockPos> protectedPositions) {
        return protectedPositions.contains(pos) || VoidPocketPreservers.isPreserver(level.getBlockState(pos));
    }
}
