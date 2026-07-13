package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class VoidPocketRuntimeEvents {
    private static final Map<ResourceLocation, VoidPocketRuntimeEvent> EVENTS = new LinkedHashMap<>();

    private VoidPocketRuntimeEvents() {
    }

    public static synchronized void register(VoidPocketRuntimeEvent event) {
        VoidPocketRuntimeEvent previous = EVENTS.putIfAbsent(event.id(), event);
        if (previous != null && previous != event) {
            throw new IllegalStateException("Duplicate Void Pocket runtime event: " + event.id());
        }
    }

    public static boolean tryTrigger(VoidPocketRuntimeContext context, long ticksSinceLastEvent) {
        List<VoidPocketRuntimeEvent> candidates = new ArrayList<>();
        int totalWeight = 0;

        for (VoidPocketRuntimeEvent event : EVENTS.values()) {
            int weight = Math.max(0, event.weight());
            double chance = Math.max(0.0D, Math.min(1.0D, event.triggerChance(context)));
            if (weight > 0
                    && ticksSinceLastEvent >= Math.max(0L, event.minimumIntervalTicks())
                    && chance > 0.0D
                    && context.level().random.nextDouble() <= chance
                    && event.canTrigger(context)) {
                candidates.add(event);
                totalWeight += weight;
            }
        }

        if (totalWeight <= 0) {
            return false;
        }

        int roll = context.level().random.nextInt(totalWeight);
        for (VoidPocketRuntimeEvent event : candidates) {
            roll -= Math.max(0, event.weight());
            if (roll < 0) {
                event.trigger(context);
                return true;
            }
        }
        return false;
    }
}
