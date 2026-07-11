package net.normlroyal.descendedcore.content.entity.imp;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.normlroyal.descendedcore.content.entity.ImpEntity;

public record ImpDamageContext(
        ImpEntity imp,
        Entity target,
        DamageSource source,
        float baseDamage,
        float currentDamage
) {
    public ImpDamageContext withCurrentDamage(float damage) {
        return new ImpDamageContext(imp, target, source, baseDamage, damage);
    }
}
