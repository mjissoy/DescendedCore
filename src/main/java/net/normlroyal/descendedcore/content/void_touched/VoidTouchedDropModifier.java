package net.normlroyal.descendedcore.content.void_touched;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface VoidTouchedDropModifier {
    ItemStack modifyDrop(VoidTouchedDropContext context);
}
