package net.normlroyal.descendedcore.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class BluePortalParticle extends TextureSheetParticle {
    private final double xStart, yStart, zStart;

    protected BluePortalParticle(ClientLevel level, double x, double y, double z,
                                 double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;

        this.xStart = x;
        this.yStart = y;
        this.zStart = z;

        this.quadSize *= 0.8F;
        this.lifetime = (int)(Math.random() * 10.0D) + 40;

        float variance = 0.08f;
        this.rCol = 0.38f + (random.nextFloat() - 0.5f) * variance;
        this.gCol = 0.50f + (random.nextFloat() - 0.5f) * variance;
        this.bCol = 0.78f + (random.nextFloat() - 0.5f) * variance;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        float f = (float)this.age / (float)this.lifetime;
        float inv = 1.0F - f;
        float curve = inv * inv;
        this.x = this.xStart + this.xd * (double)curve;
        this.y = this.yStart + this.yd * (double)curve;
        this.z = this.zStart + this.zd * (double)curve;

        if (++this.age >= this.lifetime) this.remove();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            return new BluePortalParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}