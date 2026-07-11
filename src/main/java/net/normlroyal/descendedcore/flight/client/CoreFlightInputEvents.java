package net.normlroyal.descendedcore.flight.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.flight.ClientFlightState;
import net.normlroyal.descendedcore.flight.FlightController;
import net.normlroyal.descendedcore.flight.FlightControllers;
import net.normlroyal.descendedcore.flight.FlightInput;
import net.normlroyal.descendedcore.flight.network.packet.FlightInputC2SPacket;
import net.normlroyal.descendedcore.flight.network.packet.StopFlightC2SPacket;
import net.normlroyal.descendedcore.flight.network.packet.ToggleFlightC2SPacket;
import net.normlroyal.descendedcore.flight.network.packet.TryStartGlideC2SPacket;
import net.normlroyal.descendedcore.network.CoreNetwork;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CoreFlightInputEvents {
    private static final int DOUBLE_TAP_WINDOW_TICKS = 7;

    private static boolean wasJumpDown;
    private static int ticksSinceLastJumpPress = 999;
    private static boolean stopRequested;

    private CoreFlightInputEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            resetLocalState();
            return;
        }

        ticksSinceLastJumpPress++;
        handleJumpActivation(minecraft, player);

        if (!ClientFlightState.isActive()) {
            stopRequested = false;
            ClientFlightState.setInput(FlightInput.ZERO);
            return;
        }

        Optional<FlightController> controller = FlightControllers.get(ClientFlightState.controllerId());
        if (controller.isEmpty() || !controller.get().canContinuePoweredFlight(player)) {
            requestStop();
            ClientFlightState.reset();
            return;
        }

        stopRequested = false;
        FlightInput input = readInput(minecraft);
        ClientFlightState.setInput(input);

        controller.get().tick(player, ClientFlightState.state(), input);
        CoreNetwork.sendToServer(new FlightInputC2SPacket(input));
    }

    private static void handleJumpActivation(Minecraft minecraft, LocalPlayer player) {
        boolean jumpDown = minecraft.options.keyJump.isDown();
        boolean pressedThisTick = jumpDown && !wasJumpDown;
        wasJumpDown = jumpDown;

        if (!pressedThisTick || minecraft.screen != null) {
            return;
        }

        Optional<FlightController> glideController = FlightControllers.findGlide(player);
        if (glideController.isPresent() && glideController.get().canStartGlide(player)) {
            player.startFallFlying();
            CoreNetwork.sendToServer(new TryStartGlideC2SPacket());
            ticksSinceLastJumpPress = 999;
            return;
        }

        Optional<FlightController> poweredController = FlightControllers.findPowered(player);
        if (poweredController.isEmpty() || player.isFallFlying()) {
            ticksSinceLastJumpPress = 999;
            return;
        }

        if (ticksSinceLastJumpPress <= DOUBLE_TAP_WINDOW_TICKS) {
            CoreNetwork.sendToServer(new ToggleFlightC2SPacket());
            ticksSinceLastJumpPress = 999;
        } else {
            ticksSinceLastJumpPress = 0;
        }
    }

    private static FlightInput readInput(Minecraft minecraft) {
        boolean ascend = minecraft.options.keyJump.isDown();
        boolean descend = minecraft.options.keyShift.isDown();
        boolean boost = CoreFlightKeyMappings.FLIGHT_BOOST != null
                && CoreFlightKeyMappings.FLIGHT_BOOST.isDown();

        float forward = 0.0F;
        if (minecraft.options.keyUp.isDown()) {
            forward += 1.0F;
        }
        if (minecraft.options.keyDown.isDown()) {
            forward -= 1.0F;
        }

        float strafe = 0.0F;
        if (minecraft.options.keyLeft.isDown()) {
            strafe -= 1.0F;
        }
        if (minecraft.options.keyRight.isDown()) {
            strafe += 1.0F;
        }

        return new FlightInput(ascend, descend, boost, forward, strafe).sanitized();
    }

    private static void requestStop() {
        if (!stopRequested) {
            stopRequested = true;
            CoreNetwork.sendToServer(new StopFlightC2SPacket());
        }
    }

    private static void resetLocalState() {
        wasJumpDown = false;
        ticksSinceLastJumpPress = 999;
        stopRequested = false;
        ClientFlightState.reset();
    }
}
