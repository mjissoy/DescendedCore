package net.normlroyal.descendedcore.waypoint;

public record WaypointTravelContext(WaypointLocation source, WaypointLocation target) {
    public boolean crossDimension() {
        return !source.dimension().equals(target.dimension());
    }

    public double distance() {
        return crossDimension() ? 0.0D : Math.sqrt(source.pos().distSqr(target.pos()));
    }
}
