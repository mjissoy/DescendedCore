package net.normlroyal.descendedcore.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public final class CurioEquipmentHelper {
    private CurioEquipmentHelper() {
    }

    public static ItemStack getStack(LivingEntity entity, String slotId, int index) {
        if (entity == null || slotId == null || slotId.isBlank() || index < 0) {
            return ItemStack.EMPTY;
        }

        return CuriosApi.getCuriosInventory(entity)
                .resolve()
                .flatMap(inventory -> inventory.getStacksHandler(slotId))
                .map(handler -> index < handler.getStacks().getSlots()
                        ? handler.getStacks().getStackInSlot(index)
                        : ItemStack.EMPTY)
                .orElse(ItemStack.EMPTY);
    }

    public static ItemStack getFirstStack(LivingEntity entity, String slotId) {
        return getStack(entity, slotId, 0);
    }
}
