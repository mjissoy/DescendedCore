package net.normlroyal.descendedcore;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {
    public static final ForgeConfigSpec SPEC;
    public static final Common COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Common(builder);
        SPEC = builder.build();
    }

    private Config() {
    }

    public static final class Common {
        public final ForgeConfigSpec.DoubleValue voidTouchedSpawnChance;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.push("void");
            voidTouchedSpawnChance = builder
                    .comment("Chance for an eligible living entity to naturally become Void Touched.")
                    .defineInRange("voidTouchedSpawnChance", 0.05D, 0.0D, 1.0D);
            builder.pop();
        }
    }
}
