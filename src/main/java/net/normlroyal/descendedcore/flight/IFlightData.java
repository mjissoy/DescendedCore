package net.normlroyal.descendedcore.flight;

import net.minecraft.server.level.ServerPlayer;

public interface IFlightData {
    FlightState state();

    default boolean isActive() {
        return state().isActive();
    }

    void sync(ServerPlayer player);
}
