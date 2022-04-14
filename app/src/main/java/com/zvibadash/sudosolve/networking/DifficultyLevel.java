package com.zvibadash.sudosolve.networking;

public enum DifficultyLevel {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    INSANE("Insane");

    private final String level;

    DifficultyLevel(String envUrl) {
        this.level = envUrl;
    }

    public String getLevel() {
        return level;
    }
}