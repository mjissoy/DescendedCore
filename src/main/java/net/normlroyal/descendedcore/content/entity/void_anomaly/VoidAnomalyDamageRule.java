package net.normlroyal.descendedcore.content.entity.void_anomaly;

@FunctionalInterface
public interface VoidAnomalyDamageRule {
    VoidAnomalyDamageDecision evaluate(VoidAnomalyDamageContext context);
}
