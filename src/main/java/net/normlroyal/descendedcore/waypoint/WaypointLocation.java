package net.normlroyal.descendedcore.waypoint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record WaypointLocation(ResourceKey<Level> dimension, BlockPos pos) {
    public static WaypointLocation read(FriendlyByteBuf buf) {
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
        return new WaypointLocation(dimension, buf.readBlockPos());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dimension.location());
        buf.writeBlockPos(pos);
    }
}
