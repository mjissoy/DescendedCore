package net.normlroyal.descendedcore.client.particle;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.content.particle.CoreParticles;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CoreClientParticleEvents {
    private CoreClientParticleEvents() {
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CoreParticles.VOID_TOUCHED.get(), VTPProvider::new);
        event.registerSpriteSet(CoreParticles.BLUE_PORTAL.get(), BluePortalParticle.Provider::new);
    }
}
