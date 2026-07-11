package net.normlroyal.descendedcore.content.entity.imp;

import net.minecraft.world.entity.LivingEntity;
import net.normlroyal.descendedcore.content.entity.ImpEntity;

public record ImpTargetContext(
        ImpEntity imp,
        LivingEntity target
) {
}
