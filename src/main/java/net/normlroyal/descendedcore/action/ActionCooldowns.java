package net.normlroyal.descendedcore.action;

import net.minecraft.server.level.ServerPlayer;

public final class ActionCooldowns {
    private ActionCooldowns() {
    }

    public static boolean isReady(ServerPlayer player, ActionDefinition action) {
        return player.level().getGameTime() >= until(player, action);
    }

    public static long until(ServerPlayer player, ActionDefinition action) {
        return player.getPersistentData().getLong(action.cooldownKey());
    }

    public static int remainingTicks(ServerPlayer player, ActionDefinition action) {
        long remaining = until(player, action) - player.level().getGameTime();
        return (int) Math.max(0L, Math.min(Integer.MAX_VALUE, remaining));
    }

    public static ActionCooldownSnapshot snapshot(ServerPlayer player, ActionDefinition action) {
        return new ActionCooldownSnapshot(until(player, action), Math.max(0, action.cooldownTicks(player)));
    }

    public static ActionCooldownSnapshot start(ServerPlayer player, ActionDefinition action) {
        return start(player, action, action.cooldownTicks(player));
    }

    public static ActionCooldownSnapshot start(ServerPlayer player, ActionDefinition action, int totalTicks) {
        int safeTotal = Math.max(0, totalTicks);
        long until = player.level().getGameTime() + safeTotal;
        player.getPersistentData().putLong(action.cooldownKey(), until);
        return new ActionCooldownSnapshot(until, safeTotal);
    }

    public static void clear(ServerPlayer player, ActionDefinition action) {
        player.getPersistentData().remove(action.cooldownKey());
    }
}
