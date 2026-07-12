package net.normlroyal.descendedcore.action;

import net.minecraft.util.Mth;

public final class ActionScaling {
    private ActionScaling() {
    }

    public static int masteryFromRank(int rank, int firstMasteryRank, int maxMasteryRank) {
        if (rank < firstMasteryRank) {
            return 0;
        }
        int cappedRank = Math.min(rank, maxMasteryRank);
        return Math.max(0, cappedRank - firstMasteryRank + 1);
    }

    public static double scale(double base, int mastery, double perLevelFraction) {
        return base * (1.0D + Math.max(0, mastery) * perLevelFraction);
    }

    public static int addInt(int base, int mastery, int perLevel) {
        return base + Math.max(0, mastery) * perLevel;
    }

    public static int addIntCapped(int base, int mastery, int perLevel, int max) {
        return Math.min(addInt(base, mastery, perLevel), max);
    }

    public static int scaleDuration(int baseTicks, int mastery, double perLevelFraction) {
        return Math.max(1, (int) Math.round(scale(baseTicks, mastery, perLevelFraction)));
    }

    public static int scaleCooldown(int baseTicks, double multiplier) {
        return Math.max(1, (int) Math.round(baseTicks * Math.max(0.0D, multiplier)));
    }

    public static double steppedMultiplier(int mastery, double firstMultiplier, double perLevelDelta, double minimum) {
        double multiplier = firstMultiplier + Math.max(0, mastery) * perLevelDelta;
        return Math.max(minimum, multiplier);
    }

    public static int clampMastery(int mastery, int maximum) {
        return Mth.clamp(mastery, 0, Math.max(0, maximum));
    }
}
