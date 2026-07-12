package net.normlroyal.descendedcore.void_touched;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class VoidTouchedDropModifiers {
    private static final List<Entry> MODIFIERS = new CopyOnWriteArrayList<>();

    private VoidTouchedDropModifiers() {
    }

    public static void register(VoidTouchedDropModifier modifier) {
        register(0, modifier);
    }

    public static void register(int priority, VoidTouchedDropModifier modifier) {
        MODIFIERS.add(new Entry(priority, Objects.requireNonNull(modifier, "modifier")));
        MODIFIERS.sort(Comparator.comparingInt(Entry::priority));
    }

    public static ItemStack modify(
            LivingEntity entity,
            DamageSource source,
            int lootingLevel,
            ItemStack initialDrop
    ) {
        ItemStack result = initialDrop;

        for (Entry entry : MODIFIERS) {
            ItemStack modified = entry.modifier().modifyDrop(
                    new VoidTouchedDropContext(entity, source, lootingLevel, result)
            );
            result = modified == null ? ItemStack.EMPTY : modified;
        }

        return result;
    }

    private record Entry(int priority, VoidTouchedDropModifier modifier) {
    }
}
