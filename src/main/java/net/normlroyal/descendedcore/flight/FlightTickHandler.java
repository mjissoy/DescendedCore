package net.normlroyal.descendedcore.flight;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.DescendedCore;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FlightTickHandler {
    private FlightTickHandler() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        IFlightData data = FlightData.get(player);
        FlightState state = data.state();
        if (!state.isActive()) {
            return;
        }

        Optional<FlightController> controller = FlightSystem.activeController(state);
        if (controller.isEmpty() || !controller.get().canContinuePoweredFlight(player)) {
            FlightSystem.stopFlight(player);
            return;
        }

        controller.get().tick(player, state, FlightSystem.getInput(player));
        player.fallDistance = 0.0F;
        player.hurtMarked = true;

        if (player.tickCount % 20 == 0) {
            data.sync(player);
        }
    }
}
