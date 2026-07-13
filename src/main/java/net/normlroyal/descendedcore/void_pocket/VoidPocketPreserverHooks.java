package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class VoidPocketPreserverHooks {
    private VoidPocketPreserverHooks() {
    }

    public static Optional<VoidPocketData.Anchor> onPlaced(
            Level level,
            BlockPos pos,
            @Nullable LivingEntity placer,
            ResourceLocation type,
            @Nullable String name
    ) {
        if (level.isClientSide) {
            return Optional.empty();
        }
        return Optional.of(VoidPocketManager.registerPreserver(level, pos, placer, type, name));
    }

    public static void onRemoved(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            VoidPocketManager.removePreserver(level, pos);
        }
    }

    public static void preserveAndExit(
            ServerPlayer player,
            BlockPos pos,
            ResourceLocation type,
            @Nullable String name,
            Component successMessage,
            Component failureMessage
    ) {
        VoidPocketManager.preserveAndExit(player, pos, type, name, successMessage, failureMessage);
    }
}
