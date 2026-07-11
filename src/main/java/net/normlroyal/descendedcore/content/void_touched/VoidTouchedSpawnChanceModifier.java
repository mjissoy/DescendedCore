package net.normlroyal.descendedcore.content.void_touched;

@FunctionalInterface
public interface VoidTouchedSpawnChanceModifier {
    double modifyChance(VoidTouchedSpawnChanceContext context);
}
