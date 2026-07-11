package net.normlroyal.descendedcore.content.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.normlroyal.descendedcore.content.entity.void_anomaly.VoidAnomaly;
import net.normlroyal.descendedcore.content.entity.void_anomaly.VoidAnomalyBehavior;

public class VoidAnomalyEntity extends Zombie implements VoidAnomaly {
    private int teleportCooldown = 0;

    public VoidAnomalyEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide) {
            VoidAnomalyBehavior.spawnAmbientParticles(this, 2, 0.02F);

            if (teleportCooldown > 0) {
                teleportCooldown--;
            } else if (this.getTarget() != null && random.nextFloat() < 0.05F) {
                VoidAnomalyBehavior.attemptShortTeleport(this, 5.0D, 1, 48);
                teleportCooldown = 200;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return VoidAnomalyBehavior.canBeHurt(this, source, amount) && super.hurt(source, amount);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes();
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    public static boolean canSpawnHere(
            EntityType<VoidAnomalyEntity> type,
            ServerLevelAccessor level,
            MobSpawnType reason,
            BlockPos pos,
            RandomSource random
    ) {
        return VoidAnomalyBehavior.canDeepDarkSpawn(type, level, reason, pos, random);
    }
}
