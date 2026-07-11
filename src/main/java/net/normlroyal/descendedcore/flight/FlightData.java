package net.normlroyal.descendedcore.flight;

import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FlightData {
    private static final Map<UUID, SimpleFlightData> DATA = new ConcurrentHashMap<>();

    private FlightData() {
    }

    public static IFlightData get(ServerPlayer player) {
        return DATA.computeIfAbsent(player.getUUID(), ignored -> new SimpleFlightData());
    }

    public static void clear(ServerPlayer player) {
        if (player != null) {
            DATA.remove(player.getUUID());
        }
    }

    public static void clear(UUID uuid) {
        if (uuid != null) {
            DATA.remove(uuid);
        }
    }

    private static final class SimpleFlightData implements IFlightData {
        private final FlightState state = new FlightState();

        @Override
        public FlightState state() {
            return state;
        }

        @Override
        public void sync(ServerPlayer player) {
            FlightSystem.syncState(player);
        }
    }
}
