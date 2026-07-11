package net.normlroyal.descendedcore.content.entity.imp;

@FunctionalInterface
public interface ImpTargetRule {
    ImpTargetDecision evaluate(ImpTargetContext context);
}
