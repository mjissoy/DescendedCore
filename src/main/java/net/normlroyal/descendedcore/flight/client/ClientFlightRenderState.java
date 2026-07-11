package net.normlroyal.descendedcore.flight.client;

public final class ClientFlightRenderState {
    private static float pitchDegrees;
    private static float bankDegrees;

    private ClientFlightRenderState() {
    }

    public static float pitchDegrees() {
        return pitchDegrees;
    }

    public static float bankDegrees() {
        return bankDegrees;
    }

    public static void tickToward(float targetPitchDegrees, float targetBankDegrees) {
        pitchDegrees = approach(pitchDegrees, targetPitchDegrees, 1.2F);
        bankDegrees = approach(bankDegrees, targetBankDegrees, 1.0F);
    }

    public static void reset() {
        pitchDegrees = 0.0F;
        bankDegrees = 0.0F;
    }

    private static float approach(float current, float target, float step) {
        float difference = target - current;
        if (Math.abs(difference) <= step) {
            return target;
        }
        return current + Math.copySign(step, difference);
    }
}
