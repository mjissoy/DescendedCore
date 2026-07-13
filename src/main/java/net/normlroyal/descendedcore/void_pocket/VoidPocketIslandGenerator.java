package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.resources.ResourceLocation;

public interface VoidPocketIslandGenerator {
    ResourceLocation id();

    default VoidPocketGenerationStage stage() {
        return VoidPocketGenerationStage.AFTER_TERRAIN;
    }

    default int priority() {
        return 0;
    }

    default double chance(VoidPocketGenerationContext context) {
        return 1.0D;
    }

    default boolean canGenerate(VoidPocketGenerationContext context) {
        return true;
    }

    void generate(VoidPocketGenerationContext context);
}
