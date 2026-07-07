package net.normlroyal.descendedcore.content.entity.void_anomaly;

import net.minecraft.world.damagesource.DamageSource;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class VoidAnomalyDamageRules {
    private static final List<VoidAnomalyDamageRule> RULES = new CopyOnWriteArrayList<>();

    private VoidAnomalyDamageRules() {
    }

    public static void register(VoidAnomalyDamageRule rule) {
        RULES.add(rule);
    }

    public static boolean canBeHurt(DamageSource source) {
        for (VoidAnomalyDamageRule rule : RULES) {
            if (rule.canBeHurt(source)) {
                return true;
            }
        }
        return false;
    }
}
