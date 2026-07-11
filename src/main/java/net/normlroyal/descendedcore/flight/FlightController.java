package net.normlroyal.descendedcore.flight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface FlightController {
    ResourceLocation id();

    default int priority() {
        return 0;
    }

    default boolean supportsPoweredFlight(Player player) {
        return false;
    }

    default boolean supportsGlide(Player player) {
        return false;
    }

    default boolean canStartPoweredFlight(ServerPlayer player) {
        return supportsPoweredFlight(player) && FlightConditions.canStartPoweredFlight(player);
    }

    default boolean canContinuePoweredFlight(Player player) {
        return supportsPoweredFlight(player) && FlightConditions.canContinuePoweredFlight(player);
    }

    default boolean canStartGlide(Player player) {
        return supportsGlide(player) && FlightConditions.canStartGlide(player);
    }

    default boolean canMaintainGlide(Player player) {
        return supportsGlide(player) && FlightConditions.canMaintainGlide(player);
    }

    default void onStart(ServerPlayer player, FlightState state) {
    }

    default void onStop(ServerPlayer player, FlightState state) {
    }

    void tick(Player player, FlightState state, FlightInput input);
}
