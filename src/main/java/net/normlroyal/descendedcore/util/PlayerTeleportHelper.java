package net.normlroyal.descendedcore.util;

import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerTeleportHelper {
    private PlayerTeleportHelper() {
    }

    public static void teleportPreservingExperience(
            ServerPlayer player,
            ServerLevel targetLevel,
            double x,
            double y,
            double z,
            float yaw,
            float pitch
    ) {
        float progress = player.experienceProgress;
        int level = player.experienceLevel;
        int total = player.totalExperience;

        player.teleportTo(targetLevel, x, y, z, yaw, pitch);

        player.experienceProgress = progress;
        player.experienceLevel = level;
        player.totalExperience = total;

        player.connection.send(new ClientboundSetExperiencePacket(
                progress,
                total,
                level
        ));
    }
}