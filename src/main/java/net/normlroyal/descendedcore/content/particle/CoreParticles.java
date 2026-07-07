package net.normlroyal.descendedcore.content.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.normlroyal.descendedcore.DescendedCore;

public final class CoreParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DescendedCore.MOD_ID);

    public static final RegistryObject<SimpleParticleType> VOID_TOUCHED =
            PARTICLES.register("void_touched", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> BLUE_PORTAL =
            PARTICLES.register("blue_portal", () -> new SimpleParticleType(true));

    private CoreParticles() {
    }
}
