package net.normlroyal.descendedcore.content.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.normlroyal.descendedcore.content.entity.void_anomaly.VoidAnomaly;
import net.normlroyal.descendedcore.content.entity.void_anomaly.VoidAnomalyBehavior;

public class VoidSlimeAnomalyEntity extends Slime implements VoidAnomaly {
    private int teleportCooldown = 0;

    public VoidSlimeAnomalyEntity(EntityType<? extends Slime> type, Level level) {
        super(type, level);
        this.setSize(1, true);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide) {
            VoidAnomalyBehavior.spawnAmbientParticles(this, 4, 0.035F);

            if (teleportCooldown > 0) {
                teleportCooldown--;
            } else if (this.getTarget() != null && random.nextFloat() < 0.035F) {
                VoidAnomalyBehavior.attemptShortTeleport(this, 4.0D, 1, 36);
                teleportCooldown = 110;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return VoidAnomalyBehavior.canBeHurt(source) && super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
        }
        return hurt;
    }

    @Override
    public int getVoidPocketKillValue() {
        return 2;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }
}
