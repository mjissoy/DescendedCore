package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public final class VoidPocketApi {
    private VoidPocketApi() {
    }

    public static ResourceKey<Level> dimension() {
        return VoidPocketDimensions.VOID_POCKET_LEVEL;
    }

    public static boolean isVoidPocket(Level level) {
        return VoidPocketDimensions.isVoidPocket(level);
    }

    public static VoidPocketData data(MinecraftServer server) {
        return VoidPocketData.get(server);
    }

    public static void registerPreserver(VoidPocketPreserverType type) {
        VoidPocketPreservers.register(type);
    }

    public static void registerIslandGenerator(VoidPocketIslandGenerator generator) {
        VoidPocketIslandGenerators.register(generator);
    }

    public static void registerRuntimeEvent(VoidPocketRuntimeEvent event) {
        VoidPocketRuntimeEvents.register(event);
    }

    public static void registerLifecycleListener(VoidPocketLifecycleListener listener) {
        VoidPocketLifecycle.register(listener);
    }
}
