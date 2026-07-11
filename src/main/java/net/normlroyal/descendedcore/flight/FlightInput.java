package net.normlroyal.descendedcore.flight;

public record FlightInput(
        boolean ascend,
        boolean descend,
        boolean boost,
        float forward,
        float strafe
) {
    public static final FlightInput ZERO = new FlightInput(false, false, false, 0.0F, 0.0F);

    public FlightInput sanitized() {
        boolean cleanAscend = ascend;
        boolean cleanDescend = descend;

        if (cleanAscend && cleanDescend) {
            cleanDescend = false;
        }

        return new FlightInput(
                cleanAscend,
                cleanDescend,
                boost,
                cleanAxis(forward),
                cleanAxis(strafe)
        );
    }

    public static float cleanAxis(float value) {
        if (!Float.isFinite(value)) {
            return 0.0F;
        }
        return Math.max(-1.0F, Math.min(1.0F, value));
    }
}
