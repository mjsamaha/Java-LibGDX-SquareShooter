package com.msamaha.squareshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.msamaha.squareshooter.constants.Constants;
import com.msamaha.squareshooter.entities.EntityManager;
import com.msamaha.squareshooter.entities.player.Player;
import com.msamaha.squareshooter.ui.HUD;

/**
 * GameScreen - The main gameplay screen where the core game takes place.
 * Implements full gameplay mechanics with player movement, shooting, and entity management.
 *
 * <p>This screen manages:
 * <ul>
 *   <li>Player entity with movement and shooting</li>
 *   <li>EntityManager for all game entities</li>
 *   <li>Camera and viewport for proper rendering</li>
 *   <li>Projectile system integration</li>
 *   <li>Asset management and rendering pipeline</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class GameScreen implements Screen {

    /** Main game camera for rendering */
    private OrthographicCamera camera;

    /** Viewport for handling different screen sizes */
    private Viewport viewport;

    /** Sprite batch for rendering */
    private SpriteBatch batch;

    /** Entity manager for handling all game entities */
    private EntityManager entityManager;

    /** Player entity */
    private Player player;

    /** HUD for displaying player stats */
    private HUD hud;

    /**
     * Constructor - Initialize the game screen
     */
    public GameScreen() {
        // Screen is initialized in show() method
    }

    @Override
    public void show() {
        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.Window.WIDTH, Constants.Window.HEIGHT);

        // Initialize viewport
        viewport = new FitViewport(Constants.Window.WIDTH, Constants.Window.HEIGHT, camera);
        viewport.apply();

        // Initialize sprite batch
        batch = new SpriteBatch();

        // Initialize entity manager
        entityManager = new EntityManager();

        // Create player at center of screen
        float startX = Constants.Player.START_X;
        float startY = Constants.Player.START_Y;

        player = new Player(startX, startY, camera);

        // Set entity manager reference for projectile management
        player.setEntityManager(entityManager);

        // Add player to entity manager
        entityManager.addEntity(player);

        // Initialize HUD with player reference
        hud = new HUD(player);

        Gdx.app.log("GameScreen", "Game screen initialized with player, entity manager, and HUD");
        Gdx.app.log("GameScreen", "Player stats - Health: " + player.getCurrentHealth() +
                    ", Weapon Level: " + player.getWeaponLevel());
    }

    @Override
    public void render(float delta) {
        // Limit delta to prevent large jumps
        if (delta > 1/30f) delta = 1/30f;

        // Update camera
        camera.update();

        // Update all entities through entity manager
        entityManager.update(delta);

        // Clear screen with dark background
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1); // Dark blue-gray
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set up batch with camera matrix
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Render all entities through entity manager
        entityManager.render(batch);

        // End batch
        batch.end();

        // Render HUD on top of everything (overlay layer)
        hud.render();

        // Log debug info occasionally
        if (Math.random() < 0.01) { // Log roughly every 100 frames
            Gdx.app.log("GameScreen", "Entities: " + entityManager.getEntityCount() +
                        ", Player Health: " + player.getCurrentHealth() +
                        ", Score: " + player.getScore());
        }
    }

    @Override
    public void resize(int width, int height) {
        // Update viewport for new dimensions
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        Gdx.app.log("GameScreen", "Screen resized to: " + width + "x" + height);
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "Game paused");
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "Game resumed");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "Game screen hidden");
    }

    @Override
    public void dispose() {
        // Clean up all resources
        if (batch != null) {
            batch.dispose();
        }

        if (entityManager != null) {
            entityManager.dispose();
        }

        if (hud != null) {
            hud.dispose();
        }

        Gdx.app.log("GameScreen", "Game screen disposed");
    }

    // ===== GETTERS FOR EXTERNAL ACCESS =====

    /**
     * Get the entity manager for external access
     * @return The entity manager instance
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Get the player entity
     * @return The player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the camera for external access
     * @return The orthographic camera
     */
    public OrthographicCamera getCamera() {
        return camera;
    }
}
