package net.normlroyal.descendedcore.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.ParticleRenderType;

public class VoidTouchedParticle extends TextureSheetParticle {

    protected VoidTouchedParticle(ClientLevel level, double x, double y, double z,
                                  double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);

        this.quadSize *= 0.5f;

        this.lifetime = 20 + level.random.nextInt(25);
        this.gravity = 0.0f;
        this.friction = 0.90f;
        this.xd *= 0.2;
        this.yd *= 0.2;
        this.zd *= 0.2;

        float t = level.random.nextFloat();

        this.rCol = 0.18f + 0.07f * t;
        this.gCol = 0.18f + 0.07f * t;
        this.bCol = 0.30f + 0.15f * t;

        this.alpha = 0.9f;

        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        this.yd += 0.002;

        if (this.age > this.lifetime * 0.7f) {
            this.alpha *= 0.92f;
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
