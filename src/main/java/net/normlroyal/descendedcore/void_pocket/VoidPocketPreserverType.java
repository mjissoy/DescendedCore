package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

public interface VoidPocketPreserverType {
    ResourceLocation id();

    boolean matches(BlockState state);

    BlockState restorationState();

    default boolean canPreserve(ServerPlayer player, ServerLevel level, BlockPos pos) {
        return true;
    }

    default String defaultName(ServerLevel level, BlockPos pos) {
        return "Void Preserver " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }
}
