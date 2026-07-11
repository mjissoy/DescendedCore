package net.normlroyal.descendedcore.content.void_touched;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


public final class VoidTouchedSpawnChanceModifiers {
    private static final List<Entry> MODIFIERS = new CopyOnWriteArrayList<>();

    private VoidTouchedSpawnChanceModifiers() {
    }

    public static void register(VoidTouchedSpawnChanceModifier modifier) {
        register(0, modifier);
    }

    public static void register(int priority, VoidTouchedSpawnChanceModifier modifier) {
        MODIFIERS.add(new Entry(priority, Objects.requireNonNull(modifier, "modifier")));
        MODIFIERS.sort(Comparator.comparingInt(Entry::priority));
    }

    public static double modify(LivingEntity entity, double baseChance) {
        double result = Mth.clamp(baseChance, 0.0D, 1.0D);

        for (Entry entry : MODIFIERS) {
            result = entry.modifier().modifyChance(
                    new VoidTouchedSpawnChanceContext(entity, baseChance, result)
            );

            if (!Double.isFinite(result)) {
                result = baseChance;
            }
            result = Mth.clamp(result, 0.0D, 1.0D);
        }

        return result;
    }

    private record Entry(int priority, VoidTouchedSpawnChanceModifier modifier) {
    }
}
