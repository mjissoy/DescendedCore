package net.normlroyal.descendedcore.flight.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.normlroyal.descendedcore.flight.FlightInput;
import net.normlroyal.descendedcore.flight.FlightSystem;
import net.normlroyal.descendedcore.network.NetworkContextHelper;

import java.util.function.Supplier;

public record FlightInputC2SPacket(
        boolean ascend,
        boolean descend,
        boolean boost,
        float forward,
        float strafe
) {
    public FlightInputC2SPacket(FlightInput input) {
        this(input.ascend(), input.descend(), input.boost(), input.forward(), input.strafe());
    }

    public static void encode(FlightInputC2SPacket message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.ascend());
        buffer.writeBoolean(message.descend());
        buffer.writeBoolean(message.boost());
        buffer.writeFloat(message.forward());
        buffer.writeFloat(message.strafe());
    }

    public static FlightInputC2SPacket decode(FriendlyByteBuf buffer) {
        return new FlightInputC2SPacket(
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readFloat(),
                buffer.readFloat()
        );
    }

    public static void handle(FlightInputC2SPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkContextHelper.enqueueServer(contextSupplier, player ->
                FlightSystem.receiveInput(player, new FlightInput(
                        message.ascend(),
                        message.descend(),
                        message.boost(),
                        message.forward(),
                        message.strafe()
                ).sanitized())
        );
    }
}
