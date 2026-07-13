package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class VoidPocketLifecycle {
    private static final List<VoidPocketLifecycleListener> LISTENERS = new CopyOnWriteArrayList<>();

    private VoidPocketLifecycle() {
    }

    public static void register(VoidPocketLifecycleListener listener) {
        LISTENERS.add(listener);
    }

    static void created(ServerLevel level, VoidPocketData.Pocket pocket, ServerPlayer player) {
        LISTENERS.forEach(listener -> listener.onCreated(level, pocket, player));
    }

    static void generated(ServerLevel level, VoidPocketData.Pocket pocket) {
        LISTENERS.forEach(listener -> listener.onGenerated(level, pocket));
    }

    static void entered(ServerLevel level, VoidPocketData.Pocket pocket, ServerPlayer player) {
        LISTENERS.forEach(listener -> listener.onEntered(level, pocket, player));
    }

    static void exited(VoidPocketData.Pocket pocket, ServerPlayer player) {
        LISTENERS.forEach(listener -> listener.onExited(pocket, player));
    }

    static void completed(ServerLevel level, VoidPocketData.Pocket pocket, ServerPlayer player) {
        LISTENERS.forEach(listener -> listener.onCompleted(level, pocket, player));
    }

    static void removed(ServerLevel level, VoidPocketData.Pocket pocket) {
        LISTENERS.forEach(listener -> listener.onRemoved(level, pocket));
    }
}
