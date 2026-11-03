package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class VictoryScreen extends ScreenAdapter {
    private final RogueTetrisGDX game;

    public VictoryScreen(RogueTetrisGDX game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.04f, 0.04f, 0.08f, 1);
        game.getBatch().begin();
        game.getBatch().draw(game.getAssets().winImage, 0, 0, 800, 600);
        game.getFont().setColor(Color.GOLD);
        game.getFont().draw(game.getBatch(), "FINALOWE ZWYCIESTWO!", 310, 150);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "GRATULACJE! POKONALES OSTATNIEGO BOSSA", 240, 100);
        game.getFont().draw(game.getBatch(), "TWOJ KONCOWY WYNIK: " + game.getPlayerData().getScore(), 300, 70);
        game.getFont().draw(game.getBatch(), "NACISNIJ [ESC], ABY WROCIC DO MENU", 250, 40);
        game.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }
    }
}
