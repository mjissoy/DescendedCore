package net.normlroyal.descendedcore.action;

public record ActionCooldownSnapshot(long until, int totalTicks) {
    public int remainingTicks(long now) {
        long remaining = until - now;
        return (int) Math.max(0L, Math.min(Integer.MAX_VALUE, remaining));
    }

    public boolean active(long now) {
        return remainingTicks(now) > 0;
    }
}
