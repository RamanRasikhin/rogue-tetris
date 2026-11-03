package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class BattleRenderer {
    private final RogueTetrisGDX game;
    private final ShapeRenderer shapeRenderer;
    private final TetrisEngine engine;
    private final Enemy enemy;
    private final GlyphLayout layout = new GlyphLayout();

    public BattleRenderer(RogueTetrisGDX game, TetrisEngine engine, Enemy enemy, ShapeRenderer shapeRenderer) {
        this.game = game;
        this.engine = engine;
        this.enemy = enemy;
        this.shapeRenderer = shapeRenderer;
    }

    public void drawTetris(float ox, float oy) {
        int[][] grid = engine.getGrid();

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(298 + ox, 78 + oy, 204, 404);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                if (grid[x][y] == 0) {
                    shapeRenderer.setColor(new Color(0.1f, 0.1f, 0.1f, 1f));
                    shapeRenderer.rect(300 + x * 20 + ox, 80 + y * 20 + oy, 18, 18);
                }
            }
        }

        shapeRenderer.end();
        game.getBatch().begin();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                if (grid[x][y] != 0) {
                    int colorIdx = (grid[x][y] - 1) % game.getAssets().tetrisBlocks.length;
                    game.getBatch().draw(game.getAssets().tetrisBlocks[colorIdx], 300 + x * 20 + ox, 80 + y * 20 + oy, 18, 18);
                }
            }
        }

        int[][] p = engine.getCurrentPiece();
        int ghostY = engine.getGhostY();
        int colorId = engine.getCurrentColorId();

        game.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] != 0) {
                    float px = (engine.getCurrentX() + i) * 20 + 300 + ox;
                    float py = (ghostY + j) * 20 + 80 + oy;
                    shapeRenderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));
                    shapeRenderer.rect(px, py, 18, 18);
                }
            }
        }

        shapeRenderer.end();
        game.getBatch().begin();

        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] != 0) {
                    game.getBatch().draw(game.getAssets().tetrisBlocks[colorId], (engine.getCurrentX() + i) * 20 + 300 + ox, (engine.getCurrentY() + j) * 20 + 80 + oy, 18, 18);
                }
            }
        }
        game.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void drawNextPiece(float ox, float oy) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(80 + ox, 400 + oy, 100, 100);
        shapeRenderer.end();

        game.getBatch().begin();
        int[][] next = engine.getNextPiece();
        int nextColorId = engine.getNextColorId();
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next[0].length; j++) {
                if (next[i][j] != 0) {
                    game.getBatch().draw(game.getAssets().tetrisBlocks[nextColorId], 105 + i * 18 + ox, 430 + j * 18 + oy, 16, 16);
                }
            }
        }
        game.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void drawHUD(float ox, float oy, float healFlash, float enemyTimer, float enemyCooldown) {
        shapeRenderer.end();
        game.getBatch().begin();

        game.getBatch().setColor(Color.WHITE);
        game.getBatch().draw(game.getAssets().healthBarFrame, 550 + ox, 500 + oy, 200, 20);
        float enemyHpWidth = 200 * enemy.getHpPercent();
        if (enemyHpWidth > 0) {
            game.getBatch().setColor(Color.RED);
            game.getBatch().draw(game.getAssets().healthBarFill, 550 + ox, 500 + oy, enemyHpWidth, 20);
        }

        game.getBatch().setColor(Color.WHITE);
        game.getBatch().draw(game.getAssets().healthBarFrame, 300 + ox, 40 + oy, 200, 20);
        float playerHpWidth = 200 * (game.getPlayerData().getHp() / game.getPlayerData().getMaxHp());
        if (playerHpWidth > 0) {
            game.getBatch().setColor(healFlash > 0 ? Color.WHITE : Color.GREEN);
            game.getBatch().draw(game.getAssets().healthBarFill, 300 + ox, 40 + oy, playerHpWidth, 20);
        }
        game.getBatch().setColor(Color.WHITE);
        game.getBatch().end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float progress = Math.min(1.0f, enemyTimer / enemyCooldown);
        shapeRenderer.setColor(progress > 0.8f ? Color.RED : Color.YELLOW);
        shapeRenderer.rect(550 + ox, 485 + oy, 200 * progress, 10);
    }

    public void drawArtifacts(Array<Artifact> artifacts) {
        shapeRenderer.end();
        game.getBatch().begin();
        for (int i = 0; i < artifacts.size; i++) {
            float ax = 550 + (i % 4) * 37;
            float ay = 300 - (i / 4) * 37;
            switch (artifacts.get(i)) {
                case HP: game.getBatch().draw(game.getAssets().vitalityArt, ax, ay, 32, 32); break;
                case DMG: game.getBatch().draw(game.getAssets().powerArt, ax, ay, 32, 32); break;
                case SPD: game.getBatch().draw(game.getAssets().focusArt, ax, ay, 32, 32); break;
                case VAMP: game.getBatch().draw(game.getAssets().vampireArt, ax, ay, 32, 32); break;
                case SHLD: game.getBatch().draw(game.getAssets().shieldArt, ax, ay, 32, 32); break;
            }
        }
        game.getBatch().end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void drawBattleText(float ox, float oy, int combo) {
        game.getFont().draw(game.getBatch(), "NEXT", 100 + ox, 520 + oy);
        game.getFont().draw(game.getBatch(), enemy.getName(), 550 + ox, 540 + oy);
        game.getFont().draw(game.getBatch(), "HP: " + (int)game.getPlayerData().getHp(), 360 + ox, 75 + oy);
        game.getFont().setColor(Color.SKY);
        game.getFont().draw(game.getBatch(), "SCORE: " + game.getPlayerData().getScore(), 30, 30);
        game.getFont().setColor(Color.WHITE);
        if (combo > 0) game.getFont().draw(game.getBatch(), "COMBO X" + combo, 550 + ox, 400 + oy);
    }

    public void drawRewardCards(Artifact[] rewards) {
        game.getBatch().begin();
        for (int i = 0; i < 3; i++) {
            float cardX = 20 + i * 260;
            float cardY = 160;
            game.getBatch().draw(game.getAssets().gameInfo2, cardX, cardY, 250, 330);

            if (rewards != null && i < rewards.length && rewards[i] != null) {
                Texture icon = getArtifactTexture(rewards[i]);
                if (icon != null) {
                    float iconWidth = 64;
                    float iconHeight = 64;
                    float iconX = cardX + (250 - iconWidth) / 2f;
                    float iconY = cardY + 145;

                    game.getBatch().draw(icon, iconX, iconY, iconWidth, iconHeight);
                }
            }
        }
        game.getBatch().end();
    }

    private com.badlogic.gdx.graphics.Texture getArtifactTexture(Artifact artifact) {
        switch (artifact) {
            case HP: return game.getAssets().vitalityArt;
            case DMG: return game.getAssets().powerArt;
            case SPD: return game.getAssets().focusArt;
            case VAMP: return game.getAssets().vampireArt;
            case SHLD: return game.getAssets().shieldArt;
            default: return null;
        }
    }

    public void drawRewardText(Array<Artifact> rewards) {
        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "ZWYCIĘSTWO! WYBIERZ ARTEFAKT (1-3)", 240, 520);

        for (int i = 0; i < 3; i++) {
            float cardX = 20 + i * 260;

            layout.setText(game.getFont(), rewards.get(i).name, Color.GOLD, 210, Align.center, false);
            game.getFont().draw(game.getBatch(), layout, cardX + 20, 405);
            layout.setText(game.getFont(), rewards.get(i).desc, Color.WHITE, 210, Align.center, true);
            game.getFont().draw(game.getBatch(), layout, cardX + 20, 290);
        }
    }
}
