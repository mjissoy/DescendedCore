package net.normlroyal.descendedcore.void_touched;

@FunctionalInterface
public interface VoidTouchedSpawnChanceModifier {
    double modifyChance(VoidTouchedSpawnChanceContext context);
}
