package net.normlroyal.descendedcore.content.entity.imp;

@FunctionalInterface
public interface ImpDamageRule {
    float modifyDamage(ImpDamageContext context);
}
