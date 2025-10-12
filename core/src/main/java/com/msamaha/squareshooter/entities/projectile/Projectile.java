package com.msamaha.squareshooter.entities.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.msamaha.squareshooter.constants.Constants;
import com.msamaha.squareshooter.ecs.entities.BaseEntity;
import com.msamaha.squareshooter.game.AssetManager;

/**
 * Projectile - Represents a bullet, laser, or energy shot fired by player or enemies.
 * Extends BaseEntity to provide movement, lifetime management, and collision detection.
 *
 * <p>The Projectile class handles:
 * <ul>
 *   <li>Straight-line movement with constant velocity</li>
 *   <li>Lifetime management and automatic cleanup</li>
 *   <li>Boundary checking for off-screen removal</li>
 *   <li>Damage properties for collision handling</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class Projectile extends BaseEntity {

    /** Speed of the projectile in pixels per second */
    private float speed;

    /** Current lifetime of the projectile in seconds */
    private float lifetime;

    /** Maximum lifetime before projectile expires */
    private float maxLifetime;

    /** Damage dealt to entities on collision */
    private int damage;

    /** Flag indicating if projectile pierces through enemies */
    private boolean piercing;

    /** Owner of the projectile (player or enemy) */
    private ProjectileOwner owner;

    /** Enumeration of projectile owners */
    public enum ProjectileOwner {
        PLAYER,
        ENEMY
    }

    /**
     * Constructor - Initialize projectile with position, direction, and properties
     * @param x Initial X position
     * @param y Initial Y position
     * @param direction Direction vector (will be normalized)
     * @param speed Movement speed in pixels per second
     * @param damage Damage dealt on collision
     * @param maxLifetime Maximum lifetime in seconds
     */
    public Projectile(float x, float y, Vector2 direction, float speed, int damage, float maxLifetime) {
        this(x, y, direction, speed, damage, maxLifetime, ProjectileOwner.PLAYER, false);
    }

    /**
     * Constructor - Initialize projectile with all properties
     * @param x Initial X position
     * @param y Initial Y position
     * @param direction Direction vector (will be normalized)
     * @param speed Movement speed in pixels per second
     * @param damage Damage dealt on collision
     * @param maxLifetime Maximum lifetime in seconds
     * @param owner Owner of the projectile
     * @param piercing Whether projectile pierces through enemies
     */
    public Projectile(float x, float y, Vector2 direction, float speed, int damage, float maxLifetime,
                     ProjectileOwner owner, boolean piercing) {
        super(new com.badlogic.gdx.graphics.g2d.TextureRegion(AssetManager.getInstance().laserPlayerTexture),
              x, y, Constants.Projectile.WIDTH, Constants.Projectile.HEIGHT);

        this.speed = speed;
        this.damage = damage;
        this.maxLifetime = maxLifetime;
        this.lifetime = 0f;
        this.owner = owner;
        this.piercing = piercing;

        // Set velocity based on direction and speed
        if (direction != null && direction.len2() > 0) {
            direction.nor();
            velocity.setVelocityFromDirection(direction.x, direction.y, speed);
        } else {
            // Default to moving right if no direction provided
            velocity.setVelocityFromDirection(1f, 0f, speed);
        }
    }

    /**
     * Constructor - Initialize projectile with rotation angle instead of direction vector
     * @param x Initial X position
     * @param y Initial Y position
     * @param rotation Rotation angle in degrees
     * @param speed Movement speed in pixels per second
     * @param damage Damage dealt on collision
     * @param maxLifetime Maximum lifetime in seconds
     */
    public Projectile(float x, float y, float rotation, float speed, int damage, float maxLifetime) {
        this(x, y, rotation, speed, damage, maxLifetime, ProjectileOwner.PLAYER, false);
    }

    /**
     * Constructor - Initialize projectile with rotation and all properties
     * @param x Initial X position
     * @param y Initial Y position
     * @param rotation Rotation angle in degrees
     * @param speed Movement speed in pixels per second
     * @param damage Damage dealt on collision
     * @param maxLifetime Maximum lifetime in seconds
     * @param owner Owner of the projectile
     * @param piercing Whether projectile pierces through enemies
     */
    public Projectile(float x, float y, float rotation, float speed, int damage, float maxLifetime,
                     ProjectileOwner owner, boolean piercing) {
        super(new com.badlogic.gdx.graphics.g2d.TextureRegion(AssetManager.getInstance().laserPlayerTexture),
              x, y, rotation, Constants.Projectile.WIDTH, Constants.Projectile.HEIGHT);

        this.speed = speed;
        this.damage = damage;
        this.maxLifetime = maxLifetime;
        this.lifetime = 0f;
        this.owner = owner;
        this.piercing = piercing;

        // Set velocity based on rotation angle
        float directionX = MathUtils.cosDeg(rotation);
        float directionY = MathUtils.sinDeg(rotation);
        velocity.setVelocityFromDirection(directionX, directionY, speed);
    }

    @Override
    public void update(float delta) {
        // Update lifetime
        lifetime += delta;

        // Move projectile
        move(delta);

        // Check if projectile should be destroyed
        if (shouldDestroy()) {
            setActive(false);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (texture == null || !isActive()) {
            return;
        }

        // Draw projectile with rotation
        batch.draw(
            texture,
            transform.x, transform.y,
            Constants.Projectile.WIDTH / 2f, Constants.Projectile.HEIGHT / 2f, // Origin (center)
            Constants.Projectile.WIDTH, Constants.Projectile.HEIGHT,           // Size
            transform.scale, transform.scale,                                 // Scale
            transform.rotation                                                    // Rotation
        );
    }

    /**
     * Check if projectile should be destroyed
     * @return true if projectile is out of bounds or exceeded lifetime
     */
    private boolean shouldDestroy() {
        return isOutOfBounds() || lifetime >= maxLifetime;
    }

    /**
     * Check if projectile is out of world bounds
     * @return true if projectile is outside the game world
     */
    private boolean isOutOfBounds() {
        return transform.x + Constants.Projectile.WIDTH < Constants.World.MIN_X - Constants.World.OFF_SCREEN_BUFFER ||
               transform.x > Constants.World.MAX_X + Constants.World.OFF_SCREEN_BUFFER ||
               transform.y + Constants.Projectile.HEIGHT < Constants.World.MIN_Y - Constants.World.OFF_SCREEN_BUFFER ||
               transform.y > Constants.World.MAX_Y + Constants.World.OFF_SCREEN_BUFFER;
    }

    /**
     * Handle collision with another entity
     * @param other Entity that was hit
     * @return true if collision was processed, false if projectile should continue
     */
    public boolean onCollision(BaseEntity other) {
        if (!piercing) {
            // Non-piercing projectiles are destroyed on first collision
            setActive(false);
            return true;
        }

        // Piercing projectiles continue but could be modified here
        // (e.g., damage falloff, reduced speed, etc.)
        return false;
    }

    // ===== GETTERS AND SETTERS =====

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        // Update velocity magnitude while maintaining direction
        if (velocity.speed > 0) {
            velocity.setSpeed(speed);
        }
    }

    public float getLifetime() {
        return lifetime;
    }

    public float getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(float maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isPiercing() {
        return piercing;
    }

    public void setPiercing(boolean piercing) {
        this.piercing = piercing;
    }

    public ProjectileOwner getOwner() {
        return owner;
    }

    public void setOwner(ProjectileOwner owner) {
        this.owner = owner;
    }

    /**
     * Get lifetime progress as percentage (0.0f to 1.0f)
     * @return How much of the projectile's lifetime has elapsed
     */
    public float getLifetimeProgress() {
        return Math.min(1.0f, lifetime / maxLifetime);
    }

    /**
     * Check if projectile is owned by player
     * @return true if projectile was fired by player
     */
    public boolean isPlayerProjectile() {
        return owner == ProjectileOwner.PLAYER;
    }

    /**
     * Check if projectile is owned by enemy
     * @return true if projectile was fired by enemy
     */
    public boolean isEnemyProjectile() {
        return owner == ProjectileOwner.ENEMY;
    }
}
