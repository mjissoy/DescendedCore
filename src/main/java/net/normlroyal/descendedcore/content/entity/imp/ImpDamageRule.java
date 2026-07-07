package net.normlroyal.descendedcore.content.entity.imp;

import net.minecraft.world.entity.Entity;
import net.normlroyal.descendedcore.content.entity.ImpEntity;

@FunctionalInterface
public interface ImpDamageRule {
    float modifyDamage(ImpEntity imp, Entity target, float damage);
}
