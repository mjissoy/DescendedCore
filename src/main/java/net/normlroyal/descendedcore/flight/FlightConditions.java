package net.normlroyal.descendedcore.flight;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public final class FlightConditions {
    private FlightConditions() {
    }

    public static boolean canStartPoweredFlight(Player player) {
        return canContinuePoweredFlight(player)
                && !player.hasEffect(MobEffects.LEVITATION);
    }

    public static boolean canContinuePoweredFlight(Player player) {
        return player != null
                && player.isAlive()
                && !player.onGround()
                && !player.isPassenger()
                && !player.isCreative()
                && !player.isSpectator();
    }

    public static boolean canStartGlide(Player player) {
        return player != null
                && player.isAlive()
                && !player.onGround()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.LEVITATION)
                && !player.isPassenger()
                && !player.isFallFlying()
                && player.getDeltaMovement().y < 0.0D;
    }

    public static boolean canMaintainGlide(Player player) {
        return player != null
                && player.isAlive()
                && !player.onGround()
                && !player.isPassenger()
                && !player.hasEffect(MobEffects.LEVITATION);
    }
}
