package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class VoidPocketPreservers {
    public static final ResourceLocation LEGACY_ANGELIC_ANCHOR =
            new ResourceLocation("descendedangel", "angelic_anchor");

    private static final Map<ResourceLocation, VoidPocketPreserverType> TYPES = new LinkedHashMap<>();

    private VoidPocketPreservers() {
    }

    public static synchronized void register(VoidPocketPreserverType type) {
        VoidPocketPreserverType previous = TYPES.putIfAbsent(type.id(), type);
        if (previous != null && previous != type) {
            throw new IllegalStateException("Duplicate Void Pocket preserver type: " + type.id());
        }
    }

    public static Optional<VoidPocketPreserverType> get(ResourceLocation id) {
        return Optional.ofNullable(TYPES.get(id));
    }

    public static Optional<VoidPocketPreserverType> find(BlockState state) {
        return TYPES.values().stream().filter(type -> type.matches(state)).findFirst();
    }

    public static boolean isPreserver(BlockState state) {
        return find(state).isPresent();
    }
}
