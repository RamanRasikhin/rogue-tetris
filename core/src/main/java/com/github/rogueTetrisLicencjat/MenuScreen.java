package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen extends ScreenAdapter {
    private final RogueTetrisGDX game;

    private final float clickWidth = 260;
    private final float clickHeight = 50;

    private final float startX = (800 - clickWidth) / 2;
    private final float startY = 350;
    private final float diffY = 250;
    private final float helpY = 150;
    private final float exitY = 50;

    private final GlyphLayout layout = new GlyphLayout();

    public MenuScreen(RogueTetrisGDX game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.04f, 0.04f, 0.08f, 1);

        float mouseX = Gdx.input.getX();
        float mouseY = 600 - Gdx.input.getY();

        if (Gdx.input.justTouched()) {
            if (isMouseOver(mouseX, mouseY, startX, startY, clickWidth, clickHeight)) {
                game.playSoundClick();
                game.getPlayerData().reset();
                game.setScreen(new MapScreen(game));
            }
            if (isMouseOver(mouseX, mouseY, startX, diffY, clickWidth, clickHeight)) {
                if (game.getPlayerData().getUnlockedDifficulty() > 0) {
                    int nextDiff = game.getPlayerData().getDifficultyLevel() + 1;
                    if (nextDiff > game.getPlayerData().getUnlockedDifficulty()) {
                        nextDiff = 0;
                    }
                    game.getPlayerData().setDifficultyLevel(nextDiff);
                    game.playSoundClick();
                }
            }
            if (isMouseOver(mouseX, mouseY, startX, helpY, clickWidth, clickHeight)) {
                game.playSoundClick();
                game.setScreen(new HelpScreen(game));
            }
            if (isMouseOver(mouseX, mouseY, startX, exitY, clickWidth, clickHeight)) {
                Gdx.app.exit();
            }

            if (mouseX >= 10 && mouseX <= 60 && mouseY >= 540 && mouseY <= 590) {
                game.getPlayerData().setSoundEnabled(!game.getPlayerData().isSoundEnabled());
                game.playSoundClick();
            }
        }

        game.getBatch().begin();

        game.getBatch().draw(game.getAssets().background, 0, 0, 800, 600);

        drawButton(mouseX, mouseY, startX, startY, clickWidth, clickHeight, "START GAME");

        if (game.getPlayerData().getUnlockedDifficulty() > 0) {
            String diffText = "DIFFICULTY: " + game.getPlayerData().getDifficultyLevel();
            drawButton(mouseX, mouseY, startX, diffY, clickWidth, clickHeight, diffText);
        } else {

            float imgW = 420;
            float imgH = 110;
            float imgX = startX + clickWidth/2 - imgW/2;
            float imgY = diffY + clickHeight/2 - imgH/2;

            game.getBatch().setColor(0.5f, 0.5f, 0.5f, 1f);
            game.getBatch().draw(game.getAssets().button1, imgX, imgY, imgW, imgH);
            game.getBatch().setColor(Color.WHITE);

            game.getFont().getData().setScale(1.1f);
            layout.setText(game.getFont(), "DIFFICULTY: LOCKED");
            game.getFont().draw(game.getBatch(), layout, startX + (clickWidth - layout.width) / 2, diffY + clickHeight/2 + layout.height/2);
            game.getFont().getData().setScale(1f);
        }

        drawButton(mouseX, mouseY, startX, helpY, clickWidth, clickHeight, "JAK GRAC (FAQ)");
        drawButton(mouseX, mouseY, startX, exitY, clickWidth, clickHeight, "EXIT");

        game.getFont().getData().setScale(1.8f);
        game.getFont().setColor(Color.GOLD);
        layout.setText(game.getFont(), "ROGUE TETRIS");
        game.getFont().draw(game.getBatch(), layout, (800 - layout.width) / 2, 520);
        game.getFont().getData().setScale(1.0f);
        game.getFont().setColor(Color.WHITE);

        if (game.getPlayerData().isSoundEnabled()) {
            game.getBatch().draw(game.getAssets().soundOn, 10, 540, 50, 50);
        } else {
            game.getBatch().draw(game.getAssets().soundOff, 10, 540, 50, 50);
        }

        game.getBatch().end();
    }

    private boolean isMouseOver(float mx, float my, float bx, float by, float bw, float bh) {
        return mx >= bx && mx <= bx + bw && my >= by && my <= by + bh;
    }

    private void drawButton(float mx, float my, float bx, float by, float bw, float bh, String text) {
        float imgW = 420;
        float imgH = 110;

        float imgX = bx + bw/2 - imgW/2;
        float imgY = by + bh/2 - imgH/2;

        if (isMouseOver(mx, my, bx, by, bw, bh)) {
            game.getBatch().draw(game.getAssets().button2, imgX, imgY, imgW, imgH);
        } else {
            game.getBatch().draw(game.getAssets().button1, imgX, imgY, imgW, imgH);
        }

        game.getFont().getData().setScale(1.1f);
        layout.setText(game.getFont(), text);
        game.getFont().draw(game.getBatch(), layout, bx + (bw - layout.width) / 2, by + bh/2 + layout.height/2);
        game.getFont().getData().setScale(1f);
    }
}
