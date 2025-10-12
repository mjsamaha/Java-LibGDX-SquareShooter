package com.msamaha.squareshooter.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.msamaha.squareshooter.constants.Constants;
import com.msamaha.squareshooter.ecs.components.PlayerComponent;
import com.msamaha.squareshooter.ecs.components.TransformComponent;
import com.msamaha.squareshooter.ecs.components.VelocityComponent;
import com.msamaha.squareshooter.ecs.components.WeaponComponent;
import com.msamaha.squareshooter.ecs.entities.BaseEntity;
import com.msamaha.squareshooter.entities.projectile.Projectile;
import com.msamaha.squareshooter.game.AssetManager;

/**
 * Player - The player-controlled ship entity.
 * Extends BaseEntity to provide movement, rotation, shooting, and state management.
 *
 * <p>The Player class handles all player-specific gameplay logic including:
 * <ul>
 *   <li>WASD movement with normalized velocity vectors</li>
 *   <li>Mouse-based rotation with smooth interpolation</li>
 *   <li>Screen boundary clamping</li>
 *   <li>Automatic shooting mechanics</li>
 *   <li>Health and score management through components</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class Player extends BaseEntity {

    /** Player-specific component for health, score, and upgrades */
    private final PlayerComponent playerComponent;

    /** Weapon component for shooting mechanics */
    private final WeaponComponent weaponComponent;

    /** Projectile factory for creating bullets */
    private final com.msamaha.squareshooter.game.ProjectileFactory projectileFactory;

    /** Camera reference for mouse position calculations */
    private com.badlogic.gdx.graphics.Camera camera;

    /** Temporary vector for movement calculations */
    private final Vector2 movementDirection;

    /** Temporary vector for mouse position calculations */
    private final Vector3 mousePosition;

    /**
     * Constructor - Initialize player at specified position
     * @param x Initial X position
     * @param y Initial Y position
     */
    public Player(float x, float y) {
        this(x, y, null);
    }

    /**
     * Constructor - Initialize player with camera reference
     * @param x Initial X position
     * @param y Initial Y position
     * @param camera Camera for mouse position calculations
     */
    public Player(float x, float y, com.badlogic.gdx.graphics.Camera camera) {
        super(new com.badlogic.gdx.graphics.g2d.TextureRegion(AssetManager.getInstance().playerTexture),
              x, y, Constants.Player.WIDTH, Constants.Player.HEIGHT);

        this.playerComponent = new PlayerComponent();
        this.weaponComponent = new WeaponComponent();
        this.projectileFactory = new com.msamaha.squareshooter.game.ProjectileFactory(AssetManager.getInstance());
        this.camera = camera;
        this.movementDirection = new Vector2();
        this.mousePosition = new Vector3();

        // Initialize velocity component with player speed
        this.velocity = new VelocityComponent(0, 0, Constants.Player.SPEED);
    }

    @Override
    public void update(float delta) {
        // Update components
        playerComponent.update(delta);
        weaponComponent.update(delta);

        // Handle player input and movement
        handleInput(delta);

        // Update rotation toward mouse
        updateRotation();

        // Handle automatic shooting
        handleShooting();

        // Check if player is still alive
        if (!playerComponent.isAlive()) {
            setActive(false);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (texture == null || !isActive()) {
            return;
        }

        // Draw player with rotation and scaling
        batch.draw(
            texture,
            transform.x, transform.y,
            Constants.Player.WIDTH / 2f, Constants.Player.HEIGHT / 2f, // Origin (center)
            Constants.Player.WIDTH, Constants.Player.HEIGHT,           // Size
            transform.scale, transform.scale,                         // Scale
            transform.rotation                                           // Rotation
        );

        // Optional: Add visual effects for invincibility
        if (playerComponent.isInvincible()) {
            // Could add blinking effect or shield visual here
        }
    }

    /**
     * Handle player input for movement
     * @param delta Time elapsed since last frame
     */
    private void handleInput(float delta) {
        // Reset movement direction
        movementDirection.set(0, 0);

        // Check WASD input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) movementDirection.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) movementDirection.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) movementDirection.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) movementDirection.x += 1;

        // Normalize movement vector for consistent speed diagonally
        if (movementDirection.len2() > 0) {
            movementDirection.nor();
            velocity.setVelocityFromDirection(movementDirection.x, movementDirection.y, Constants.Player.SPEED);
        } else {
            // No input - stop movement
            velocity.stop();
        }

        // Apply movement
        move(delta);

        // Clamp to screen bounds
        clampToScreenBounds();
    }

    /**
     * Update player rotation to face mouse cursor
     */
    private void updateRotation() {
        if (camera == null) {
            return;
        }

        // Get mouse position in world coordinates
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        // Calculate angle between player center and mouse
        float centerX = transform.x + Constants.Player.WIDTH / 2f;
        float centerY = transform.y + Constants.Player.HEIGHT / 2f;

        float angle = MathUtils.atan2(
            mousePosition.y - centerY,
            mousePosition.x - centerX
        ) * MathUtils.radiansToDegrees;

        // Smooth rotation using lerp
        float lerpFactor = 0.15f; // Adjust for rotation smoothness
        transform.rotation = MathUtils.lerpAngleDeg(transform.rotation, angle, lerpFactor);
    }

    /**
     * Handle automatic shooting mechanics
     */
    private void handleShooting() {
        // Try to fire weapon
        if (weaponComponent.tryFire()) {
            fireProjectiles();
        }
    }

    /**
     * Fire projectiles based on current weapon configuration
     */
    private void fireProjectiles() {
        // Calculate spawn position (slightly in front of player center)
        Vector2 spawnPosition = projectileFactory.calculateSpawnPosition(
            transform.x, transform.y, transform.rotation, 20f
        );

        // Create projectiles based on weapon configuration
        Projectile[] projectiles = projectileFactory.createProjectileSpread(
            spawnPosition.x, spawnPosition.y, transform.rotation, weaponComponent
        );

        // Log firing for debugging
        Gdx.app.log("Player", "Firing " + projectiles.length + " projectiles - Type: " +
                    weaponComponent.getProjectileType() + ", Weapon Level: " + weaponComponent.getWeaponLevel());

        // TODO: Add projectiles to EntityManager when we integrate with GameScreen
        // For now, just create them (they'll be garbage collected)
    }

    /**
     * Clamp player position within screen bounds
     */
    private void clampToScreenBounds() {
        float halfWidth = Constants.Player.WIDTH / 2f;
        float halfHeight = Constants.Player.HEIGHT / 2f;

        // Clamp X position
        if (transform.x < Constants.World.MIN_X) {
            transform.x = Constants.World.MIN_X;
        } else if (transform.x > Constants.World.MAX_X - Constants.Player.WIDTH) {
            transform.x = Constants.World.MAX_X - Constants.Player.WIDTH;
        }

        // Clamp Y position
        if (transform.y < Constants.World.MIN_Y) {
            transform.y = Constants.World.MIN_Y;
        } else if (transform.y > Constants.World.MAX_Y - Constants.Player.HEIGHT) {
            transform.y = Constants.World.MAX_Y - Constants.Player.HEIGHT;
        }

        // Update hitbox position after clamping
        hitbox.setPosition(transform.x, transform.y);
    }

    /**
     * Take damage from external sources
     * @param damage Amount of damage to take
     * @return true if damage was taken
     */
    public boolean takeDamage(int damage) {
        return playerComponent.takeDamage(damage);
    }

    /**
     * Add score points
     * @param points Points to add
     */
    public void addScore(int points) {
        playerComponent.addScore(points);
    }

    /**
     * Heal the player
     * @param healAmount Amount to heal
     */
    public void heal(int healAmount) {
        playerComponent.heal(healAmount);
    }

    /**
     * Upgrade the player's weapon
     */
    public void upgradeWeapon() {
        weaponComponent.upgrade();
    }

    /**
     * Set camera for mouse position calculations
     * @param camera Camera reference
     */
    public void setCamera(com.badlogic.gdx.graphics.Camera camera) {
        this.camera = camera;
    }

    // ===== GETTERS =====

    public PlayerComponent getPlayerComponent() {
        return playerComponent;
    }

    public WeaponComponent getWeaponComponent() {
        return weaponComponent;
    }

    public int getCurrentHealth() {
        return playerComponent.getCurrentHealth();
    }

    public int getMaxHealth() {
        return playerComponent.getMaxHealth();
    }

    public int getScore() {
        return playerComponent.getScore();
    }

    public int getLives() {
        return playerComponent.getLives();
    }

    public int getWeaponLevel() {
        return weaponComponent.getWeaponLevel();
    }

    public boolean isInvincible() {
        return playerComponent.isInvincible();
    }

    public float getHealthPercentage() {
        return playerComponent.getHealthPercentage();
    }

    public boolean canFire() {
        return weaponComponent.canFire();
    }

    public boolean isOnCooldown() {
        return weaponComponent.isOnCooldown();
    }
}
