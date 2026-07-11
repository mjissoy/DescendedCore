package net.normlroyal.descendedcore.content.entity.void_anomaly;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class VoidAnomalyDamageRules {
    private static final List<Entry> RULES = new CopyOnWriteArrayList<>();

    private VoidAnomalyDamageRules() {
    }

    public static void register(VoidAnomalyDamageRule rule) {
        register(0, rule);
    }

    public static void register(int priority, VoidAnomalyDamageRule rule) {
        RULES.add(new Entry(priority, Objects.requireNonNull(rule, "rule")));
        RULES.sort(Comparator.comparingInt(Entry::priority).reversed());
    }

    public static boolean canBeHurt(VoidAnomalyDamageContext context) {
        for (Entry entry : RULES) {
            VoidAnomalyDamageDecision decision = entry.rule().evaluate(context);
            if (decision == VoidAnomalyDamageDecision.ALLOW) {
                return true;
            }
            if (decision == VoidAnomalyDamageDecision.DENY) {
                return false;
            }
        }
        return false;
    }

    private record Entry(int priority, VoidAnomalyDamageRule rule) {
    }
}
