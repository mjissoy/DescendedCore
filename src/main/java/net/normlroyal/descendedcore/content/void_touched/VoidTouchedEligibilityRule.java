package net.normlroyal.descendedcore.content.void_touched;

import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface VoidTouchedEligibilityRule {
    VoidTouchedDecision evaluate(LivingEntity entity);
}
