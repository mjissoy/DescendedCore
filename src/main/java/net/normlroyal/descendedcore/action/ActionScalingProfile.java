package net.normlroyal.descendedcore.action;

import net.minecraft.world.entity.player.Player;

public interface ActionScalingProfile {
    int mastery(Player player);

    default double scaleMagnitude(Player player, double base, double perLevelFraction) {
        return ActionScaling.scale(base, mastery(player), perLevelFraction);
    }

    default int addInt(Player player, int base, int perLevel) {
        return ActionScaling.addInt(base, mastery(player), perLevel);
    }

    default int addIntCapped(Player player, int base, int perLevel, int max) {
        return ActionScaling.addIntCapped(base, mastery(player), perLevel, max);
    }

    default int scaleDuration(Player player, int baseTicks, double perLevelFraction) {
        return ActionScaling.scaleDuration(baseTicks, mastery(player), perLevelFraction);
    }

    default int scaleCooldown(Player player, int baseTicks, double multiplier) {
        return ActionScaling.scaleCooldown(baseTicks, multiplier);
    }
}
