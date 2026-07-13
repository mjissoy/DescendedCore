package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import net.normlroyal.descendedcore.content.entity.CoreEntities;
import net.normlroyal.descendedcore.content.entity.void_anomaly.VoidAnomaly;
import net.normlroyal.descendedcore.content.item.CoreItems;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class VoidPocketManager {
    private static final int ANOMALY_CAP = 7;
    private static final int SPAWN_PADDING = 3;

    private VoidPocketManager() {
    }

    public static boolean isVoidPocket(Level level) {
        return VoidPocketDimensions.isVoidPocket(level);
    }

    public static Optional<VoidPocketData.Pocket> pocketAt(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return Optional.empty();
        }
        return VoidPocketData.get(serverLevel.getServer()).findPocketAt(pos);
    }

    public static void enterNewPocket(ServerPlayer player) {
        enterNewPocket(player, new ResourceLocation("descendedcore", "default"));
    }

    public static void enterNewPocket(ServerPlayer player, ResourceLocation origin) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        ServerLevel voidLevel = server.getLevel(VoidPocketDimensions.VOID_POCKET_LEVEL);
        if (voidLevel == null) {
            player.displayClientMessage(Component.literal("The void pocket dimension is missing.").withStyle(ChatFormatting.RED), true);
            return;
        }

        VoidPocketData data = VoidPocketData.get(server);
        Optional<VoidPocketData.Anchor> nearestAnchor = data.findNearestAnchor(
                player.level().dimension(),
                player.blockPosition(),
                8,
                player.getUUID()
        );

        VoidPocketData.Pocket pocket = data.createPocket(player, nearestAnchor.orElse(null), origin);
        VoidPocketLifecycle.created(voidLevel, pocket, player);
        ensurePocketGenerated(voidLevel, pocket);
        teleportToPocket(player, voidLevel, pocket);
        VoidPocketLifecycle.entered(voidLevel, pocket, player);
        player.displayClientMessage(Component.literal("The Void Heart opens a pocket of nothing.").withStyle(ChatFormatting.DARK_PURPLE), true);
    }

    public static void enterExistingPocket(ServerPlayer player, VoidPocketData.Pocket pocket) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        ServerLevel voidLevel = server.getLevel(VoidPocketDimensions.VOID_POCKET_LEVEL);
        if (voidLevel == null) {
            player.displayClientMessage(Component.literal("The void pocket dimension is missing.").withStyle(ChatFormatting.RED), true);
            return;
        }

        ensurePocketGenerated(voidLevel, pocket);
        teleportToPocket(player, voidLevel, pocket);
        VoidPocketLifecycle.entered(voidLevel, pocket, player);
    }

    public static VoidPocketData.Anchor registerPreserver(
            Level level,
            BlockPos pos,
            @Nullable LivingEntity placer,
            ResourceLocation typeId,
            @Nullable String name
    ) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new IllegalStateException("Void Pocket preservers must be registered on the logical server");
        }

        VoidPocketPreserverType type = VoidPocketPreservers.get(typeId)
                .orElseThrow(() -> new IllegalStateException("Unregistered Void Pocket preserver type: " + typeId));
        if (!type.matches(level.getBlockState(pos))) {
            throw new IllegalStateException("Block at " + pos + " does not match Void Pocket preserver type " + typeId);
        }
        String resolvedName = name;
        if ((resolvedName == null || resolvedName.isBlank()) && level instanceof ServerLevel) {
            resolvedName = type.defaultName(serverLevel, pos);
        }

        VoidPocketData data = VoidPocketData.get(serverLevel.getServer());
        VoidPocketData.Anchor anchor = data.registerAnchor(
                typeId,
                level.dimension(),
                pos,
                placer instanceof ServerPlayer player ? player.getUUID() : null,
                resolvedName
        );

        if (isVoidPocket(level)) {
            data.findPocketAt(pos).ifPresent(pocket -> {
                pocket.preserved = true;
                anchor.pocketId = pocket.id;
                data.linkReturnAnchor(pocket);
                data.setDirty();
            });
        }
        return anchor;
    }

    public static void removePreserver(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        VoidPocketData data = VoidPocketData.get(serverLevel.getServer());
        Optional<VoidPocketData.Pocket> containingPocket = isVoidPocket(level)
                ? data.findPocketAt(pos)
                : Optional.empty();
        data.removeAnchor(level.dimension(), pos);
        containingPocket.ifPresent(pocket -> {
            if (data.anchorsInPocket(pocket).isEmpty()) {
                pocket.preserved = false;
                data.setDirty();
            }
        });
    }

    public static boolean preservePocketAt(
            ServerPlayer player,
            BlockPos preserverPos,
            ResourceLocation typeId,
            @Nullable String name
    ) {
        MinecraftServer server = player.getServer();
        if (server == null || !(player.level() instanceof ServerLevel level)) {
            return false;
        }

        VoidPocketPreserverType type = VoidPocketPreservers.get(typeId).orElse(null);
        if (type == null
                || !type.matches(level.getBlockState(preserverPos))
                || !type.canPreserve(player, level, preserverPos)) {
            return false;
        }

        VoidPocketData data = VoidPocketData.get(server);
        Optional<VoidPocketData.Pocket> pocketOptional = data.findPocketAt(preserverPos);
        if (pocketOptional.isEmpty()) {
            pocketOptional = data.findPocketAt(player.blockPosition());
        }
        if (pocketOptional.isEmpty()) {
            return false;
        }

        VoidPocketData.Pocket pocket = pocketOptional.get();
        VoidPocketData.Anchor anchor = data.registerAnchor(typeId, level.dimension(), preserverPos, player.getUUID(), name);
        anchor.pocketId = pocket.id;
        pocket.preserved = true;
        data.linkReturnAnchor(pocket);
        data.setDirty();
        return true;
    }

    public static void preserveAndExit(
            ServerPlayer player,
            BlockPos preserverPos,
            ResourceLocation typeId,
            @Nullable String name,
            Component successMessage,
            Component failureMessage
    ) {
        if (!preservePocketAt(player, preserverPos, typeId, name)) {
            emergencyExit(player, failureMessage);
            return;
        }

        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        VoidPocketData.get(server).findPocketAt(player.blockPosition())
                .ifPresentOrElse(
                        pocket -> exitPocket(player, pocket, successMessage),
                        () -> emergencyExit(player, failureMessage)
                );
    }

    public static void exitWithVoidHeart(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        Optional<VoidPocketData.Pocket> pocket = VoidPocketData.get(server).findPocketAt(player.blockPosition());
        if (pocket.isPresent()) {
            exitPocket(player, pocket.get(), Component.literal("The Void Heart tears open a way home.").withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            emergencyExit(player, Component.literal("The Void Heart rejects this broken pocket.").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    public static void ejectPlayer(ServerPlayer player, Component message) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        Optional<VoidPocketData.Pocket> pocket = VoidPocketData.get(server).findPocketAt(player.blockPosition());
        if (pocket.isPresent()) {
            exitPocket(player, pocket.get(), message);
        } else {
            emergencyExit(player, message);
        }
    }

    public static void recordAnomalyKill(ServerLevel level, BlockPos pos, ServerPlayer player, int killValue) {
        if (!isVoidPocket(level)) {
            return;
        }

        VoidPocketData data = VoidPocketData.get(level.getServer());
        Optional<VoidPocketData.Pocket> pocketOptional = data.findPocketAt(pos);
        if (pocketOptional.isEmpty() || pocketOptional.get().completed) {
            return;
        }

        VoidPocketData.Pocket pocket = pocketOptional.get();
        pocket.kills += Math.max(1, killValue);
        data.setDirty();

        int remaining = Math.max(0, pocket.requiredKills - pocket.kills);
        if (remaining > 0) {
            player.displayClientMessage(Component.literal("Void anomalies remaining: " + remaining).withStyle(ChatFormatting.DARK_PURPLE), true);
            return;
        }

        pocket.completed = true;
        ItemStack link = new ItemStack(CoreItems.VOID_HEART_LINK.get());
        if (!player.getInventory().add(link)) {
            player.drop(link, false);
        }
        data.setDirty();
        VoidPocketLifecycle.completed(level, pocket, player);
        player.displayClientMessage(Component.literal("The pocket ruptures. A Void Heart Link forms in your grasp.").withStyle(ChatFormatting.LIGHT_PURPLE), true);
    }

    public static void tickPlayer(ServerPlayer player) {
        if (!isVoidPocket(player.level()) || !(player.level() instanceof ServerLevel level)) {
            return;
        }

        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        VoidPocketData data = VoidPocketData.get(server);
        Optional<VoidPocketData.Pocket> pocketOptional = data.findPocketAt(player.blockPosition());
        if (pocketOptional.isEmpty()) {
            emergencyExit(player, Component.literal("The unstable void spits you out.").withStyle(ChatFormatting.DARK_PURPLE));
            return;
        }

        VoidPocketData.Pocket pocket = pocketOptional.get();
        if (player.getY() < pocket.center.getY() - VoidPocketData.HALF_HEIGHT - 4) {
            ejectPlayer(player, Component.literal("You fall through the pocket's seam and wake outside it.").withStyle(ChatFormatting.DARK_PURPLE));
            return;
        }

        if (!pocket.bounds().inflate(2.0D).contains(player.position())) {
            BlockPos entry = pocket.entryPos();
            player.teleportTo(level, entry.getX() + 0.5D, entry.getY(), entry.getZ() + 0.5D, player.getYRot(), player.getXRot());
            player.fallDistance = 0.0F;
            return;
        }

        int spawnInterval = pocket.completed ? 120 : 80;
        if (player.tickCount % spawnInterval == 0) {
            spawnAnomalyIfNeeded(level, pocket);
        }

        long now = level.getGameTime();
        long ticksSinceLastEvent = now - pocket.lastRuntimeEventGameTime;
        if (player.tickCount % 100 == 0
                && VoidPocketRuntimeEvents.tryTrigger(
                        new VoidPocketRuntimeContext(level, pocket, player),
                        ticksSinceLastEvent
                )) {
            pocket.lastRuntimeEventGameTime = now;
            data.setDirty();
        }
    }

    public static boolean isDestinationValid(MinecraftServer server, VoidPocketData.Anchor anchor) {
        if (!anchor.dimension.equals(VoidPocketDimensions.VOID_POCKET_LEVEL)) {
            return true;
        }
        return VoidPocketData.get(server).findPocketAt(anchor.pos)
                .map(pocket -> pocket.preserved)
                .orElse(false);
    }

    public static void ensurePocketGenerated(ServerLevel level, VoidPocketData.Pocket pocket) {
        if (!pocket.generated) {
            DefaultVoidPocketIslandGenerator.generate(level, pocket);
            VoidPocketLifecycle.generated(level, pocket);
        } else {
            restorePocketPreservers(level, pocket);
        }
    }

    public static BlockPos findArrivalPos(ServerLevel level, BlockPos anchorPos) {
        BlockPos candidate = anchorPos.above();
        for (int i = 0; i < 6; i++) {
            if (level.getBlockState(candidate).getCollisionShape(level, candidate).isEmpty()
                    && level.getBlockState(candidate.above()).getCollisionShape(level, candidate.above()).isEmpty()) {
                return candidate;
            }
            candidate = candidate.above();
        }
        return anchorPos.above();
    }

    static void restorePocketPreservers(ServerLevel level, VoidPocketData.Pocket pocket) {
        VoidPocketData data = VoidPocketData.get(level.getServer());
        for (VoidPocketData.Anchor anchor : data.anchorsInPocket(pocket)) {
            VoidPocketPreservers.get(anchor.type).ifPresent(type -> {
                if (!type.matches(level.getBlockState(anchor.pos))) {
                    level.setBlock(anchor.pos, type.restorationState(), 3);
                }
            });
        }
    }

    private static void exitPocket(ServerPlayer player, VoidPocketData.Pocket pocket, Component message) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        ServerLevel returnLevel = server.getLevel(pocket.returnDimension);
        if (returnLevel == null) {
            returnLevel = server.overworld();
        }

        teleportPreservingExperience(player, returnLevel, pocket.returnX, pocket.returnY, pocket.returnZ, pocket.returnYaw, pocket.returnPitch);
        player.fallDistance = 0.0F;
        player.setHealth(Math.max(1.0F, player.getHealth()));
        player.displayClientMessage(message, true);
        VoidPocketLifecycle.exited(pocket, player);

        if (!pocket.preserved) {
            ServerLevel voidLevel = server.getLevel(VoidPocketDimensions.VOID_POCKET_LEVEL);
            if (voidLevel != null) {
                clearPocket(voidLevel, pocket);
                VoidPocketLifecycle.removed(voidLevel, pocket);
            }
            VoidPocketData.get(server).removePocket(pocket.id);
        }
    }

    private static void emergencyExit(ServerPlayer player, Component message) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        BlockPos respawn = player.getRespawnPosition();
        ServerLevel targetLevel = respawn != null
                ? server.getLevel(player.getRespawnDimension())
                : server.overworld();
        if (targetLevel == null) {
            targetLevel = server.overworld();
        }

        BlockPos target = respawn != null ? respawn : targetLevel.getSharedSpawnPos();
        teleportPreservingExperience(player, targetLevel, target.getX() + 0.5D, target.getY() + 1.0D, target.getZ() + 0.5D, player.getYRot(), player.getXRot());
        player.fallDistance = 0.0F;
        player.setHealth(Math.max(1.0F, player.getHealth()));
        player.displayClientMessage(message, true);
    }

    private static void teleportToPocket(ServerPlayer player, ServerLevel voidLevel, VoidPocketData.Pocket pocket) {
        BlockPos entry = pocket.entryPos();
        teleportPreservingExperience(player, voidLevel, entry.getX() + 0.5D, entry.getY(), entry.getZ() + 0.5D, player.getYRot(), player.getXRot());
        player.fallDistance = 0.0F;
    }

    private static void clearPocket(ServerLevel level, VoidPocketData.Pocket pocket) {
        BlockState air = Blocks.AIR.defaultBlockState();
        AABB bounds = pocket.bounds();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int x = Mth.floor(bounds.minX); x < Mth.floor(bounds.maxX); x++) {
            for (int y = Mth.floor(bounds.minY); y < Mth.floor(bounds.maxY); y++) {
                for (int z = Mth.floor(bounds.minZ); z < Mth.floor(bounds.maxZ); z++) {
                    mutable.set(x, y, z);
                    level.setBlock(mutable, air, 3);
                }
            }
        }
    }

    private static void spawnAnomalyIfNeeded(ServerLevel level, VoidPocketData.Pocket pocket) {
        int existing = level.getEntitiesOfClass(
                Entity.class,
                pocket.bounds(),
                entity -> entity.isAlive() && entity instanceof VoidAnomaly
        ).size();
        int cap = pocket.completed ? Math.max(3, ANOMALY_CAP - 2) : ANOMALY_CAP;
        if (existing >= cap) {
            return;
        }

        Mob anomaly = chooseVoidPocketAnomaly(level).create(level);
        if (anomaly == null) {
            return;
        }

        Optional<BlockPos> spawnPos = findIslandSpawnPos(level, pocket);
        if (spawnPos.isEmpty()) {
            return;
        }

        BlockPos pos = spawnPos.get();

        anomaly.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
        ForgeEventFactory.onFinalizeSpawn(anomaly, level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
        anomaly.setPersistenceRequired();
        level.addFreshEntity(anomaly);
    }

    private static Optional<BlockPos> findIslandSpawnPos(ServerLevel level, VoidPocketData.Pocket pocket) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int attempt = 0; attempt < 48; attempt++) {
            int range = VoidPocketData.HORIZONTAL_RADIUS - SPAWN_PADDING;
            int x = pocket.center.getX() + level.random.nextInt(range * 2 + 1) - range;
            int z = pocket.center.getZ() + level.random.nextInt(range * 2 + 1) - range;
            for (int y = pocket.center.getY() + 12; y >= pocket.center.getY() - 14; y--) {
                mutable.set(x, y, z);
                BlockPos floor = mutable.below();
                boolean solidFloor = !level.getBlockState(floor).getCollisionShape(level, floor).isEmpty();
                boolean feetClear = level.getBlockState(mutable).getCollisionShape(level, mutable).isEmpty();
                boolean headClear = level.getBlockState(mutable.above()).getCollisionShape(level, mutable.above()).isEmpty();
                if (solidFloor && feetClear && headClear && mutable.distSqr(pocket.entryPos()) >= 64.0D) {
                    return Optional.of(mutable.immutable());
                }
            }
        }
        return Optional.empty();
    }

    private static EntityType<? extends Mob> chooseVoidPocketAnomaly(ServerLevel level) {
        int roll = level.random.nextInt(100);
        if (roll < 60) {
            return CoreEntities.VOID_ANOMALY.get();
        }
        if (roll < 82) {
            return CoreEntities.VOID_SKELETON_ANOMALY.get();
        }
        return CoreEntities.VOID_SLIME_ANOMALY.get();
    }

    private static void teleportPreservingExperience(
            ServerPlayer player,
            ServerLevel targetLevel,
            double x,
            double y,
            double z,
            float yaw,
            float pitch
    ) {
        float experienceProgress = player.experienceProgress;
        int experienceLevel = player.experienceLevel;
        int totalExperience = player.totalExperience;

        player.teleportTo(targetLevel, x, y, z, yaw, pitch);

        player.experienceProgress = experienceProgress;
        player.experienceLevel = experienceLevel;
        player.totalExperience = totalExperience;

        player.connection.send(new ClientboundSetExperiencePacket(
                experienceProgress,
                totalExperience,
                experienceLevel
        ));
    }
}
