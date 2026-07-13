package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.resources.ResourceLocation;

public interface VoidPocketRuntimeEvent {
    ResourceLocation id();

    default int weight() {
        return 1;
    }

    default long minimumIntervalTicks() {
        return 600L;
    }

    default double triggerChance(VoidPocketRuntimeContext context) {
        return 1.0D;
    }

    default boolean canTrigger(VoidPocketRuntimeContext context) {
        return true;
    }

    void trigger(VoidPocketRuntimeContext context);
}
