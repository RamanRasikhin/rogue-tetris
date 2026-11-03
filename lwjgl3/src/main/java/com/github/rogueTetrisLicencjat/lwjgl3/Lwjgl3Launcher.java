package com.github.rogueTetrisLicencjat.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.rogueTetrisLicencjat.RogueTetrisGDX;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static void createApplication() {
        boolean smoke = Boolean.getBoolean("rogueTetris.smoke");
        RogueTetrisGDX app = smoke ? new RogueTetrisGDX() {
            private int readyFrames;
            @Override public void render() {
                super.render();
                if (getAssets().background != null && ++readyFrames >= 10) {
                    System.out.println("Desktop startup smoke passed");
                    com.badlogic.gdx.Gdx.app.exit();
                }
            }
        } : new RogueTetrisGDX();
        Lwjgl3ApplicationConfiguration configuration = getDefaultConfiguration();
        configuration.setInitialVisible(!smoke);
        new Lwjgl3Application(app, configuration);
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("RogueTetris");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);

        configuration.setWindowedMode(800, 600);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        return configuration;
    }
}
