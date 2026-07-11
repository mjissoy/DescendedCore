package net.normlroyal.descendedcore.content.entity.imp;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.normlroyal.descendedcore.content.entity.ImpEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ImpDamageRules {
    private static final List<Entry> RULES = new CopyOnWriteArrayList<>();

    private ImpDamageRules() {
    }

    public static void register(ImpDamageRule rule) {
        register(0, rule);
    }

    public static void register(int priority, ImpDamageRule rule) {
        RULES.add(new Entry(priority, Objects.requireNonNull(rule, "rule")));
        RULES.sort(Comparator.comparingInt(Entry::priority));
    }

    public static float modifyDamage(
            ImpEntity imp,
            Entity target,
            DamageSource source,
            float baseDamage
    ) {
        float result = baseDamage;

        for (Entry entry : RULES) {
            result = entry.rule().modifyDamage(
                    new ImpDamageContext(imp, target, source, baseDamage, result)
            );

            if (!Float.isFinite(result)) {
                result = baseDamage;
            }
            result = Math.max(0.0F, result);
        }

        return result;
    }

    private record Entry(int priority, ImpDamageRule rule) {
    }
}
