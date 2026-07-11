package net.normlroyal.descendedcore.content.entity.void_anomaly;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.normlroyal.descendedcore.content.particle.CoreParticles;

public final class VoidAnomalyBehavior {
    private VoidAnomalyBehavior() {
    }

    public static boolean canBeHurt(Mob anomaly, DamageSource source, float amount) {
        return VoidAnomalyDamageRules.canBeHurt(
                new VoidAnomalyDamageContext(anomaly, source, amount)
        );
    }

    public static void spawnAmbientParticles(Mob mob, int count, float chance) {
        if (!(mob.level() instanceof ServerLevel level)) {
            return;
        }

        RandomSource random = mob.getRandom();
        if (random.nextFloat() >= chance) {
            return;
        }

        level.sendParticles(
                CoreParticles.VOID_TOUCHED.get(),
                mob.getX(),
                mob.getY() + mob.getBbHeight() * 0.55D,
                mob.getZ(),
                count,
                mob.getBbWidth() * 0.45D,
                mob.getBbHeight() * 0.35D,
                mob.getBbWidth() * 0.45D,
                0.0D
        );
    }

    public static boolean attemptShortTeleport(Mob mob, double range, int verticalRange, int particleCount) {
        if (!(mob.level() instanceof ServerLevel level)) {
            return false;
        }

        RandomSource random = mob.getRandom();
        double fromX = mob.getX();
        double fromY = mob.getY();
        double fromZ = mob.getZ();

        double targetX = mob.getX() + (random.nextDouble() - 0.5D) * 2.0D * range;
        double targetY = mob.getY() + random.nextInt(verticalRange * 2 + 1) - verticalRange;
        double targetZ = mob.getZ() + (random.nextDouble() - 0.5D) * 2.0D * range;

        spawnTeleportParticles(level, mob, fromX, fromY, fromZ, particleCount);

        boolean success = mob.randomTeleport(targetX, targetY, targetZ, false);
        if (success) {
            spawnTeleportParticles(level, mob, mob.getX(), mob.getY(), mob.getZ(), particleCount);
            level.gameEvent(GameEvent.TELEPORT, mob.position(), GameEvent.Context.of(mob));
        }

        return success;
    }

    public static void spawnTeleportParticles(ServerLevel level, Mob mob, double x, double y, double z, int count) {
        RandomSource random = mob.getRandom();
        for (int i = 0; i < count; i++) {
            double vx = (random.nextDouble() - 0.5D) * 2.0D;
            double vy = (random.nextDouble() - 0.5D) * 2.0D;
            double vz = (random.nextDouble() - 0.5D) * 2.0D;

            level.sendParticles(
                    CoreParticles.BLUE_PORTAL.get(),
                    x,
                    y + mob.getBbHeight() * 0.5D,
                    z,
                    1,
                    vx,
                    vy,
                    vz,
                    0.0D
            );
        }
    }

    public static boolean canDeepDarkSpawn(
            EntityType<? extends Mob> type,
            ServerLevelAccessor level,
            MobSpawnType reason,
            BlockPos pos,
            RandomSource random
    ) {
        if (level.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        if (level.getRawBrightness(pos, 0) > 7) {
            return false;
        }

        if (pos.getY() > 0) {
            return false;
        }

        return level.getBlockState(pos.below()).isValidSpawn(level, pos.below(), type);
    }
}
