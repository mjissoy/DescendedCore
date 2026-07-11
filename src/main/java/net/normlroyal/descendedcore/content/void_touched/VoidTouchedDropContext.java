package net.normlroyal.descendedcore.content.void_touched;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record VoidTouchedDropContext(
        LivingEntity entity,
        DamageSource source,
        int lootingLevel,
        ItemStack currentDrop
) {
    public VoidTouchedDropContext withCurrentDrop(ItemStack stack) {
        return new VoidTouchedDropContext(entity, source, lootingLevel, stack);
    }
}
