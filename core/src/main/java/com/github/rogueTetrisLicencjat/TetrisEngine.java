package com.github.rogueTetrisLicencjat;

import java.util.Random;

public class TetrisEngine {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    private int[][] grid;
    private int currentX, currentY;
    private int[][] currentPiece;
    private int currentColorId;

    private boolean isGameOver = false;
    private boolean isWin = false;
    private Random random = new Random();

    private int[][] nextPiece;
    private int nextColorId;

    private final int[][][] SHAPES = {
        {{1, 1, 1, 1}},
        {{1, 1}, {1, 1}},
        {{0, 1, 0}, {1, 1, 1}},
        {{1, 1, 0}, {0, 1, 1}},
        {{0, 1, 1}, {1, 1, 0}},
        {{1, 0, 0}, {1, 1, 1}},
        {{0, 0, 1}, {1, 1, 1}}
    };

    public TetrisEngine() {
        grid = new int[WIDTH][HEIGHT];

        int shapeIndex = random.nextInt(SHAPES.length);
        nextPiece = SHAPES[shapeIndex];
        nextColorId = shapeIndex;

        spawnPiece();
    }

    private void spawnPiece() {
        currentPiece = nextPiece;
        currentColorId = nextColorId;

        int shapeIndex = random.nextInt(SHAPES.length);
        nextPiece = SHAPES[shapeIndex];
        nextColorId = shapeIndex;

        currentX = WIDTH / 2 - currentPiece.length / 2;
        currentY = HEIGHT - currentPiece[0].length;

        if (!canMove(currentX, currentY)) {
            isGameOver = true;
        }
    }

    public int[][] getNextPiece() { return nextPiece; }
    public int getNextColorId() { return nextColorId; }

    public int update() {
        if (isGameOver || isWin) return -1;

        if (canMove(currentX, currentY - 1)) {
            currentY--;
            return -1;
        } else {
            lockPiece();
            int lines = checkAndClearLines();
            spawnPiece();
            return lines;
        }
    }

    public int dropDown() {
        if (isGameOver || isWin) return -1;

        while (canMove(currentX, currentY - 1)) {
            currentY--;
        }

        lockPiece();
        int lines = checkAndClearLines();
        spawnPiece();
        return lines;
    }

    public void rotatePiece() {
        int r = currentPiece.length;
        int c = currentPiece[0].length;
        int[][] rotated = new int[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                rotated[j][r - 1 - i] = currentPiece[i][j];
            }
        }
        int[][] oldPiece = currentPiece;
        currentPiece = rotated;
        if (!canMove(currentX, currentY)) {
            currentPiece = oldPiece;
        }
    }

    public void addTrashLine() {
        for (int y = HEIGHT - 1; y > 0; y--) {
            for (int x = 0; x < WIDTH; x++) {
                grid[x][y] = grid[x][y - 1];
            }
        }
        int hole = random.nextInt(WIDTH);
        for (int x = 0; x < WIDTH; x++) {

            grid[x][0] = (x == hole) ? 0 : 8;
        }
    }

    public void movePiece(int direction) {
        if (!isGameOver && !isWin && canMove(currentX + direction, currentY)) {
            currentX += direction;
        }
    }

    public int getGhostY() {
        int ghostY = currentY;
        while (canMove(currentX, ghostY - 1)) {
            ghostY--;
        }
        return ghostY;
    }

    private boolean canMove(int newX, int newY) {
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[0].length; j++) {
                if (currentPiece[i][j] != 0) {
                    int tx = newX + i;
                    int ty = newY + j;
                    if (tx < 0 || tx >= WIDTH || ty < 0 || ty >= HEIGHT) return false;
                    if (grid[tx][ty] != 0) return false;
                }
            }
        }
        return true;
    }

    private void lockPiece() {
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[0].length; j++) {
                if (currentPiece[i][j] != 0) {

                    grid[currentX + i][currentY + j] = currentColorId + 1;
                }
            }
        }
    }

    private int checkAndClearLines() {
        int cleared = 0;
        for (int y = 0; y < HEIGHT; y++) {
            boolean full = true;
            for (int x = 0; x < WIDTH; x++) {
                if (grid[x][y] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                cleared++;
                removeLine(y);
                y--;
            }
        }
        return cleared;
    }

    private void removeLine(int line) {
        for (int y = line; y < HEIGHT - 1; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[x][y] = grid[x][y + 1];
            }
        }
    }

    public void setWin(boolean win) { this.isWin = win; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isWin() { return isWin; }
    public int[][] getGrid() { return grid; }
    public int getCurrentX() { return currentX; }
    public int getCurrentY() { return currentY; }
    public int[][] getCurrentPiece() { return currentPiece; }
    public int getCurrentColorId() { return currentColorId; }
}
