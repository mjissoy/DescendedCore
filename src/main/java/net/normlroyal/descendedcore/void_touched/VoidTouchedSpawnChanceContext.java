package net.normlroyal.descendedcore.void_touched;

import net.minecraft.world.entity.LivingEntity;

public record VoidTouchedSpawnChanceContext(
        LivingEntity entity,
        double baseChance,
        double currentChance
) {
}
