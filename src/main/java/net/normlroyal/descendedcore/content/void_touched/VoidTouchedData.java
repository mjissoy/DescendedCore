package net.normlroyal.descendedcore.content.void_touched;

import net.minecraft.world.entity.LivingEntity;

public final class VoidTouchedData {
    public static final String LEGACY_NBT_KEY = "descendedangel:void_touched";

    private VoidTouchedData() {
    }

    public static boolean isVoidTouched(LivingEntity entity) {
        return entity.getPersistentData().getBoolean(LEGACY_NBT_KEY);
    }

    public static void setVoidTouched(LivingEntity entity, boolean value) {
        if (value) {
            entity.getPersistentData().putBoolean(LEGACY_NBT_KEY, true);
        } else {
            entity.getPersistentData().remove(LEGACY_NBT_KEY);
        }
    }

    public static boolean tryApplyNaturally(LivingEntity entity, double chance) {
        if (isVoidTouched(entity)
                || !VoidTouchedEligibilityRules.canBecomeVoidTouched(entity)
                || chance <= 0.0D) {
            return false;
        }

        if (chance >= 1.0D || entity.getRandom().nextDouble() < chance) {
            setVoidTouched(entity, true);
            return true;
        }

        return false;
    }
}
