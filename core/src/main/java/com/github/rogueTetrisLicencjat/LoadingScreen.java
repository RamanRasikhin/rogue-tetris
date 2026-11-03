package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen extends ScreenAdapter {
    private final RogueTetrisGDX game;
    private final ShapeRenderer shapeRenderer;

    public LoadingScreen(RogueTetrisGDX game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        game.getAssets().loadAll();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1);

        if (game.getAssets().update()) {
            game.setScreen(new MenuScreen(game));
        }

        float progress = game.getAssets().getProgress();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(200, 250, 400, 30);
        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.rect(200, 250, 400 * progress, 30);
        shapeRenderer.end();

        game.getBatch().begin();
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "LOADING... " + (int)(progress * 100) + "%", 340, 310);
        game.getBatch().end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
