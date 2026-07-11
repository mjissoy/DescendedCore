package net.normlroyal.descendedcore.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NetworkContextHelper {
    private NetworkContextHelper() {
    }

    public static void enqueue(Supplier<NetworkEvent.Context> contextSupplier, Runnable work) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(work);
        context.setPacketHandled(true);
    }

    public static void enqueueServer(
            Supplier<NetworkEvent.Context> contextSupplier,
            Consumer<ServerPlayer> work
    ) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer sender = context.getSender();
        if (sender != null) {
            context.enqueueWork(() -> work.accept(sender));
        }
        context.setPacketHandled(true);
    }
}
