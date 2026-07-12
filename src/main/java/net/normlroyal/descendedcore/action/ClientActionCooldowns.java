package net.normlroyal.descendedcore.action;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientActionCooldowns {
    private static final Map<ResourceLocation, ActionCooldownSnapshot> SNAPSHOTS = new ConcurrentHashMap<>();

    private ClientActionCooldowns() {
    }

    public static void set(ResourceLocation actionId, long until, int totalTicks) {
        SNAPSHOTS.put(actionId, new ActionCooldownSnapshot(until, totalTicks));
    }

    public static ActionCooldownSnapshot get(ResourceLocation actionId) {
        return SNAPSHOTS.getOrDefault(actionId, new ActionCooldownSnapshot(0L, 0));
    }

    public static int remainingTicks(ResourceLocation actionId, long now) {
        return get(actionId).remainingTicks(now);
    }

    public static int totalTicks(ResourceLocation actionId) {
        return get(actionId).totalTicks();
    }

    public static void clear() {
        SNAPSHOTS.clear();
    }
}
