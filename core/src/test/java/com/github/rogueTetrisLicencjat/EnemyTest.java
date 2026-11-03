package com.github.rogueTetrisLicencjat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {

    @Test
    void testEnemyCreation() {
        Enemy enemy = new Enemy("Test Boss", 200f);

        assertEquals("Test Boss", enemy.getName());
        assertEquals(1.0f, enemy.getHpPercent());
        assertFalse(enemy.isDead());
    }

    @Test
    void testDamageTaking() {
        Enemy enemy = new Enemy("Goblin", 100f);

        enemy.takeDamage(30f);
        assertEquals(0.7f, enemy.getHpPercent(), 0.01f);
        assertFalse(enemy.isDead());

        enemy.takeDamage(80f);
        assertEquals(0.0f, enemy.getHpPercent(), 0.01f);
        assertTrue(enemy.isDead(), "Przeciwnik powinien być martwy, jeśli jego HP spadnie poniżej zera");
    }
}
