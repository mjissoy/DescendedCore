package net.normlroyal.descendedcore.flight;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class ClientFlightState {
    private static final FlightState STATE = new FlightState();
    private static FlightInput input = FlightInput.ZERO;

    private ClientFlightState() {
    }

    public static boolean isActive() {
        return STATE.isActive();
    }

    public static boolean isActive(ResourceLocation controllerId) {
        return STATE.isControlledBy(controllerId);
    }

    @Nullable
    public static ResourceLocation controllerId() {
        return STATE.controllerId();
    }

    public static void applyServerState(boolean active, @Nullable ResourceLocation controllerId) {
        if (!active || controllerId == null || FlightControllers.get(controllerId).isEmpty()) {
            reset();
            return;
        }

        if (!STATE.isControlledBy(controllerId)) {
            STATE.activate(controllerId);
        }
    }

    public static FlightState state() {
        return STATE;
    }

    public static FlightInput input() {
        return input;
    }

    public static void setInput(FlightInput value) {
        input = value == null ? FlightInput.ZERO : value.sanitized();
    }

    public static void reset() {
        input = FlightInput.ZERO;
        STATE.deactivate();
    }
}
