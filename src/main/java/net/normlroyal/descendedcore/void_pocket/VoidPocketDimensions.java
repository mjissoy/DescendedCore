package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;


public final class VoidPocketDimensions {
    public static final ResourceLocation ID = new ResourceLocation("descendedangel", "void_pocket");

    public static final ResourceKey<Level> VOID_POCKET_LEVEL =
            ResourceKey.create(Registries.DIMENSION, ID);

    public static final ResourceKey<DimensionType> VOID_POCKET_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, ID);

    private VoidPocketDimensions() {
    }

    public static boolean isVoidPocket(Level level) {
        return level.dimension().equals(VOID_POCKET_LEVEL);
    }
}
