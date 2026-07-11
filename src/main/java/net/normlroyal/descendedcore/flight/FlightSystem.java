package net.normlroyal.descendedcore.flight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.normlroyal.descendedcore.flight.network.packet.FlightActiveS2CPacket;
import net.normlroyal.descendedcore.network.CoreNetwork;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FlightSystem {
    private static final Map<UUID, FlightInput> INPUTS = new ConcurrentHashMap<>();

    private FlightSystem() {
    }

    public static FlightInput getInput(ServerPlayer player) {
        return INPUTS.getOrDefault(player.getUUID(), FlightInput.ZERO);
    }

    public static void setInput(ServerPlayer player, FlightInput input) {
        if (player != null) {
            INPUTS.put(player.getUUID(), input == null ? FlightInput.ZERO : input.sanitized());
        }
    }

    public static void receiveInput(ServerPlayer player, FlightInput input) {
        if (player == null) {
            return;
        }

        FlightState state = FlightData.get(player).state();
        if (!state.isActive()) {
            syncState(player);
            return;
        }

        Optional<FlightController> controller = activeController(state);
        if (controller.isEmpty() || !controller.get().canContinuePoweredFlight(player)) {
            stopFlight(player);
            return;
        }

        setInput(player, input);
    }

    public static void toggleFlight(ServerPlayer player) {
        if (player == null) {
            return;
        }

        FlightState state = FlightData.get(player).state();
        if (state.isActive()) {
            stopFlight(player);
            return;
        }

        Optional<FlightController> controller = FlightControllers.findPowered(player);
        if (controller.isEmpty() || !controller.get().canStartPoweredFlight(player)) {
            clearAndSync(player);
            return;
        }

        startFlight(player, controller.get());
    }

    public static void startFlight(ServerPlayer player, FlightController controller) {
        if (player == null || controller == null || !controller.canStartPoweredFlight(player)) {
            if (player != null) {
                clearAndSync(player);
            }
            return;
        }

        IFlightData data = FlightData.get(player);
        FlightState state = data.state();

        if (state.isActive()) {
            stopFlight(player);
        }

        if (player.isFallFlying()) {
            player.stopFallFlying();
        }

        player.getAbilities().flying = false;
        player.onUpdateAbilities();

        state.activate(controller.id());
        setInput(player, FlightInput.ZERO);
        controller.onStart(player, state);
        data.sync(player);
    }

    public static void stopFlight(ServerPlayer player) {
        if (player == null) {
            return;
        }

        IFlightData data = FlightData.get(player);
        FlightState state = data.state();

        if (state.isActive()) {
            activeController(state).ifPresent(controller -> controller.onStop(player, state));
        }

        state.deactivate();
        clearInput(player);
        data.sync(player);
    }

    public static void clearAndSync(ServerPlayer player) {
        if (player == null) {
            return;
        }

        IFlightData data = FlightData.get(player);
        data.state().deactivate();
        clearInput(player);
        data.sync(player);
    }

    public static void clearInput(ServerPlayer player) {
        if (player != null) {
            INPUTS.remove(player.getUUID());
        }
    }

    public static void clearAll(ServerPlayer player) {
        if (player != null) {
            clearInput(player);
            FlightData.clear(player);
        }
    }

    public static Optional<FlightController> activeController(FlightState state) {
        ResourceLocation id = state == null ? null : state.controllerId();
        return FlightControllers.get(id);
    }

    public static void syncState(ServerPlayer player) {
        if (player == null) {
            return;
        }

        FlightState state = FlightData.get(player).state();
        CoreNetwork.sendToPlayer(
                new FlightActiveS2CPacket(state.isActive(), state.controllerId()),
                player
        );
    }
}
