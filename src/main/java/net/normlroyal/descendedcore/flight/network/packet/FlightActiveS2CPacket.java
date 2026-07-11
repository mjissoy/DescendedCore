package net.normlroyal.descendedcore.flight.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.normlroyal.descendedcore.flight.ClientFlightState;
import net.normlroyal.descendedcore.network.NetworkContextHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record FlightActiveS2CPacket(boolean active, @Nullable ResourceLocation controllerId) {
    public static void encode(FlightActiveS2CPacket message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.active());
        buffer.writeBoolean(message.controllerId() != null);
        if (message.controllerId() != null) {
            buffer.writeResourceLocation(message.controllerId());
        }
    }

    public static FlightActiveS2CPacket decode(FriendlyByteBuf buffer) {
        boolean active = buffer.readBoolean();
        ResourceLocation controllerId = buffer.readBoolean() ? buffer.readResourceLocation() : null;
        return new FlightActiveS2CPacket(active, controllerId);
    }

    public static void handle(FlightActiveS2CPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkContextHelper.enqueue(
                contextSupplier,
                () -> ClientFlightState.applyServerState(message.active(), message.controllerId())
        );
    }
}
