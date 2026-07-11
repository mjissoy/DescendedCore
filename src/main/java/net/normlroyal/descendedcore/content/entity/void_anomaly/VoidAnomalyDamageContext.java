package net.normlroyal.descendedcore.content.entity.void_anomaly;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;

public record VoidAnomalyDamageContext(
        Mob anomaly,
        DamageSource source,
        float amount
) {
}
