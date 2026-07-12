package net.normlroyal.descendedcore.waypoint;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface WaypointAccessPolicy {
    WaypointAccessResult test(ServerPlayer player, WaypointLocation source, WaypointEntry destination);
}
