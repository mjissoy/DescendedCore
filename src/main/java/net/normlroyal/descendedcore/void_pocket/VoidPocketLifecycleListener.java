package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface VoidPocketLifecycleListener {
    default void onCreated(ServerLevel level, VoidPocketData.Pocket pocket, ServerPlayer creator) {
    }

    default void onGenerated(ServerLevel level, VoidPocketData.Pocket pocket) {
    }

    default void onEntered(ServerLevel level, VoidPocketData.Pocket pocket, ServerPlayer player) {
    }

    default void onExited(VoidPocketData.Pocket pocket, ServerPlayer player) {
    }

    default void onCompleted(ServerLevel level, VoidPocketData.Pocket pocket, ServerPlayer player) {
    }

    default void onRemoved(ServerLevel level, VoidPocketData.Pocket pocket) {
    }
}
