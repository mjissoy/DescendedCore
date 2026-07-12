package net.normlroyal.descendedcore.waypoint;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface WaypointTravelHandler {
    boolean travel(ServerPlayer player, WaypointLocation source, WaypointEntry destination);
}
