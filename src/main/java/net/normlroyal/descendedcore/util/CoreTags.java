package net.normlroyal.descendedcore.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.normlroyal.descendedcore.DescendedCore;

public final class CoreTags {
    public static final TagKey<Block> VOID_DECORATION_SUPPORT = TagKey.create(
            Registries.BLOCK,
            new ResourceLocation(DescendedCore.MOD_ID, "void_decoration_support")
    );

    private CoreTags() {
    }
}
