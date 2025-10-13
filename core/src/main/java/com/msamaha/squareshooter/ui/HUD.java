package com.msamaha.squareshooter.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.msamaha.squareshooter.constants.Constants;
import com.msamaha.squareshooter.ecs.components.PlayerComponent;
import com.msamaha.squareshooter.ecs.components.WeaponComponent;
import com.msamaha.squareshooter.entities.player.Player;
import com.msamaha.squareshooter.game.AssetManager;

/**
 * HUD - Heads-Up Display for player statistics and game information.
 * Displays real-time player stats using the Minecraft font for consistent game aesthetic.
 *
 * <p>This class handles:
 * <ul>
 *   <li>Real-time display of health, score, and weapon information</li>
 *   <li>Minecraft font integration using the same pattern as StarterGameScreen</li>
 *   <li>Health bar visualization</li>
 *   <li>Responsive positioning for different screen sizes</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class HUD {

    /** Player reference for accessing stats */
    private final Player player;

    /** Sprite batch for text rendering */
    private final SpriteBatch batch;

    /** Shape renderer for health bar and UI elements */
    private final ShapeRenderer shapeRenderer;

    /** Minecraft font using same pattern as StarterGameScreen */
    private final BitmapFont minecraftFont;

    /** Cached strings to avoid garbage collection */
    private final StringBuilder stringBuilder;

    /**
     * Constructor - Initialize HUD with player reference
     * @param player The player entity to display stats for
     */
    public HUD(Player player) {
        this.player = player;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();

        // Use same font loading pattern as StarterGameScreen
        this.minecraftFont = AssetManager.getInstance().minecraftRegular;

        this.stringBuilder = new StringBuilder();

        // Configure font for better readability
        minecraftFont.setColor(Constants.Colors.UI_TEXT);
        minecraftFont.getData().setScale(1.0f);
    }

    /**
     * Render the HUD overlay
     */
    public void render() {
        if (player == null || !player.isActive()) {
            return;
        }

        // Get current player stats
        PlayerComponent playerStats = player.getPlayerComponent();
        WeaponComponent weaponStats = player.getWeaponComponent();

        // Set up rendering matrices
        batch.begin();

        // Render text information
        renderPlayerStats(playerStats, weaponStats);

        // End text batch
        batch.end();

        // Render health bar and other graphical elements
        renderHealthBar(playerStats);
        renderWeaponInfo(weaponStats);
    }

    /**
     * Render player statistics text
     * @param playerStats Player component with current stats
     * @param weaponStats Weapon component with weapon info
     */
    private void renderPlayerStats(PlayerComponent playerStats, WeaponComponent weaponStats) {
        float startY = Constants.Window.HEIGHT - 30f;
        float lineHeight = 25f;

        // Health
        stringBuilder.setLength(0);
        stringBuilder.append("HP: ").append(playerStats.getCurrentHealth()).append("/").append(playerStats.getMaxHealth());
        minecraftFont.draw(batch, stringBuilder.toString(), 20f, startY);

        // Score
        stringBuilder.setLength(0);
        stringBuilder.append("Score: ").append(playerStats.getScore());
        minecraftFont.draw(batch, stringBuilder.toString(), 20f, startY - lineHeight);

        // Lives
        stringBuilder.setLength(0);
        stringBuilder.append("Lives: ").append(playerStats.getLives());
        minecraftFont.draw(batch, stringBuilder.toString(), 20f, startY - lineHeight * 2);

        // Weapon Level
        stringBuilder.setLength(0);
        stringBuilder.append("Weapon: ").append(weaponStats.getProjectileType().name()).append(" Lv.").append(weaponStats.getWeaponLevel());
        minecraftFont.draw(batch, stringBuilder.toString(), 20f, startY - lineHeight * 3);


    }

    /**
     * Render the health bar visualization
     * @param playerStats Player component with health information
     */
    private void renderHealthBar(PlayerComponent playerStats) {
        if (playerStats.getMaxHealth() <= 0) return;

        float barWidth = 200f;
        float barHeight = 20f;
        float barX = Constants.Window.WIDTH - barWidth - 20f;
        float barY = Constants.Window.HEIGHT - 40f;

        float healthPercentage = playerStats.getHealthPercentage();

        // Draw background
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Background (dark red)
        shapeRenderer.setColor(0.3f, 0.1f, 0.1f, 0.8f);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        // Health (green to red based on percentage)
        if (healthPercentage > 0.6f) {
            shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 0.9f); // Green
        } else if (healthPercentage > 0.3f) {
            shapeRenderer.setColor(0.8f, 0.8f, 0.2f, 0.9f); // Yellow
        } else {
            shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 0.9f); // Red
        }
        shapeRenderer.rect(barX, barY, barWidth * healthPercentage, barHeight);

        shapeRenderer.end();

        // Draw border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Constants.Colors.UI_TEXT);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.end();
    }

    /**
     * Render weapon-specific information
     * @param weaponStats Weapon component with weapon data
     */
    private void renderWeaponInfo(WeaponComponent weaponStats) {
        // Draw weapon level indicator
        float indicatorX = Constants.Window.WIDTH - 100f;
        float indicatorY = Constants.Window.HEIGHT - 80f;
        float indicatorSize = 15f;

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw weapon level circles
        for (int i = 0; i < 5; i++) {
            float x = indicatorX + (i * (indicatorSize + 5f));

            if (i < weaponStats.getWeaponLevel()) {
                // Active level (bright)
                shapeRenderer.setColor(Constants.Colors.HEALTH_FULL);
            } else {
                // Inactive level (dim)
                shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.5f);
            }

            shapeRenderer.circle(x, indicatorY, indicatorSize / 2f);
        }

        shapeRenderer.end();
    }

    /**
     * Update HUD (called each frame)
     * @param delta Time elapsed since last frame
     */
    public void update(float delta) {
        // Update any animated elements if needed
        // Currently just polls player stats each frame in render()
    }

    /**
     * Dispose of HUD resources
     */
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }

    /**
     * Resize HUD for new screen dimensions
     * @param width New screen width
     * @param height New screen height
     */
    public void resize(int width, int height) {
        // HUD automatically adapts to new window size through Constants.Window references
        // No specific resize logic needed as positions are calculated dynamically
    }
}
