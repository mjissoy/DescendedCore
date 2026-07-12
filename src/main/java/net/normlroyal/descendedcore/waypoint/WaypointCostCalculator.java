package net.normlroyal.descendedcore.waypoint;

@FunctionalInterface
public interface WaypointCostCalculator {
    ItemWaypointCost calculate(WaypointTravelContext context);
}
