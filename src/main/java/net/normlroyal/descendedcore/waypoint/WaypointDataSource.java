package net.normlroyal.descendedcore.waypoint;

import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Optional;

public interface WaypointDataSource<T extends WaypointEndpoint> {
    Collection<T> waypointsFor(ServerPlayer player);

    Optional<T> find(WaypointLocation location);
}
