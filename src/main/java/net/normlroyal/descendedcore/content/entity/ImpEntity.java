package net.normlroyal.descendedcore.content.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.normlroyal.descendedcore.content.entity.imp.ImpDamageRules;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class ImpEntity extends Monster implements GeoEntity {

    private static final RawAnimation IDLE   = RawAnimation.begin().thenLoop("animation.imp.idle");
    private static final RawAnimation WALK   = RawAnimation.begin().thenLoop("animation.imp.fly");
    private static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.imp.attack");
    private static final RawAnimation DEATH  = RawAnimation.begin().thenPlay("animation.imp.death");

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0, state -> {
            if (this.isDeadOrDying()) {
                state.setAnimation(DEATH);
                return PlayState.CONTINUE;
            }

            if (state.isMoving()) {
                state.setAnimation(WALK);
            } else {
                state.setAnimation(IDLE);
            }
            return PlayState.CONTINUE;
        }));

        controllers.add(new AnimationController<>(this, "attack", 0, state -> PlayState.STOP)
                .triggerableAnim("attack", ATTACK));
    }

    @Override
    public void swing(InteractionHand hand) {
        super.swing(hand);
        if (!this.level().isClientSide) {
            this.triggerAnim("attack", "attack");
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.FLYING_SPEED, 0.28D);
    }

    public ImpEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {
            BlockPos below = this.blockPosition();
            int groundY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, below.getX(), below.getZ());
            double maxY = groundY + 3.0D;

            if (this.getY() > maxY) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
                this.setPos(this.getX(), maxY, this.getZ());
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float base = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float finalDmg = base;
        finalDmg = ImpDamageRules.modifyDamage(this, target, finalDmg);

        DamageSource source = this.damageSources().mobAttack(this);
        return target.hurt(source, finalDmg);
    }


    public static boolean canSpawnHere(EntityType<ImpEntity> type,
                                       ServerLevelAccessor level,
                                       MobSpawnType reason,
                                       BlockPos pos,
                                       RandomSource random) {

        if (level.getLevel().dimension() != Level.NETHER) return false;

        if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) return false;
        if (!level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()) return false;

        if (level.getBrightness(LightLayer.BLOCK, pos) > 7) return false;

        BlockPos.MutableBlockPos mpos = pos.mutable();
        int minY = level.getMinBuildHeight();

        while (mpos.getY() > minY && level.getBlockState(mpos).getCollisionShape(level, mpos).isEmpty()) {
            mpos.move(Direction.DOWN);
        }
        if (mpos.getY() <= minY) return false;

        BlockState ground = level.getBlockState(mpos);
        if (ground.liquid()) return false;

        int dy = pos.getY() - mpos.getY();
        return dy >= 0 && dy <= 3;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_FIRE)
                || source.is(DamageTypes.ON_FIRE)
                || source.is(DamageTypes.LAVA)
                || source.is(DamageTypes.HOT_FLOOR)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

}
