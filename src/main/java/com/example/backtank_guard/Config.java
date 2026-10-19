package com.example.backtank_guard;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALWAYS_SPRINT = BUILDER
            .comment("always sprint")
            .define("alwaysSprint", false);
    public static final ModConfigSpec SPEC = BUILDER.build();
}
