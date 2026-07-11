package net.normlroyal.descendedcore.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.normlroyal.descendedcore.flight.FlightControllers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    protected MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyArg(
            method = "updateFallFlying",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V"
            )
    )
    private boolean descendedcore$allowRegisteredGlide(boolean vanillaValue) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (vanillaValue || !(self instanceof Player player)) {
            return vanillaValue;
        }

        if (!getSharedFlag(7)) {
            return false;
        }

        return FlightControllers.canGlide(player);
    }
}
