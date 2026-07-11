package net.normlroyal.descendedcore.content.entity.imp;

import net.minecraft.world.entity.LivingEntity;
import net.normlroyal.descendedcore.content.entity.ImpEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ImpTargetRules {
    private static final List<Entry> RULES = new CopyOnWriteArrayList<>();

    private ImpTargetRules() {
    }

    public static void register(ImpTargetRule rule) {
        register(0, rule);
    }

    public static void register(int priority, ImpTargetRule rule) {
        RULES.add(new Entry(priority, Objects.requireNonNull(rule, "rule")));
        RULES.sort(Comparator.comparingInt(Entry::priority).reversed());
    }

    public static boolean canTarget(ImpEntity imp, LivingEntity target) {
        ImpTargetContext context = new ImpTargetContext(imp, target);

        for (Entry entry : RULES) {
            ImpTargetDecision decision = entry.rule().evaluate(context);
            if (decision == ImpTargetDecision.ALLOW) {
                return true;
            }
            if (decision == ImpTargetDecision.DENY) {
                return false;
            }
        }

        return true;
    }

    private record Entry(int priority, ImpTargetRule rule) {
    }
}
