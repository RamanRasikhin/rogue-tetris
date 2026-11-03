package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.utils.Array;

public class PlayerData implements Damageable {
    private float hp;
    private float maxHp;
    private float damageMultiplier;
    private float fallSpeed;
    private int floor;

    private int score;
    private boolean soundEnabled;
    private Array<MapNode> worldMap;

    private float shieldDelay;
    private float vampHealMultiplier;

    private int difficultyLevel = 0;
    private int unlockedDifficulty = 0;

    private Array<Artifact> collectedArtifacts;

    public PlayerData() {
        this.soundEnabled = true;
        this.collectedArtifacts = new Array<>();
        reset();
    }

    public void reset() {
        this.maxHp = 100 - (difficultyLevel >= 4 ? 20 : 0);
        this.hp = this.maxHp;
        this.damageMultiplier = 1.0f;
        this.fallSpeed = 0.5f - (difficultyLevel >= 5 ? 0.1f : 0);
        this.floor = 1;
        this.score = 0;
        this.worldMap = null;
        this.shieldDelay = 0.0f;
        this.vampHealMultiplier = (difficultyLevel >= 3 ? 0.5f : 1.0f);
        this.collectedArtifacts.clear();
    }

    @Override
    public float getHp() { return hp; }

    public void setHp(float hp) {
        this.hp = hp;
        if (this.hp > maxHp) this.hp = maxHp;
        if (this.hp < 0) this.hp = 0;
    }

    public void addHp(float amount) {
        this.hp += amount;
        if (this.hp > maxHp) this.hp = maxHp;
        if (this.hp < 0) this.hp = 0;
    }

    @Override
    public void takeDamage(float amount) {
        addHp(-amount);
    }

    @Override
    public void heal(float amount) {
        addHp(amount);
    }

    @Override
    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public float getMaxHp() { return maxHp; }
    public void setMaxHp(float maxHp) { this.maxHp = maxHp; }

    @Override
    public float getHpPercent() {
        if (maxHp <= 0) return 0;
        return hp / maxHp;
    }

    public float getDamageMultiplier() { return damageMultiplier; }
    public void setDamageMultiplier(float damageMultiplier) { this.damageMultiplier = damageMultiplier; }
    public void addDamageMultiplier(float amount) { this.damageMultiplier += amount; }

    public float getFallSpeed() { return fallSpeed; }
    public void setFallSpeed(float fallSpeed) { this.fallSpeed = fallSpeed; }

    public int getFloor() { return floor; }
    public void incrementFloor() { this.floor++; }

    public int getScore() { return score; }
    public void addScore(int amount) { this.score += amount; }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }

    public Array<MapNode> getWorldMap() { return worldMap; }
    public void setWorldMap(Array<MapNode> worldMap) { this.worldMap = worldMap; }

    public float getShieldDelay() { return shieldDelay; }
    public void addShieldDelay(float amount) { this.shieldDelay += amount; }

    public float getVampHealMultiplier() { return vampHealMultiplier; }
    public void addVampHealMultiplier(float amount) { this.vampHealMultiplier += amount; }

    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public int getUnlockedDifficulty() { return unlockedDifficulty; }
    public void unlockNextDifficulty() {
        if (difficultyLevel == unlockedDifficulty && unlockedDifficulty < 5) {
            unlockedDifficulty++;
        }
    }

    public Array<Artifact> getCollectedArtifacts() { return collectedArtifacts; }
    public void addArtifact(Artifact artifact) { this.collectedArtifacts.add(artifact); }
}
