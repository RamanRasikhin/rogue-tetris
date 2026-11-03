package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class HelpScreen extends ScreenAdapter {
    private final RogueTetrisGDX game;
    private final ShapeRenderer shapeRenderer;
    private int page = 1;

    private float timer = 0f;

    public HelpScreen(RogueTetrisGDX game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.04f, 0.04f, 0.08f, 1);
        timer += delta;

        game.getBatch().begin();
        game.getBatch().draw(game.getAssets().background, 0, 0, 800, 600);
        game.getBatch().end();

        if (page == 1) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            drawPage1Animation();
            shapeRenderer.end();
        } else if (page == 2) {
            drawPage2Blocks();
        } else if (page == 3) {
            drawPage3Icons();
        }

        game.getBatch().begin();

        if (page == 1) {
            drawPage1Text();
        } else if (page == 2) {
            drawPage2Text();
        } else if (page == 3) {
            drawPage3Text();
        } else if (page == 4) {
            drawPage4Text();
        }

        game.getFont().setColor(Color.LIGHT_GRAY);
        game.getFont().draw(game.getBatch(), "STRONA " + page + "/4", 360, 60);

        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "[STRZAŁKA W LEWO/PRAWO] Zmiana strony   |   [ESC] Wróć do menu", 130, 30);

        game.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (page < 4) {
                page++;
                timer = 0;
                game.playSoundClick();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (page > 1) {
                page--;
                game.playSoundClick();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.playSoundClick();
            game.setScreen(new MenuScreen(game));
        }
    }

    private void drawPage1Text() {
        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "JAK GRAC W ROGUE TETRIS?", 300, 530);

        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "Zasady sa proste: to polaczenie Tetrisa z walka turowa.", 30, 490);
        game.getFont().draw(game.getBatch(), "Ukladaj pelne linie z klockow, aby zadawac obrazenia przeciwnikowi.", 30, 460);
        game.getFont().draw(game.getBatch(), "Im wiecej linii ulozysz naraz (Combo), tym wieksze obrazenia i leczenie zyskasz!", 30, 430);

        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "PRZYKŁAD WALKI", 330, 340);

        float animCycle = timer % 4.0f;

        game.getFont().setColor(Color.WHITE);
        if (animCycle < 1.5f) {
            game.getFont().draw(game.getBatch(), "Krok 1: Klocki spadają. Zbuduj pełną linię!", 250, 140);
        } else if (animCycle < 2.5f) {
            game.getFont().draw(game.getBatch(), "Krok 2: Pełna linia zostaje zniszczona!", 250, 140);
        } else {
            game.getFont().setColor(Color.RED);
            game.getFont().draw(game.getBatch(), "Krok 3: Przeciwnik otrzymuje obrażenia!", 250, 140);
        }
    }

    private void drawPage1Animation() {
        float animCycle = timer % 4.0f;

        float boardX = 250;
        float boardY = 170;
        float blockSize = 16;

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(boardX - 2, boardY - 2, 6 * blockSize + 4, 10 * blockSize + 4);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(boardX, boardY, 6 * blockSize, 10 * blockSize);

        shapeRenderer.setColor(Color.SKY);
        for (int x = 0; x < 6; x++) {
            if (x == 3) continue;
            shapeRenderer.rect(boardX + x * blockSize, boardY, blockSize - 1, blockSize - 1);
        }

        float fallY = boardY + 7 * blockSize;

        if (animCycle < 1.0f) {
            fallY -= (animCycle * 7 * blockSize);
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.rect(boardX + 3 * blockSize, fallY, blockSize - 1, blockSize - 1);
            shapeRenderer.rect(boardX + 3 * blockSize, fallY + blockSize, blockSize - 1, blockSize - 1);
        } else if (animCycle < 1.5f) {
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.rect(boardX + 3 * blockSize, boardY, blockSize - 1, blockSize - 1);
            shapeRenderer.rect(boardX + 3 * blockSize, boardY + blockSize, blockSize - 1, blockSize - 1);
        } else if (animCycle >= 1.5f && animCycle < 2.5f) {
            if ((int)(animCycle * 10) % 2 == 0) {
                shapeRenderer.setColor(Color.WHITE);
                for (int x = 0; x < 6; x++) {
                    shapeRenderer.rect(boardX + x * blockSize, boardY, blockSize - 1, blockSize - 1);
                }
            }
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.rect(boardX + 3 * blockSize, boardY + blockSize, blockSize - 1, blockSize - 1);
        } else {
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.rect(boardX + 3 * blockSize, boardY, blockSize - 1, blockSize - 1);
        }

        float enemyX = 400;
        float enemyY = 240;

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(enemyX, enemyY, 120, 15);

        float hpPercent = 1.0f;
        if (animCycle >= 2.5f) {
            hpPercent = Math.max(0.6f, 1.0f - (animCycle - 2.5f));
        }

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(enemyX, enemyY, 120 * hpPercent, 15);

        if (animCycle >= 2.5f && animCycle < 3.0f && (int)(animCycle * 15) % 2 == 0) {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(enemyX + 60, enemyY + 40, 20);
        } else {
            shapeRenderer.setColor(Color.BROWN);
            shapeRenderer.circle(enemyX + 60, enemyY + 40, 20);
        }
    }

    private void drawPage2Text() {
        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "DOSTEPNE KLOCKI (TETROMINA)", 280, 520);

        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "W grze dostepnych jest 7 klasycznych ksztaltow.", 30, 480);
        game.getFont().draw(game.getBatch(), "Mozesz obracac je za pomoca strzalki w GORE, a ukladac strzalka w DOL.", 30, 450);
        game.getFont().draw(game.getBatch(), "Pamietaj: obrot jest zalezny od przestrzeni, w ciasnym miejscu klocek moze sie nie obrocic!", 30, 420);
    }

    private void drawPage2Blocks() {
        game.getBatch().begin();
        float startX = 130;
        float startY = 250;
        float size = 20;
        float spacing = 140;

        for(int i=0; i<4; i++) game.getBatch().draw(game.getAssets().tetrisBlocks[0], startX + i * size, startY, size, size);

        for(int i=0; i<2; i++) for(int j=0; j<2; j++) game.getBatch().draw(game.getAssets().tetrisBlocks[1], startX + spacing + i * size, startY - size + j * size, size, size);

        game.getBatch().draw(game.getAssets().tetrisBlocks[2], startX + spacing * 2, startY, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[2], startX + spacing * 2 + size, startY, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[2], startX + spacing * 2 + size * 2, startY, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[2], startX + spacing * 2 + size, startY - size, size, size);

        game.getBatch().draw(game.getAssets().tetrisBlocks[3], startX + spacing * 3 + size, startY, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[3], startX + spacing * 3 + size * 2, startY, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[3], startX + spacing * 3, startY - size, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[3], startX + spacing * 3 + size, startY - size, size, size);

        float row2Y = startY - 100;
        game.getBatch().draw(game.getAssets().tetrisBlocks[4], startX, row2Y, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[4], startX + size, row2Y, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[4], startX + size, row2Y - size, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[4], startX + size * 2, row2Y - size, size, size);

        game.getBatch().draw(game.getAssets().tetrisBlocks[5], startX + spacing, row2Y, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[5], startX + spacing + size, row2Y, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[5], startX + spacing + size * 2, row2Y, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[5], startX + spacing + size * 2, row2Y - size, size, size);

        game.getBatch().draw(game.getAssets().tetrisBlocks[6], startX + spacing * 2, row2Y, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[6], startX + spacing * 2 + size, row2Y, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[6], startX + spacing * 2 + size * 2, row2Y, size, size);
        game.getBatch().draw(game.getAssets().tetrisBlocks[6], startX + spacing * 2, row2Y - size, size, size);
        game.getBatch().end();
    }

    private void drawPage3Icons() {
        game.getBatch().begin();
        float startX = 40;
        float startY = 480;
        float gap = 60;
        float size = 45;

        game.getBatch().draw(game.getAssets().fightIcon, startX, startY, size, size);
        game.getBatch().draw(game.getAssets().epicIcon, startX, startY - gap, size, size);
        game.getBatch().draw(game.getAssets().unknownIcon, startX, startY - gap * 2, size, size);
        game.getBatch().draw(game.getAssets().chillIcon, startX, startY - gap * 3, size, size);
        game.getBatch().draw(game.getAssets().bossIcon, startX, startY - gap * 4, size, size);
        game.getBatch().end();
    }

    private void drawPage3Text() {
        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "MAPA I ZDARZENIA", 320, 550);

        game.getFont().setColor(Color.WHITE);
        float startX = 100;
        float startY = 510;
        float gap = 60;

        game.getFont().draw(game.getBatch(), "- Zwykly przeciwnik (Bonusowe PUNKTY)", startX, startY);
        game.getFont().draw(game.getBatch(), "- Elitarny przeciwnik (100% Szansy na ARTEFAKT)", startX, startY - gap);
        game.getFont().draw(game.getBatch(), "- Wydarzenie (Wymien HP na Statystyki)", startX, startY - gap * 2);
        game.getFont().draw(game.getBatch(), "- Ognisko (Leczenie lub Wzmocnienie Mnoznika Ataku)", startX, startY - gap * 3);
        game.getFont().draw(game.getBatch(), "- Ostateczny BOSS (Wygrana)", startX, startY - gap * 4);
    }

    private void drawPage4Text() {
        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "ARTEFAKTY:", 350, 540);

        float startY = 510;
        float gap = 40;

        game.getBatch().draw(game.getAssets().vitalityArt, 30, startY - 20, 35, 35);
        game.getFont().setColor(Color.LIME);
        game.getFont().draw(game.getBatch(), "VITALITY : +30 do Maksymalnego i Obecnego HP", 80, startY);

        game.getBatch().draw(game.getAssets().powerArt, 30, startY - gap - 20, 35, 35);
        game.getFont().setColor(Color.RED);
        game.getFont().draw(game.getBatch(), "POWER : Zwieksza zadawane obrazenia o 40%", 80, startY - gap);

        game.getBatch().draw(game.getAssets().focusArt, 30, startY - gap*2 - 20, 35, 35);
        game.getFont().setColor(Color.CYAN);
        game.getFont().draw(game.getBatch(), "FOCUS : Spowalnia predkosc spadania klockow", 80, startY - gap*2);

        game.getBatch().draw(game.getAssets().vampireArt, 30, startY - gap*3 - 20, 35, 35);
        game.getFont().setColor(Color.MAGENTA);
        game.getFont().draw(game.getBatch(), "VAMPIRE : Zwieksza leczenie otrzymywane z Combo", 80, startY - gap*3);

        game.getBatch().draw(game.getAssets().shieldArt, 30, startY - gap*4 - 20, 35, 35);
        game.getFont().setColor(Color.ROYAL);
        game.getFont().draw(game.getBatch(), "SHIELD : Zwieksza czas pomiedzy atakami wrogow o 1.5s", 80, startY - gap*4);

        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "POZIOMY TRUDNOSCI (ASCENSION):", 50, 235);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "Poziom 1: +20 HP u wszystkich wrogow ", 30, 195);
        game.getFont().draw(game.getBatch(), "Poziom 2: Elity maja dodatkowe +50 HP", 30, 165);
        game.getFont().draw(game.getBatch(), "Poziom 3: Artefakt VAMPIRE dziala 2x gorzej", 30, 135);
        game.getFont().draw(game.getBatch(), "Poziom 4: Zaczynasz gre z -20 MAX HP. Ostateczny BOSS zyskuje +150 HP", 30, 105);
        game.getFont().draw(game.getBatch(), "Poziom 5: Klocki od samego poczatku spadaja znacznie szybciej", 30, 75);
    }

    @Override public void dispose() {
        shapeRenderer.dispose();
    }
}
