package net.normlroyal.descendedcore.flight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

public final class FlightControllers {
    private static final Map<ResourceLocation, FlightController> BY_ID = new ConcurrentHashMap<>();
    private static volatile List<FlightController> ordered = List.of();

    private FlightControllers() {
    }

    public static synchronized void register(FlightController controller) {
        if (controller == null || controller.id() == null) {
            throw new IllegalArgumentException("Flight controllers require a non-null controller and id");
        }

        FlightController existing = BY_ID.get(controller.id());
        if (existing != null) {
            if (existing.getClass() == controller.getClass()) {
                return;
            }
            throw new IllegalStateException("Duplicate flight controller id: " + controller.id());
        }

        BY_ID.put(controller.id(), controller);
        ArrayList<FlightController> rebuilt = new ArrayList<>(BY_ID.values());
        rebuilt.sort(Comparator.comparingInt(FlightController::priority).reversed());
        ordered = List.copyOf(rebuilt);
    }

    public static Optional<FlightController> get(ResourceLocation id) {
        return id == null ? Optional.empty() : Optional.ofNullable(BY_ID.get(id));
    }

    public static Optional<FlightController> findPowered(Player player) {
        return ordered.stream().filter(controller -> controller.supportsPoweredFlight(player)).findFirst();
    }

    public static Optional<FlightController> findGlide(Player player) {
        return ordered.stream().filter(controller -> controller.supportsGlide(player)).findFirst();
    }

    public static boolean canGlide(Player player) {
        return findGlide(player).map(controller -> controller.canMaintainGlide(player)).orElse(false);
    }

    public static List<FlightController> all() {
        return ordered;
    }
}
