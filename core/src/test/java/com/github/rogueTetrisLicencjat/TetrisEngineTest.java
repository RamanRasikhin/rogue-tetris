package com.github.rogueTetrisLicencjat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TetrisEngineTest {

    private TetrisEngine engine;

    @BeforeEach
    void setUp() {
        engine = new TetrisEngine();
    }

    @Test
    void testInitialState() {
        assertNotNull(engine.getCurrentPiece(), "Klocek nie powinien być nullem");
        assertNotNull(engine.getNextPiece(), "Następny klocek nie powinien być nullem");
        assertFalse(engine.isGameOver(), "Gra nie powinna zaczynać się od Game Over");

        int[][] grid = engine.getGrid();
        assertEquals(TetrisEngine.WIDTH, grid.length, "Szerokość planszy powinna wynosić 10");
        assertEquals(TetrisEngine.HEIGHT, grid[0].length, "Wysokość planszy powinna wynosić 20");
    }

    @Test
    void testMoveBounds() {
        for (int i = 0; i < 20; i++) {
            engine.movePiece(-1);
        }
        assertTrue(engine.getCurrentX() >= 0, "Klocek nie powinien wyjść poza lewą krawędź planszy");

        for (int i = 0; i < 20; i++) {
            engine.movePiece(1);
        }
        assertTrue(engine.getCurrentX() + engine.getCurrentPiece().length <= TetrisEngine.WIDTH,
            "Klocek nie powinien wyjść poza prawą krawędź planszy");
    }
}
