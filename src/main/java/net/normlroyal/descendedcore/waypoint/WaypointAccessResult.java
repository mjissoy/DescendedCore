package net.normlroyal.descendedcore.waypoint;

import net.minecraft.network.chat.Component;

public record WaypointAccessResult(boolean allowed, Component message) {
    public static final WaypointAccessResult ALLOW = new WaypointAccessResult(true, Component.empty());

    public static WaypointAccessResult deny(Component message) {
        return new WaypointAccessResult(false, message);
    }
}
