package com.zvibadash.sudosolve.networking;

public enum DifficultyLevel {
    EASY("EASY"),
    MEDIUM("MEDIUM"),
    HARD("HARD"),
    INSANE("INSANE");

    private final String level;

    DifficultyLevel(String envUrl) {
        this.level = envUrl;
    }

    public String getLevel() {
        return level;
    }
}