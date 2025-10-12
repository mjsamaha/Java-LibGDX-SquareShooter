package com.msamaha.squareshooter;

import com.badlogic.gdx.Game;
import com.msamaha.squareshooter.game.AssetManager;
import com.msamaha.squareshooter.screens.StarterGameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SquareShooter extends Game {
    @Override
    public void create() {
        // Initialize asset manager and load all assets
        AssetManager.getInstance().loadAllAssets();

        setScreen(new StarterGameScreen());
    }
}
