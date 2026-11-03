package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class RestScreen extends ScreenAdapter {
    private final RogueTetrisGDX game;
    private final InputMultiplexer inputMultiplexer;
    private final GlyphLayout layout = new GlyphLayout();

    public RestScreen(RogueTetrisGDX game) {
        this.game = game;

        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(new RestInputProcessor());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.inputMultiplexer);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.04f, 0.04f, 0.08f, 1);

        game.getBatch().begin();

        game.getBatch().draw(game.getAssets().chillBg, 0, 0, 800, 600);

        game.getBatch().draw(game.getAssets().gameInfo2, 10, 60, 780, 520);

        game.getFont().getData().setScale(1.2f);
        layout.setText(game.getFont(), "OGNISKO", Color.ORANGE, 500, Align.center, false);
        game.getFont().draw(game.getBatch(), layout, 150, 450);
        game.getFont().getData().setScale(1.0f);

        layout.setText(game.getFont(), "Odpoczywasz przy ognisku.", Color.WHITE, 500, Align.center, true);
        game.getFont().draw(game.getBatch(), layout, 150, 395);

        layout.setText(game.getFont(), "1. Odpocznij (Odzyskaj 30% MAX HP)", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 310);
        layout.setText(game.getFont(), "2. Trenuj (+0.2 Mnoznik Obrazen)", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 250);

        float hpBarWidth = 300;
        float hpBarHeight = 25;
        float hpBarX = (800 - hpBarWidth) / 2;
        float hpBarY = 40;

        game.getBatch().setColor(Color.WHITE);
        game.getBatch().draw(game.getAssets().healthBarFrame, hpBarX, hpBarY, hpBarWidth, hpBarHeight);

        float playerHpWidth = hpBarWidth * (game.getPlayerData().getHp() / game.getPlayerData().getMaxHp());
        if (playerHpWidth > 0) {
            game.getBatch().setColor(Color.LIME);
            game.getBatch().draw(game.getAssets().healthBarFill, hpBarX, hpBarY, playerHpWidth, hpBarHeight);
            game.getBatch().setColor(Color.WHITE);
        }

        game.getFont().setColor(Color.WHITE);
        String hpText = "HP: " + (int) game.getPlayerData().getHp() + "/" + (int) game.getPlayerData().getMaxHp();
        game.getFont().draw(game.getBatch(), hpText, hpBarX + hpBarWidth / 2 - 40, hpBarY + 50);

        game.getBatch().end();
    }

    private class RestInputProcessor extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.NUM_1) {
                float heal = game.getPlayerData().getMaxHp() * 0.3f;
                game.getPlayerData().setHp(Math.min(game.getPlayerData().getMaxHp(), game.getPlayerData().getHp() + heal));
                game.playSound(game.playSoundClick());
                game.getPlayerData().incrementFloor();
                game.setScreen(new MapScreen(game));
                return true;
            }
            if (keycode == Input.Keys.NUM_2) {
                game.getPlayerData().addDamageMultiplier(0.2f);
                game.playSound(game.playSoundClick());
                game.getPlayerData().incrementFloor();
                game.setScreen(new MapScreen(game));
                return true;
            }
            return false;
        }
    }
}
