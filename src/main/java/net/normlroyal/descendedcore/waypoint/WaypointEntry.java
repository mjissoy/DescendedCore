package net.normlroyal.descendedcore.waypoint;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public record WaypointEntry(
        WaypointLocation location,
        String name,
        String dimensionLabel,
        String distanceLabel,
        ResourceLocation type,
        ItemWaypointCost cost
) {
    public static WaypointEntry fromEndpoint(
            WaypointEndpoint endpoint,
            String dimensionLabel,
            String distanceLabel,
            ItemWaypointCost cost
    ) {
        return new WaypointEntry(
                endpoint.location(),
                endpoint.displayName(),
                dimensionLabel,
                distanceLabel,
                endpoint.type(),
                cost
        );
    }

    public static WaypointEntry read(FriendlyByteBuf buf) {
        return new WaypointEntry(
                WaypointLocation.read(buf),
                buf.readUtf(128),
                buf.readUtf(64),
                buf.readUtf(64),
                buf.readResourceLocation(),
                ItemWaypointCost.read(buf)
        );
    }

    public void write(FriendlyByteBuf buf) {
        location.write(buf);
        buf.writeUtf(name, 128);
        buf.writeUtf(dimensionLabel, 64);
        buf.writeUtf(distanceLabel, 64);
        buf.writeResourceLocation(type);
        cost.write(buf);
    }

    public ResourceKey<Level> dimension() {
        return location.dimension();
    }

    public BlockPos pos() {
        return location.pos();
    }

    public boolean isType(ResourceLocation expectedType) {
        return type.equals(expectedType);
    }
}
