package net.normlroyal.descendedcore.waypoint;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public final class WaypointLabels {
    private WaypointLabels() {
    }

    public static String distance(WaypointLocation source, WaypointLocation target) {
        if (!source.dimension().equals(target.dimension())) {
            return "Cross-dimensional";
        }
        int distance = Mth.floor(Math.sqrt(source.pos().distSqr(target.pos())));
        return distance + " blocks away";
    }

    public static String dimension(ResourceKey<Level> dimension) {
        String path = dimension.location().getPath();
        return switch (path) {
            case "overworld" -> "Overworld";
            case "the_nether" -> "Nether";
            case "the_end" -> "End";
            default -> titleCase(path.replace('_', ' '));
        };
    }

    private static String titleCase(String value) {
        StringBuilder result = new StringBuilder(value.length());
        boolean capitalize = true;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isWhitespace(c)) {
                capitalize = true;
                result.append(c);
            } else if (capitalize) {
                result.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
