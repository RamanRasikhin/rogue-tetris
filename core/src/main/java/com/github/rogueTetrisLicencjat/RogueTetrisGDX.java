package com.github.rogueTetrisLicencjat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class RogueTetrisGDX extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    private PlayerData playerData;
    private Assets assets;

    private Sound soundClick;
    private Sound soundClear;
    private Sound soundHit;

    public RogueTetrisGDX() {
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerData = new PlayerData();
        assets = new Assets();

        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 20;
            parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ąćęłńóśźżĄĆĘŁŃÓŚŹŻ";
            font = generator.generateFont(parameter);
            generator.dispose();
        } catch (Exception e) {
            font = new BitmapFont();
        }

        soundClick = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
        soundClear = Gdx.audio.newSound(Gdx.files.internal("clear.wav"));
        soundHit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));

        this.setScreen(new LoadingScreen(this));
    }

    public void playSound(Sound sound) {
        if (playerData != null && playerData.isSoundEnabled()) {
            sound.play(1.0f);
        }
    }

    public Sound playSoundClick() {
        playSound(soundClick);
        return soundClick;
    }
    public Sound playSoundHit() {
        playSound(soundHit);
        return soundHit;
    }
    public Sound playSoundClear() {
        playSound(soundClear);
        return soundClear;
    }

    public SpriteBatch getBatch() { return batch; }
    public BitmapFont getFont() { return font; }
    public PlayerData getPlayerData() { return playerData; }
    public Assets getAssets() { return assets; }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (assets != null) assets.dispose();
        if (soundClick != null) soundClick.dispose();
        if (soundClear != null) soundClear.dispose();
        if (soundHit != null) soundHit.dispose();
    }
}
