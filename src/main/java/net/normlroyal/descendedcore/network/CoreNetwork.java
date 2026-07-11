package net.normlroyal.descendedcore.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.flight.network.packet.FlightActiveS2CPacket;
import net.normlroyal.descendedcore.flight.network.packet.FlightInputC2SPacket;
import net.normlroyal.descendedcore.flight.network.packet.StopFlightC2SPacket;
import net.normlroyal.descendedcore.flight.network.packet.ToggleFlightC2SPacket;
import net.normlroyal.descendedcore.flight.network.packet.TryStartGlideC2SPacket;

public final class CoreNetwork {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DescendedCore.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static int nextId;
    private static boolean registered;

    private CoreNetwork() {
    }

    public static synchronized void registerPackets() {
        if (registered) {
            return;
        }
        registered = true;

        CHANNEL.messageBuilder(ToggleFlightC2SPacket.class, nextId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ToggleFlightC2SPacket::encode)
                .decoder(ToggleFlightC2SPacket::decode)
                .consumerMainThread(ToggleFlightC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(StopFlightC2SPacket.class, nextId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(StopFlightC2SPacket::encode)
                .decoder(StopFlightC2SPacket::decode)
                .consumerMainThread(StopFlightC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(FlightInputC2SPacket.class, nextId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(FlightInputC2SPacket::encode)
                .decoder(FlightInputC2SPacket::decode)
                .consumerMainThread(FlightInputC2SPacket::handle)
                .add();

        CHANNEL.messageBuilder(FlightActiveS2CPacket.class, nextId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(FlightActiveS2CPacket::encode)
                .decoder(FlightActiveS2CPacket::decode)
                .consumerMainThread(FlightActiveS2CPacket::handle)
                .add();

        CHANNEL.messageBuilder(TryStartGlideC2SPacket.class, nextId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(TryStartGlideC2SPacket::encode)
                .decoder(TryStartGlideC2SPacket::decode)
                .consumerMainThread(TryStartGlideC2SPacket::handle)
                .add();
    }

    public static void sendToPlayer(Object packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToTrackingAndSelf(Object packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), packet);
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }
}
