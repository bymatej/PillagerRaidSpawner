package com.bymatej.minecraft.plugins.pillagerraidspawner.common;

public enum WorldSpawn {
    ALWAYS(true, true),
    OVERWORLD_ONLY(false, false),
    OVERWORLD_NETHER(true, false),
    OVERWORLD_END(false, true);

    private final boolean isNetherSpawnEnabled;

    private final boolean isEndSpawnEnabled;

    WorldSpawn(boolean isNetherSpawnEnabled, boolean isEndSpawnEnabled) {
        this.isNetherSpawnEnabled = isNetherSpawnEnabled;
        this.isEndSpawnEnabled = isEndSpawnEnabled;
    }

    public boolean isNetherSpawnEnabled() {
        return isNetherSpawnEnabled;
    }

    public boolean isEndSpawnEnabled() {
        return isEndSpawnEnabled;
    }
}
