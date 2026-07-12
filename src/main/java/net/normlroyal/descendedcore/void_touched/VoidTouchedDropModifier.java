package net.normlroyal.descendedcore.void_touched;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface VoidTouchedDropModifier {
    ItemStack modifyDrop(VoidTouchedDropContext context);
}
