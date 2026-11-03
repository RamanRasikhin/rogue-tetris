package com.github.rogueTetrisLicencjat;

public class Enemy implements Damageable {
    private final String name;
    private float hp;
    private final float maxHp;

    public Enemy(String name, float maxHp) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    @Override
    public void takeDamage(float amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    @Override
    public void heal(float amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
    }

    @Override
    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public float getHp() {
        return hp;
    }

    @Override
    public float getMaxHp() {
        return maxHp;
    }

    @Override
    public float getHpPercent() {
        if (maxHp <= 0) return 0;
        return hp / maxHp;
    }

    public String getName() {
        return name;
    }
}
