package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    private final AssetManager manager;
    private final ProceduralTextures generated = new ProceduralTextures();

    public Texture background;
    public Texture button1;
    public Texture button2;
    public Texture soundOn;
    public Texture soundOff;

    public Texture bossBg;
    public Texture chillBg;
    public Texture artBg;

    public Texture bossIcon;
    public Texture epicIcon;
    public Texture fightIcon;
    public Texture chillIcon;
    public Texture unknownIcon;

    public Texture vitalityArt;
    public Texture powerArt;
    public Texture focusArt;
    public Texture vampireArt;
    public Texture shieldArt;

    public Texture winImage;
    public Texture lostImage;

    public Texture[] tetrisBlocks;

    public Texture gameInfo2;

    public Texture healthBarFrame;
    public Texture healthBarFill;

    public Assets() {
        manager = new AssetManager();
    }

    public void loadAll() {
        manager.load("background.png", Texture.class);

        manager.load("bossbg.png", Texture.class);
        manager.load("chillbg.png", Texture.class);
        manager.load("artbg.png", Texture.class);

        manager.load("fight.png", Texture.class);
        manager.load("chill.png", Texture.class);
        manager.load("unknown.png", Texture.class);

        manager.load("vitality.png", Texture.class);
        manager.load("power.png", Texture.class);
        manager.load("focus.png", Texture.class);
        manager.load("vampire.png", Texture.class);
        manager.load("shield.png", Texture.class);

        manager.load("win.png", Texture.class);
        manager.load("lost.png", Texture.class);




    }

    public boolean update() {
        boolean isFinished = manager.update();
        if (isFinished && background == null) {
            assignAssets();
        }
        return isFinished;
    }

    private void assignAssets() {
        background = manager.get("background.png", Texture.class);
        button1 = generated.panel(false);
        button2 = generated.panel(true);
        soundOn = generated.sound(true);
        soundOff = generated.sound(false);

        bossBg = manager.get("bossbg.png", Texture.class);
        chillBg = manager.get("chillbg.png", Texture.class);
        artBg = manager.get("artbg.png", Texture.class);

        bossIcon = generated.encounter(true);
        epicIcon = generated.encounter(false);
        fightIcon = manager.get("fight.png", Texture.class);
        chillIcon = manager.get("chill.png", Texture.class);
        unknownIcon = manager.get("unknown.png", Texture.class);

        vitalityArt = manager.get("vitality.png", Texture.class);
        powerArt = manager.get("power.png", Texture.class);
        focusArt = manager.get("focus.png", Texture.class);
        vampireArt = manager.get("vampire.png", Texture.class);
        shieldArt = manager.get("shield.png", Texture.class);

        winImage = manager.get("win.png", Texture.class);
        lostImage = manager.get("lost.png", Texture.class);

        gameInfo2 = generated.card();

        healthBarFrame = generated.solid();
        healthBarFill = generated.solid();

        tetrisBlocks = new Texture[8];
        for (int i = 1; i <= 8; i++) {
            tetrisBlocks[i-1] = generated.block(i - 1);
        }
    }

    public float getProgress() {
        return manager.getProgress();
    }

    public void dispose() {
        manager.dispose();
        generated.dispose();
    }
}
