package net.normlroyal.descendedcore.void_touched;

import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface VoidTouchedEligibilityRule {
    VoidTouchedDecision evaluate(LivingEntity entity);
}
