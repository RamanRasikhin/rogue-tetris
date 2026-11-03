package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class MapScreen extends ScreenAdapter {
    private final RogueTetrisGDX game;
    private final ShapeRenderer shapeRenderer;
    private float cameraX = 0, targetCameraX = 0, dragStartX = 0;
    private boolean isDragging = false;

    private final InputMultiplexer inputMultiplexer;

    public MapScreen(RogueTetrisGDX game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        if (game.getPlayerData().getWorldMap() == null) {
            game.getPlayerData().setWorldMap(MapGenerator.generateMap());
        }

        MapInputProcessor mapInputProcessor = new MapInputProcessor();
        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(mapInputProcessor);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.inputMultiplexer);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    private void checkClick() {
        float worldMouseX = (Gdx.input.getX() * (800f / Gdx.graphics.getWidth())) + cameraX;
        float worldMouseY = (600f - Gdx.input.getY() * (600f / Gdx.graphics.getHeight()));

        for (MapNode node : game.getPlayerData().getWorldMap()) {
            if (node.isReachable() && !node.isCompleted()) {
                float dist = (float) Math.sqrt(Math.pow(worldMouseX - node.getX(), 2) + Math.pow(worldMouseY - node.getY(), 2));

                if (dist < 40) {
                    node.setCompleted(true);
                    for (MapNode any : game.getPlayerData().getWorldMap()) {
                        any.setReachable(false);
                    }
                    for (MapNode child : node.getConnections()) {
                        child.setReachable(true);
                    }

                    if (node.getType() == MapNode.Type.ENEMY || node.getType() == MapNode.Type.ELITE || node.getType() == MapNode.Type.BOSS) {
                        game.setScreen(new BattleScreen(game, node.getType()));
                    } else if (node.getType() == MapNode.Type.REST) {
                        game.setScreen(new RestScreen(game));
                    } else {
                        game.setScreen(new EventScreen(game));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        float maxScroll = (10 * 200) + 200 - 800;
        ScreenUtils.clear(0.04f, 0.04f, 0.06f, 1);
        if (!isDragging) {
            for (MapNode node : game.getPlayerData().getWorldMap()) {
                if (node.isReachable()) { targetCameraX = node.getX() - 400; break; }
            }
        }
        cameraX += (MathUtils.clamp(targetCameraX, 0, maxScroll) - cameraX) * 0.1f;

        game.getBatch().begin();
        game.getBatch().draw(game.getAssets().background, 0, 0, 800, 600);
        game.getBatch().end();

        Gdx.gl.glLineWidth(3);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (MapNode node : game.getPlayerData().getWorldMap()) {
            for (MapNode t : node.getConnections()) {
                if (node.isCompleted() && t.isCompleted()) {
                    shapeRenderer.setColor(Color.GREEN);
                } else if (node.isCompleted() && t.isReachable()) {
                    shapeRenderer.setColor(Color.GOLD);
                } else {
                    shapeRenderer.setColor(Color.DARK_GRAY);
                }
                shapeRenderer.line(node.getX() - cameraX, node.getY(), t.getX() - cameraX, t.getY());
            }
        }
        shapeRenderer.end();
        Gdx.gl.glLineWidth(1);

        game.getBatch().begin();
        for (MapNode node : game.getPlayerData().getWorldMap()) {
            float size = node.getType() == MapNode.Type.BOSS ? 80 : 50;
            float drawX = node.getX() - size / 2 - cameraX;
            float drawY = node.getY() - size / 2;

            if (node.isCompleted()) {
                game.getBatch().setColor(0.3f, 1f, 0.3f, 1f);
            } else if (node.isReachable()) {
                float pulse = 0.8f + 0.2f * MathUtils.sin(System.currentTimeMillis() / 200f);
                game.getBatch().setColor(pulse, pulse, pulse, 1f);
            } else {
                game.getBatch().setColor(0.4f, 0.4f, 0.4f, 1f);
            }

            if (node.getType() == MapNode.Type.BOSS) {
                game.getBatch().draw(game.getAssets().bossIcon, drawX, drawY, size, size);
            } else if (node.getType() == MapNode.Type.ELITE) {
                game.getBatch().draw(game.getAssets().epicIcon, drawX, drawY, size, size);
            } else if (node.getType() == MapNode.Type.REST) {
                game.getBatch().draw(game.getAssets().chillIcon, drawX, drawY, size, size);
            } else if (node.getType() == MapNode.Type.MYSTERY) {
                game.getBatch().draw(game.getAssets().unknownIcon, drawX, drawY, size, size);
            } else {
                game.getBatch().draw(game.getAssets().fightIcon, drawX, drawY, size, size);
            }
            game.getBatch().setColor(Color.WHITE);
        }

        drawArtifacts(game.getBatch());

        game.getFont().draw(game.getBatch(), "SCORE: " + game.getPlayerData().getScore() + "  FLOOR: " + game.getPlayerData().getFloor() + "/10", 20, 580);
        game.getBatch().end();

        drawMapHPBar();
    }

    private void drawMapHPBar() {
        game.getBatch().begin();
        float hpBarWidth = 200;
        float hpBarHeight = 20;
        float hpBarX = (800 - hpBarWidth) / 2;
        float hpBarY = 560;

        game.getBatch().setColor(Color.WHITE);
        game.getBatch().draw(game.getAssets().healthBarFrame, hpBarX, hpBarY, hpBarWidth, hpBarHeight);

        float playerHpWidth = hpBarWidth * (game.getPlayerData().getHp() / game.getPlayerData().getMaxHp());
        if (playerHpWidth > 0) {
            game.getBatch().setColor(Color.LIME);
            game.getBatch().draw(game.getAssets().healthBarFill, hpBarX, hpBarY, playerHpWidth, hpBarHeight);
            game.getBatch().setColor(Color.WHITE);
        }

        String hpText = (int)game.getPlayerData().getHp() + "/" + (int)game.getPlayerData().getMaxHp();
        float textWidth = hpText.length() * 8;
        game.getFont().draw(game.getBatch(), hpText, hpBarX + (hpBarWidth - textWidth) / 2, hpBarY + 16);
        game.getBatch().end();
    }

    private void drawArtifacts(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        Array<Artifact> artifacts = game.getPlayerData().getCollectedArtifacts();
        float startX = 20;
        float startY = 510;
        float size = 32;
        float gap = 5;

        int cols = 8;

        for (int i = 0; i < artifacts.size; i++) {
            int col = i % cols;
            int row = i / cols;
            float ax = startX + col * (size + gap);
            float ay = startY - row * (size + gap);

            switch (artifacts.get(i)) {
                case HP:
                    batch.draw(game.getAssets().vitalityArt, ax, ay, size, size);
                    break;
                case DMG:
                    batch.draw(game.getAssets().powerArt, ax, ay, size, size);
                    break;
                case SPD:
                    batch.draw(game.getAssets().focusArt, ax, ay, size, size);
                    break;
                case VAMP:
                    batch.draw(game.getAssets().vampireArt, ax, ay, size, size);
                    break;
                case SHLD:
                    batch.draw(game.getAssets().shieldArt, ax, ay, size, size);
                    break;
            }
        }
    }

    private class MapInputProcessor extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (button == Input.Buttons.LEFT) {
                dragStartX = screenX;
                isDragging = true;
                return true;
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (button == Input.Buttons.LEFT && isDragging) {
                isDragging = false;
                checkClick();
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (isDragging) {
                targetCameraX += (dragStartX - screenX) * 1.5f;
                dragStartX = screenX;
                return true;
            }
            return false;
        }
    }
}
