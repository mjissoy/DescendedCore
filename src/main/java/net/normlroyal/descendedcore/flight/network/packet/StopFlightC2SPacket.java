package net.normlroyal.descendedcore.flight.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.normlroyal.descendedcore.flight.FlightSystem;
import net.normlroyal.descendedcore.network.NetworkContextHelper;

import java.util.function.Supplier;

public record StopFlightC2SPacket() {
    public static void encode(StopFlightC2SPacket message, FriendlyByteBuf buffer) {
    }

    public static StopFlightC2SPacket decode(FriendlyByteBuf buffer) {
        return new StopFlightC2SPacket();
    }

    public static void handle(StopFlightC2SPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkContextHelper.enqueueServer(contextSupplier, player -> FlightSystem.stopFlight(player));
    }
}
