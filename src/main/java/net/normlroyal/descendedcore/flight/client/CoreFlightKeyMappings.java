package net.normlroyal.descendedcore.flight.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.DescendedCore;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CoreFlightKeyMappings {
    public static KeyMapping FLIGHT_BOOST;

    private CoreFlightKeyMappings() {
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        FLIGHT_BOOST = new KeyMapping(
                "key.descendedcore.flight_boost",
                GLFW.GLFW_KEY_LEFT_CONTROL,
                "key.categories.descendedcore"
        );
        event.register(FLIGHT_BOOST);
    }
}
