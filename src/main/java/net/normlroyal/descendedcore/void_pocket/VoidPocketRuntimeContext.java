package net.normlroyal.descendedcore.void_pocket;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public record VoidPocketRuntimeContext(
        ServerLevel level,
        VoidPocketData.Pocket pocket,
        ServerPlayer player
) {
}
