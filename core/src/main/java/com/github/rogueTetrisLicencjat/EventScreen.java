package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class EventScreen extends ScreenAdapter {
    private final RogueTetrisGDX game;
    private final int eventId;

    private final InputMultiplexer inputMultiplexer;
    private final EventInputProcessor eventInputProcessor;
    private final GlyphLayout layout = new GlyphLayout();

    public EventScreen(RogueTetrisGDX game) {
        this.game = game;
        this.eventId = MathUtils.random(0, 4);

        this.eventInputProcessor = new EventInputProcessor();
        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(this.eventInputProcessor);
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

        game.getBatch().draw(game.getAssets().artBg, 0, 0, 800, 600);

        game.getBatch().draw(game.getAssets().gameInfo2, 10, 60, 780, 520);

        game.getFont().getData().setScale(1.2f);

        switch (eventId) {
            case 0: drawEvent0(); break;
            case 1: drawEvent1(); break;
            case 2: drawEvent2(); break;
            case 3: drawEvent3(); break;
            case 4: drawEvent4(); break;
        }

        game.getFont().getData().setScale(1.0f);

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
        String hpText = "HP: " + (int)game.getPlayerData().getHp() + "/" + (int)game.getPlayerData().getMaxHp();
        game.getFont().draw(game.getBatch(), hpText, hpBarX + hpBarWidth / 2 - 40, hpBarY + 50);

        game.getBatch().end();
    }

    private void drawEvent0() {
        layout.setText(game.getFont(), "TAJEMNICZY AUTOMAT", Color.GOLD, 500, Align.center, false);
        game.getFont().draw(game.getBatch(), layout, 150, 450);

        game.getFont().getData().setScale(1.0f);
        layout.setText(game.getFont(), "Znalazles stary automat do gier. Co robisz?", Color.WHITE, 500, Align.center, true);
        game.getFont().draw(game.getBatch(), layout, 150, 395);

        layout.setText(game.getFont(), "1. Wrzuc 15 HP, aby zwiekszyc mnoznik obrazen (+0.3)", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 310);
        layout.setText(game.getFont(), "2. Odejdź spokojnie (nic sie ne dzieje)", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 250);
    }

    private void drawEvent1() {
        layout.setText(game.getFont(), "SPOWIJAJACA MGŁA", Color.GOLD, 500, Align.center, false);
        game.getFont().draw(game.getBatch(), layout, 150, 450);

        game.getFont().getData().setScale(1.0f);
        layout.setText(game.getFont(), "Gestą mgła ukrywa dziwne pudełko. Wezmiesz je w ciemno?", Color.WHITE, 500, Align.center, true);
        game.getFont().draw(game.getBatch(), layout, 150, 395);

        layout.setText(game.getFont(), "1. Tak! (Szansa na +30 MAX HP albo -10 HP)", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 310);
        layout.setText(game.getFont(), "2. Lepiej nie ryzykowac.", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 250);
    }

    private void drawEvent2() {
        layout.setText(game.getFont(), "ZAGINIONY HANDLARZ", Color.GOLD, 500, Align.center, false);
        game.getFont().draw(game.getBatch(), layout, 150, 450);

        game.getFont().getData().setScale(1.0f);
        layout.setText(game.getFont(), "Wędrowiec oferuje napój. Twierdzi ze przyspiesza reakcje.", Color.WHITE, 500, Align.center, true);
        game.getFont().draw(game.getBatch(), layout, 150, 395);

        layout.setText(game.getFont(), "1. Wypij (Klocki spadają wolniej, ale tracisz 15 HP)", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 310);
        layout.setText(game.getFont(), "2. Podziękuj grzecznie.", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 250);
    }

    private void drawEvent3() {
        layout.setText(game.getFont(), "KAPLICZKA CZASU", Color.GOLD, 500, Align.center, false);
        game.getFont().draw(game.getBatch(), layout, 150, 450);

        game.getFont().getData().setScale(1.0f);
        layout.setText(game.getFont(), "Czas zwalnia w tym miejscu. Możesz medytować.", Color.WHITE, 500, Align.center, true);
        game.getFont().draw(game.getBatch(), layout, 150, 395);

        layout.setText(game.getFont(), "1. Skup sie na obronie (+1.0s do opoznienia wrogow, -20 MAX HP)", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 310);
        layout.setText(game.getFont(), "2. Zostaw to w spokoju.", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 250);
    }

    private void drawEvent4() {
        layout.setText(game.getFont(), "KRWAWY OŁTARZ", Color.GOLD, 500, Align.center, false);
        game.getFont().draw(game.getBatch(), layout, 150, 450);

        game.getFont().getData().setScale(1.0f);
        layout.setText(game.getFont(), "Ołtarz pulsuje mroczną energią. Pragnię krwi.", Color.WHITE, 500, Align.center, true);
        game.getFont().draw(game.getBatch(), layout, 150, 395);

        layout.setText(game.getFont(), "1. Weż ofiarę (Wampiryzm x1.5, ale tracisz 30 HP)", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 310);
        layout.setText(game.getFont(), "2. Odejdź.", Color.WHITE, 500, Align.left, true);
        game.getFont().draw(game.getBatch(), layout, 150, 250);
    }

    private class EventInputProcessor extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.NUM_1) {
                switch (eventId) {
                    case 0:
                        game.getPlayerData().addHp(-15);
                        game.getPlayerData().addDamageMultiplier(0.3f);
                        break;
                    case 1:
                        if (MathUtils.randomBoolean()) {
                            game.getPlayerData().setMaxHp(game.getPlayerData().getMaxHp() + 30);
                            game.getPlayerData().addHp(30);
                        } else {
                            game.getPlayerData().addHp(-10);
                        }
                        break;
                    case 2:
                        game.getPlayerData().setFallSpeed(Math.max(0.15f, game.getPlayerData().getFallSpeed() + 0.05f));
                        game.getPlayerData().addHp(-15);
                        break;
                    case 3:
                        game.getPlayerData().addShieldDelay(1.0f);
                        game.getPlayerData().setMaxHp(game.getPlayerData().getMaxHp() - 20);
                        if (game.getPlayerData().getHp() > game.getPlayerData().getMaxHp()) {
                            game.getPlayerData().setHp(game.getPlayerData().getMaxHp());
                        }
                        break;
                    case 4:
                        game.getPlayerData().addVampHealMultiplier(0.5f);
                        game.getPlayerData().addHp(-30);
                        break;
                }

                if (game.getPlayerData().getHp() <= 0) {
                    game.playSoundHit();
                    game.setScreen(new BattleScreen(game, MapNode.Type.ENEMY));
                } else {
                    game.playSoundClick();
                    game.getPlayerData().incrementFloor();
                    game.setScreen(new MapScreen(game));
                }
                return true;
            }

            if (keycode == Input.Keys.NUM_2) {
                game.playSoundClick();
                game.getPlayerData().incrementFloor();
                game.setScreen(new MapScreen(game));
                return true;
            }
            return false;
        }
    }
}
