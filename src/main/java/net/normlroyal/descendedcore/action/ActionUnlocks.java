package net.normlroyal.descendedcore.action;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public final class ActionUnlocks {
    private ActionUnlocks() {
    }

    public static boolean has(Player player, String key) {
        return player != null && player.getPersistentData().getBoolean(key);
    }

    public static boolean unlock(ServerPlayer player, String key) {
        if (has(player, key)) {
            return false;
        }
        player.getPersistentData().putBoolean(key, true);
        return true;
    }

    public static void set(ServerPlayer player, String key, boolean unlocked) {
        player.getPersistentData().putBoolean(key, unlocked);
    }

    public static int count(Player player, Collection<String> keys) {
        int count = 0;
        for (String key : keys) {
            if (has(player, key)) {
                count++;
            }
        }
        return count;
    }
}
