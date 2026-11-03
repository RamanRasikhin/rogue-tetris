package com.github.rogueTetrisLicencjat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerDataTest {

    private PlayerData playerData;

    @BeforeEach
    void setUp() {
        playerData = new PlayerData();
    }

    @Test
    void testInitialValues() {
        assertEquals(100f, playerData.getMaxHp(), "Początkowe HP powinno wynosić 100");
        assertEquals(100f, playerData.getHp(), "Obecne HP na start powinno wynosić 100");
        assertEquals(1, playerData.getFloor(), "Gra powinna zaczynać się na 1 piętrze");
        assertEquals(0, playerData.getScore(), "Początkowy wynik powinien wynosić 0");
        assertEquals(0, playerData.getDifficultyLevel(), "Początkowy poziom trudności to 0");
    }

    @Test
    void testHpModification() {
        playerData.addHp(-20);
        assertEquals(80f, playerData.getHp(), "HP po odjęciu 20 powinno wynosić 80");

        playerData.addHp(50);
        assertEquals(100f, playerData.getHp(), "HP po dodaniu 50 nie może przekroczyć maxHp (100)");
    }

    @Test
    void testDifficultyUnlocking() {
        assertEquals(0, playerData.getUnlockedDifficulty(), "Na start odblokowany jest tylko poziom 0");

        playerData.unlockNextDifficulty();
        assertEquals(1, playerData.getUnlockedDifficulty(), "Po przejściu gry powinien odblokować się poziom 1");

        playerData.unlockNextDifficulty();
        assertEquals(1, playerData.getUnlockedDifficulty(), "Przejście na zbyt niskim poziomie nie powinno odblokować kolejnego");

        playerData.setDifficultyLevel(1);
        playerData.unlockNextDifficulty();
        assertEquals(2, playerData.getUnlockedDifficulty(), "Przejście na poziomie 1 powinno odblokować poziom 2");
    }

    @Test
    void testDifficultyModifiersOnReset() {
        playerData.setDifficultyLevel(4);
        playerData.reset();

        assertEquals(80f, playerData.getMaxHp(), "Na poziomie trudności 4 MAX HP powinno wynosić 80");
        assertEquals(80f, playerData.getHp(), "Obecne HP powinno również wynosić 80");

        playerData.setDifficultyLevel(5);
        playerData.reset();

        assertEquals(0.4f, playerData.getFallSpeed(), 0.001f, "Na poziomie trudności 5 klocki powinny spadać szybciej (0.4)");
    }
}
