package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.resources.ResourceLocation;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class VoidPocketIslandGenerators {
    private static final Map<ResourceLocation, VoidPocketIslandGenerator> GENERATORS = new LinkedHashMap<>();

    private VoidPocketIslandGenerators() {
    }

    public static synchronized void register(VoidPocketIslandGenerator generator) {
        VoidPocketIslandGenerator previous = GENERATORS.putIfAbsent(generator.id(), generator);
        if (previous != null && previous != generator) {
            throw new IllegalStateException("Duplicate Void Pocket island generator: " + generator.id());
        }
    }

    public static void generateStage(VoidPocketGenerationStage stage, VoidPocketGenerationContext baseContext) {
        List<VoidPocketIslandGenerator> ordered = new ArrayList<>();
        for (VoidPocketIslandGenerator generator : GENERATORS.values()) {
            if (generator.stage() == stage) {
                ordered.add(generator);
            }
        }
        ordered.sort(Comparator.comparingInt(VoidPocketIslandGenerator::priority)
                .thenComparing(generator -> generator.id().toString()));

        for (VoidPocketIslandGenerator generator : ordered) {
            long salt = stableHash(generator.id());
            long seed = baseContext.pocket().id.getMostSignificantBits()
                    ^ baseContext.pocket().id.getLeastSignificantBits()
                    ^ salt;
            VoidPocketGenerationContext context = new VoidPocketGenerationContext(
                    baseContext.level(),
                    baseContext.pocket(),
                    new Random(seed),
                    baseContext.protectedPositions()
            );

            double chance = Math.max(0.0D, Math.min(1.0D, generator.chance(context)));
            if (chance <= 0.0D || context.random().nextDouble() > chance || !generator.canGenerate(context)) {
                continue;
            }
            generator.generate(context);
        }
    }

    private static long stableHash(ResourceLocation id) {
        byte[] bytes = id.toString().getBytes(StandardCharsets.UTF_8);
        long hash = 0xcbf29ce484222325L;
        for (byte value : bytes) {
            hash ^= value & 0xffL;
            hash *= 0x100000001b3L;
        }
        return hash;
    }
}
