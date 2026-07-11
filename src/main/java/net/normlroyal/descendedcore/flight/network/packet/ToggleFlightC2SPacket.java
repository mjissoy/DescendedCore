package net.normlroyal.descendedcore.flight.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.normlroyal.descendedcore.flight.FlightSystem;
import net.normlroyal.descendedcore.network.NetworkContextHelper;

import java.util.function.Supplier;

public record ToggleFlightC2SPacket() {
    public static void encode(ToggleFlightC2SPacket message, FriendlyByteBuf buffer) {
    }

    public static ToggleFlightC2SPacket decode(FriendlyByteBuf buffer) {
        return new ToggleFlightC2SPacket();
    }

    public static void handle(ToggleFlightC2SPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkContextHelper.enqueueServer(contextSupplier, player -> FlightSystem.toggleFlight(player));
    }
}
