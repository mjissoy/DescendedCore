package net.normlroyal.descendedcore.waypoint;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface WaypointEndpoint {
    WaypointLocation location();

    String displayName();

    ResourceLocation type();

    default Component displayComponent() {
        return Component.literal(displayName());
    }
}
