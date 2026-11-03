package com.github.rogueTetrisLicencjat;

public interface Damageable {
    void takeDamage(float amount);
    void heal(float amount);
    boolean isDead();
    float getHp();
    float getMaxHp();
    float getHpPercent();
}
