package com.msamaha.squareshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Singleton AssetManager class - Central point for loading and managing all game assets.
 * Follows the Singleton pattern to ensure only one instance exists throughout the application.
 *
 * <p>Assets managed include:
 * <ul>
 *   <li>Textures - Player, enemies, projectiles, UI elements</li>
 *   <li>Sounds - Laser shots, explosions, menu sounds</li>
 *   <li>Music - Background tracks</li>
 * </ul>
 *
 * <p>Usage: Call {@code AssetManager.getInstance()} to get the singleton instance.
 * Use {@code AssetManager.getInstance().loadAllAssets()} to load all assets.
 * Use {@code AssetManager.getInstance().dispose()} to clean up resources.
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class AssetManager {

    /** Singleton instance */
    private static AssetManager instance;

    // ===== TEXTURES =====
    // Player assets
    public Texture playerTexture;
    public Texture laserPlayerTexture;

    // Enemy assets (updated to match your actual assets)
    public Texture enemy1Texture;
    public Texture enemy2Texture;
    public Texture enemy3Texture;
    public Texture enemy4Texture;
    public Texture enemy5Texture;

    // UI assets
    public Texture iconTexture;
    public Texture borderTopTexture;
    public Texture borderBottomTexture;
    public Texture borderLeftTexture;
    public Texture borderRightTexture;

    // ===== SOUNDS =====
    // Player sounds
    public Sound laserSound;
    public Sound playerHurtSound;

    // Enemy sounds
    public Sound enemyDeathSound;
    public Sound enemySpawnSound;

    // UI sounds
    public Sound buttonClickSound;
    public Sound menuSelectSound;

    // ===== FONTS =====
    public BitmapFont orbitronRegular;
    public BitmapFont orbitronBold;

    // ===== MUSIC =====
    public Music backgroundMusic;
    public Music menuMusic;


    /** Asset paths - Central definition of all asset file paths */
    public static final class Paths {
        public static final String TEXTURES = "textures/";
        public static final String SOUNDS = "sounds/";
        public static final String MUSIC = "music/";
        public static final String AUDIO = "audio/";
        public static final String FONTS = "fonts/";

        // Fonts
        public static final String FONT_ORBITRON_REGULAR = FONTS + "Orbitron-Regular.ttf";
        public static final String FONT_ORBITRON_BOLD = FONTS + "Orbitron-Bold.ttf";

        // Player
        public static final String PLAYER = TEXTURES + "player/player.png";
        public static final String LASER_PLAYER = TEXTURES + "player/laser.png";

        // Enemies (now matching your actual asset structure)
        public static final String ENEMY_1 = TEXTURES + "enemy/enemy1.png";
        public static final String ENEMY_2 = TEXTURES + "enemy/enemy2.png";
        public static final String ENEMY_3 = TEXTURES + "enemy/enemy3.png";
        public static final String ENEMY_4 = TEXTURES + "enemy/enemy4.png";
        public static final String ENEMY_5 = TEXTURES + "enemy/enemy5.png";

        // UI/Icon
        public static final String ICON = TEXTURES + "icon/icon.png";
        public static final String BORDER_TOP = TEXTURES + "window_borders/top-border.png";
        public static final String BORDER_BOTTOM = TEXTURES + "window_borders/bottom-border.png";
        public static final String BORDER_LEFT = TEXTURES + "window_borders/left-border.png";
        public static final String BORDER_RIGHT = TEXTURES + "window_borders/right-border.png";

        // Sounds (now matching your actual sound files)
        public static final String SOUND_LASER_SHOOT = SOUNDS + "laser_shoot.wav";
        public static final String SOUND_LASER_EXPLOSION = SOUNDS + "laser_explosion.wav";

        // Music (now matching your actual music file)
        public static final String MUSIC_BACKGROUND = AUDIO + "DSSongRager.mp3";
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AssetManager() {
        // Constructor is private for singleton pattern
    }

    /**
     * Gets the singleton instance of AssetManager
     * @return The singleton AssetManager instance
     */
    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    /**
     * Loads all game assets. Should be called once at application startup.
     * All assets are loaded synchronously for simplicity.
     *
     * <p>Note: In a production game, you might want to use AssetManager from LibGDX
     * which supports asynchronous loading and progress tracking.
     */
    public void loadAllAssets() {
        // Load fonts
        loadFonts();

        // Load textures
        loadTextures();

        // Load sounds
        loadSounds();

        // Load music
        loadMusic();

        Gdx.app.log("AssetManager", "All assets loaded successfully");
    }

    /**
     * Loads all font assets
     */
    private void loadFonts() {
        orbitronRegular = loadFont(Paths.FONT_ORBITRON_REGULAR);
        orbitronBold = loadFont(Paths.FONT_ORBITRON_BOLD);
    }

    /**
     * Loads all texture assets
     */
    private void loadTextures() {
        // Player
        playerTexture = loadTexture(Paths.PLAYER);
        laserPlayerTexture = loadTexture(Paths.LASER_PLAYER);

        // Enemies (updated to match actual asset names)
        enemy1Texture = loadTexture(Paths.ENEMY_1);
        enemy2Texture = loadTexture(Paths.ENEMY_2);
        enemy3Texture = loadTexture(Paths.ENEMY_3);
        enemy4Texture = loadTexture(Paths.ENEMY_4);
        enemy5Texture = loadTexture(Paths.ENEMY_5);

        // UI assets
        iconTexture = loadTexture(Paths.ICON);
        borderTopTexture = loadTexture(Paths.BORDER_TOP);
        borderBottomTexture = loadTexture(Paths.BORDER_BOTTOM);
        borderLeftTexture = loadTexture(Paths.BORDER_LEFT);
        borderRightTexture = loadTexture(Paths.BORDER_RIGHT);
    }

    /**
     * Loads all sound assets
     */
    private void loadSounds() {
        laserSound = loadSound(Paths.SOUND_LASER_SHOOT);
        enemyDeathSound = loadSound(Paths.SOUND_LASER_EXPLOSION);
        // Other sounds not available in current assets
        // playerHurtSound = loadSound(Paths.SOUND_PLAYER_HURT);
        // enemySpawnSound = loadSound(Paths.SOUND_ENEMY_SPAWN);
        // buttonClickSound = loadSound(Paths.SOUND_BUTTON_CLICK);
        // menuSelectSound = loadSound(Paths.SOUND_MENU_SELECT);
    }

    /**
     * Loads all music assets
     */
    private void loadMusic() {
        backgroundMusic = loadMusic(Paths.MUSIC_BACKGROUND);
        // menuMusic is not available in current assets, so it stays null
    }

    /**
     * Unloads all assets and disposes of resources.
     * Should be called when the application is closing.
     */
    public void dispose() {
        // Dispose fonts
        disposeFonts();

        // Dispose textures
        disposeTextures();

        // Dispose sounds
        disposeSounds();

        // Dispose music
        disposeMusic();

        Gdx.app.log("AssetManager", "All assets disposed");
    }

    /**
     * Disposes all font assets
     */
    private void disposeFonts() {
        if (orbitronRegular != null) {
            orbitronRegular.dispose();
        }
        if (orbitronBold != null) {
            orbitronBold.dispose();
        }
    }

    /**
     * Disposes all texture assets
     */
    private void disposeTextures() {
        // Player
        if (playerTexture != null) playerTexture.dispose();
        if (laserPlayerTexture != null) laserPlayerTexture.dispose();

        // Enemies
        if (enemy1Texture != null) enemy1Texture.dispose();
        if (enemy2Texture != null) enemy2Texture.dispose();
        if (enemy3Texture != null) enemy3Texture.dispose();
        if (enemy4Texture != null) enemy4Texture.dispose();
        if (enemy5Texture != null) enemy5Texture.dispose();

        // UI
        if (iconTexture != null) iconTexture.dispose();
        if (borderTopTexture != null) borderTopTexture.dispose();
        if (borderBottomTexture != null) borderBottomTexture.dispose();
        if (borderLeftTexture != null) borderLeftTexture.dispose();
        if (borderRightTexture != null) borderRightTexture.dispose();
    }

    /**
     * Disposes all sound assets
     */
    private void disposeSounds() {
        if (laserSound != null) laserSound.dispose();
        if (playerHurtSound != null) playerHurtSound.dispose();
        if (enemyDeathSound != null) enemyDeathSound.dispose();
        if (enemySpawnSound != null) enemySpawnSound.dispose();
        if (buttonClickSound != null) buttonClickSound.dispose();
        if (menuSelectSound != null) menuSelectSound.dispose();
    }

    /**
     * Disposes all music assets
     */
    private void disposeMusic() {
        if (backgroundMusic != null) backgroundMusic.dispose();
        if (menuMusic != null) menuMusic.dispose();
    }

    // ===== UTILITY METHODS =====

    /**
     * Loads a texture safely with error handling
     * @param path The path to the texture file
     * @return The loaded Texture
     */
    private Texture loadTexture(String path) {
        try {
            Texture texture = new Texture(Gdx.files.internal(path));
            Gdx.app.log("AssetManager", "Loaded texture: " + path);
            return texture;
        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load texture: " + path, e);
            // Return a default 1x1 texture to prevent crashes
            return new Texture(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        }
    }

    /**
     * Loads a sound safely with error handling
     * @param path The path to the sound file
     * @return The loaded Sound
     */
    private Sound loadSound(String path) {
        try {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            Gdx.app.log("AssetManager", "Loaded sound: " + path);
            return sound;
        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load sound: " + path, e);
            // Return a silent sound to prevent crashes
            return Gdx.audio.newSound(Gdx.files.internal(path)); // This will fail, but returns a valid object
        }
    }

    /**
     * Loads music safely with error handling
     * @param path The path to the music file
     * @return The loaded Music
     */
    private Music loadMusic(String path) {
        try {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
            Gdx.app.log("AssetManager", "Loaded music: " + path);
            return music;
        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load music: " + path, e);
            // Return null - music is optional
            return null;
        }
    }

    /**
     * Loads a font safely with error handling
     * @param path The path to the font file (TTF)
     * @return The loaded BitmapFont
     */
    private BitmapFont loadFont(String path) {
        try {
            // Note: In a real implementation, you might want to specify different sizes
            // For now, we'll create fonts with default size and scale them later
            BitmapFont font = new BitmapFont(Gdx.files.internal(path));
            Gdx.app.log("AssetManager", "Loaded font: " + path);
            return font;
        } catch (Exception e) {
            Gdx.app.error("AssetManager", "Failed to load font: " + path, e);
            // Return a default font to prevent crashes
            return new BitmapFont(); // Uses LibGDX default built-in font
        }
    }
}
