package net.normlroyal.descendedcore.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class VTPProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public VTPProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                   double x, double y, double z,
                                   double xd, double yd, double zd) {
        return new VoidTouchedParticle(level, x, y, z, xd, yd, zd, sprites);
    }
}
