package net.normlroyal.descendedcore.void_touched;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


public final class VoidTouchedEligibilityRules {
    private static final List<Entry> RULES = new CopyOnWriteArrayList<>();

    private VoidTouchedEligibilityRules() {
    }

    public static void register(VoidTouchedEligibilityRule rule) {
        register(0, rule);
    }

    public static void register(int priority, VoidTouchedEligibilityRule rule) {
        RULES.add(new Entry(priority, Objects.requireNonNull(rule, "rule")));
        RULES.sort(Comparator.comparingInt(Entry::priority).reversed());
    }

    public static boolean canBecomeVoidTouched(LivingEntity entity) {
        for (Entry entry : RULES) {
            VoidTouchedDecision decision = entry.rule().evaluate(entity);
            if (decision == VoidTouchedDecision.ALLOW) {
                return true;
            }
            if (decision == VoidTouchedDecision.DENY) {
                return false;
            }
        }
        return entity instanceof Monster;
    }

    private record Entry(int priority, VoidTouchedEligibilityRule rule) {
    }
}
