package net.normlroyal.descendedcore.flight.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.normlroyal.descendedcore.flight.FlightControllers;
import net.normlroyal.descendedcore.network.NetworkContextHelper;

import java.util.function.Supplier;

public record TryStartGlideC2SPacket() {
    public static void encode(TryStartGlideC2SPacket message, FriendlyByteBuf buffer) {
    }

    public static TryStartGlideC2SPacket decode(FriendlyByteBuf buffer) {
        return new TryStartGlideC2SPacket();
    }

    public static void handle(TryStartGlideC2SPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkContextHelper.enqueueServer(contextSupplier, player ->
                FlightControllers.findGlide(player).ifPresent(controller -> {
                    if (controller.canStartGlide(player)) {
                        player.startFallFlying();
                        player.fallDistance = 0.0F;
                    }
                })
        );
    }
}
