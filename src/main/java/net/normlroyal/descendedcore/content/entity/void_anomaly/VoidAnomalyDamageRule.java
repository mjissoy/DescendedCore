package net.normlroyal.descendedcore.content.entity.void_anomaly;

import net.minecraft.world.damagesource.DamageSource;

@FunctionalInterface
public interface VoidAnomalyDamageRule {
    boolean canBeHurt(DamageSource source);
}
