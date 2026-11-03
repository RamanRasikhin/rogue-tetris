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

public class BattleScreen extends ScreenAdapter {
    private enum State { BATTLE, REWARD, GAMEOVER }

    private State state = State.BATTLE;
    private final RogueTetrisGDX game;
    private final TetrisEngine engine;
    private final Enemy enemy;
    private final ShapeRenderer shapeRenderer;
    private final BattleRenderer battleRenderer;
    private final MapNode.Type enemyType;
    private final Array<Artifact> rewards = new Array<>();

    private float fallTimer = 0, enemyTimer = 0, shake = 0, healFlash = 0;
    private int combo = 0;

    private float moveTimer = 0;

    private final InputMultiplexer inputMultiplexer;

    public BattleScreen(RogueTetrisGDX game, MapNode.Type enemyType) {
        this.game = game;
        this.enemyType = enemyType;
        this.engine = new TetrisEngine();
        this.shapeRenderer = new ShapeRenderer();

        boolean isBoss = (enemyType == MapNode.Type.BOSS);
        boolean isElite = (enemyType == MapNode.Type.ELITE);

        int difficulty = game.getPlayerData().getDifficultyLevel();

        int hp = 50 + (game.getPlayerData().getFloor() * 25);

        if (difficulty >= 1) hp += 20;
        if (difficulty >= 2 && isElite) hp += 50;

        if (isElite) hp += 100;
        if (isBoss) hp += 200;

        if (difficulty >= 4 && isBoss) hp += 150;

        String name = getRandomEnemyName(isElite, isBoss);

        this.enemy = new Enemy(name, hp);
        this.battleRenderer = new BattleRenderer(game, engine, enemy, shapeRenderer);

        BattleInputProcessor battleInputProcessor = new BattleInputProcessor();
        this.inputMultiplexer = new InputMultiplexer();
        this.inputMultiplexer.addProcessor(battleInputProcessor);
    }

    private String getRandomEnemyName(boolean isElite, boolean isBoss) {
        if (isBoss) {
            String[] bossNames = {"Przeznaczenie", "Wladca Cieni", "Kosmiczny Byt", "Pradawny Smok"};
            return bossNames[MathUtils.random(bossNames.length - 1)] + " (BOSS)";
        }

        if (isElite) {
            String[] eliteNames = {"Mroczny Rycerz", "Ogrzy Berserker", "Wielki Golem", "Elitarny Mag"};
            return eliteNames[MathUtils.random(eliteNames.length - 1)];
        }

        String[] normalNames = {"Goblin", "Ork", "Slime", "Szkielet", "Wilkor", "Pajak", "Bandyta"};
        return normalNames[MathUtils.random(normalNames.length - 1)];
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.inputMultiplexer);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    private void genRewards() {
        rewards.clear();
        Array<Artifact> all = new Array<>(Artifact.values());
        all.shuffle();
        for (int i = 0; i < 3; i++) rewards.add(all.get(i));
    }

    @Override
    public void render(float delta) {
        update(delta);

        float ox = (shake > 0) ? MathUtils.random(-6, 6) : 0;
        float oy = (shake > 0) ? MathUtils.random(-6, 6) : 0;
        if (shake > 0) shake -= delta;
        if (healFlash > 0) healFlash -= delta;

        ScreenUtils.clear(0.04f, 0.04f, 0.08f, 1);

        game.getBatch().begin();
        if (state == State.GAMEOVER) {
            game.getBatch().draw(game.getAssets().lostImage, 0, 0, 800, 600);
        } else {
            game.getBatch().draw(enemyType == MapNode.Type.BOSS ? game.getAssets().bossBg : game.getAssets().background, 0, 0, 800, 600);
        }
        game.getBatch().end();

        if (state != State.GAMEOVER) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (state == State.BATTLE) {
                battleRenderer.drawTetris(ox, oy);
                battleRenderer.drawNextPiece(ox, oy);
                battleRenderer.drawHUD(ox, oy, healFlash, enemyTimer, getEnemyCooldown());
                battleRenderer.drawArtifacts(game.getPlayerData().getCollectedArtifacts());
            } else if (state == State.REWARD) {
                battleRenderer.drawRewardCards(rewards.toArray(Artifact.class));
            }
            shapeRenderer.end();

            game.getBatch().begin();
            if (state == State.BATTLE) {
                battleRenderer.drawBattleText(ox, oy, combo);
            } else if (state == State.REWARD) {
                battleRenderer.drawRewardText(rewards);
            }
            game.getBatch().end();
        } else {
            game.getBatch().begin();
            game.getFont().draw(game.getBatch(), "KONIEC GRY! WYNIK: " + game.getPlayerData().getScore(), 300, 100);
            game.getFont().draw(game.getBatch(), "[R] WROC DO MENU", 330, 60);
            game.getBatch().end();
        }
    }

    private float getEnemyCooldown() {
        float baseCooldown = 12.0f - ((game.getPlayerData().getFloor() - 1) * 0.5f);
        baseCooldown = Math.max(5.0f, baseCooldown);

        float cooldown = baseCooldown + game.getPlayerData().getShieldDelay();
        if (enemyType == MapNode.Type.ELITE) cooldown -= 1.0f;
        if (enemyType == MapNode.Type.BOSS) cooldown -= 2.0f;

        return Math.max(2.0f, cooldown);
    }

    private void update(float delta) {
        if (game.getPlayerData().getHp() <= 0 || engine.isGameOver()) {
            state = State.GAMEOVER;
            return;
        }

        if (state == State.BATTLE) {
            if (enemy.isDead()) {
                if (enemyType == MapNode.Type.BOSS) {
                    game.getPlayerData().unlockNextDifficulty();
                    game.setScreen(new VictoryScreen(game));
                } else if (enemyType == MapNode.Type.ELITE) {
                    game.playSoundClear();
                    genRewards();
                    state = State.REWARD;
                } else {
                    game.playSoundClear();
                    game.getPlayerData().addScore(500);
                    game.getPlayerData().incrementFloor();
                    game.setScreen(new MapScreen(game));
                }
                return;
            }

            handleTetrisLogic(delta);

            enemyTimer += delta;
            float cooldown = getEnemyCooldown();

            if (enemyTimer >= cooldown) {
                game.getPlayerData().addHp(-12);
                shake = 0.5f;
                engine.addTrashLine();
                enemyTimer = 0;
                game.playSoundHit();
            }
        }
    }

    private void handleTetrisLogic(float delta) {
        fallTimer += delta;
        float moveSpeed = 0.05f;

        boolean isDownPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        if (isDownPressed) {
            moveTimer += delta;
            if (moveTimer >= moveSpeed) {
                int lines = engine.update();
                if (lines != -1) {
                    processLines(lines);
                }
                moveTimer = 0;
                fallTimer = 0;
            }
        } else {
            if (fallTimer >= game.getPlayerData().getFallSpeed()) {
                int lines = engine.update();
                if (lines != -1) {
                    processLines(lines);
                }
                fallTimer -= game.getPlayerData().getFallSpeed();
                if (fallTimer > game.getPlayerData().getFallSpeed()) {
                    fallTimer = game.getPlayerData().getFallSpeed();
                }
            }
            moveTimer = 0;
        }
    }

    private void processLines(int lines) {
        if (lines > 0) {
            combo++;
            shake = 0.2f;
            game.getPlayerData().addScore((int)((lines * 100) * (1 + combo * 0.5f)));
            float dmg = (float)(10 * Math.pow(2, lines - 1)) * game.getPlayerData().getDamageMultiplier() * (1.0f + combo * 0.15f);
            enemy.takeDamage(dmg);
            game.getPlayerData().setHp(Math.min(game.getPlayerData().getMaxHp(), game.getPlayerData().getHp() + (combo * 4 * game.getPlayerData().getVampHealMultiplier())));
            healFlash = 0.3f;
            game.playSoundClear();
        } else if (lines == 0) {
            combo = 0;
        }
    }

    private void applyReward(Artifact type) {
        game.getPlayerData().addArtifact(type);
        switch (type) {
            case HP:
                game.getPlayerData().setMaxHp(game.getPlayerData().getMaxHp() + 30);
                game.getPlayerData().setHp(game.getPlayerData().getMaxHp());
                break;
            case DMG: game.getPlayerData().addDamageMultiplier(0.4f); break;
            case SPD: game.getPlayerData().setFallSpeed(Math.max(0.15f, game.getPlayerData().getFallSpeed() + 0.05f)); break;
            case VAMP: game.getPlayerData().addVampHealMultiplier(0.5f); break;
            case SHLD: game.getPlayerData().addShieldDelay(1.5f); break;
        }
    }

    @Override public void dispose() { shapeRenderer.dispose(); }

    private class BattleInputProcessor extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            if (state == State.GAMEOVER) {
                if (keycode == Input.Keys.R) {
                    game.setScreen(new MenuScreen(game));
                    return true;
                }
                return false;
            }

            if (state == State.REWARD) {
                int idx = -1;
                if (keycode == Input.Keys.NUM_1) idx = 0;
                if (keycode == Input.Keys.NUM_2) idx = 1;
                if (keycode == Input.Keys.NUM_3) idx = 2;

                if (idx != -1) {
                    applyReward(rewards.get(idx));
                    game.getPlayerData().incrementFloor();
                    game.setScreen(new MapScreen(game));
                    return true;
                }
                return false;
            }

            if (state == State.BATTLE) {
                if (keycode == Input.Keys.K) {
                    enemy.takeDamage(9999);
                    return true;
                }
                if (keycode == Input.Keys.LEFT) {
                    engine.movePiece(-1);
                    return true;
                }
                if (keycode == Input.Keys.RIGHT) {
                    engine.movePiece(1);
                    return true;
                }
                if (keycode == Input.Keys.UP) {
                    engine.rotatePiece();
                    return true;
                }
                if (keycode == Input.Keys.SPACE) {
                    int lines = engine.dropDown();
                    if (lines != -1) {
                        processLines(lines);
                        fallTimer = 0;
                        moveTimer = 0;
                    }
                    return true;
                }
            }
            return false;
        }
    }
}
