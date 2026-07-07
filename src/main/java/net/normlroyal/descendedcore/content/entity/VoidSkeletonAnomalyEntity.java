package net.normlroyal.descendedcore.content.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.normlroyal.descendedcore.content.entity.void_anomaly.VoidAnomaly;
import net.normlroyal.descendedcore.content.entity.void_anomaly.VoidAnomalyBehavior;

public class VoidSkeletonAnomalyEntity extends Skeleton implements VoidAnomaly {
    private int teleportCooldown = 0;

    public VoidSkeletonAnomalyEntity(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide) {
            VoidAnomalyBehavior.spawnAmbientParticles(this, 2, 0.025F);

            if (teleportCooldown > 0) {
                teleportCooldown--;
            } else if (this.getTarget() != null && random.nextFloat() < 0.04F) {
                VoidAnomalyBehavior.attemptShortTeleport(this, 8.0D, 2, 42);
                teleportCooldown = 150;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return VoidAnomalyBehavior.canBeHurt(source) && super.hurt(source, amount);
    }

    @Override
    public int getVoidPocketKillValue() {
        return 2;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Skeleton.createAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.27D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }
}
