package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.graphics.Color;

public enum Artifact {
    HP("VITALITY", "+30 MAX HP", Color.LIME),
    DMG("POWER", "+0.4 DMG Mult", Color.RED),
    SPD("FOCUS", "Slower Fall", Color.CYAN),
    VAMP("VAMPIRE", "Combo Heal+", Color.MAGENTA),
    SHLD("SHIELD", "Enemy Delay+", Color.ROYAL);

    public final String name;
    public final String desc;
    public final Color color;

    Artifact(String n, String d, Color c) {
        this.name = n;
        this.desc = d;
        this.color = c;
    }
}
