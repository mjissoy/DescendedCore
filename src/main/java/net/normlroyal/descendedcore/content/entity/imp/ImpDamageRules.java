package net.normlroyal.descendedcore.content.entity.imp;

import net.minecraft.world.entity.Entity;
import net.normlroyal.descendedcore.content.entity.ImpEntity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ImpDamageRules {
    private static final List<ImpDamageRule> RULES = new CopyOnWriteArrayList<>();

    private ImpDamageRules() {
    }

    public static void register(ImpDamageRule rule) {
        RULES.add(rule);
    }

    public static float modifyDamage(ImpEntity imp, Entity target, float damage) {
        float result = damage;
        for (ImpDamageRule rule : RULES) {
            result = rule.modifyDamage(imp, target, result);
        }
        return result;
    }
}
