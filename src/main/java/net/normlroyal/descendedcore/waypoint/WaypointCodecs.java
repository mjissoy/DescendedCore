package net.normlroyal.descendedcore.waypoint;

import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public final class WaypointCodecs {
    private static final int MAX_ENTRIES = 512;

    private WaypointCodecs() {
    }

    public static void writeEntries(FriendlyByteBuf buf, List<WaypointEntry> entries) {
        buf.writeVarInt(entries.size());
        for (WaypointEntry entry : entries) {
            entry.write(buf);
        }
    }

    public static List<WaypointEntry> readEntries(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        if (size < 0 || size > MAX_ENTRIES) {
            throw new IllegalArgumentException("Invalid waypoint entry count: " + size);
        }
        List<WaypointEntry> entries = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            entries.add(WaypointEntry.read(buf));
        }
        return entries;
    }
}
