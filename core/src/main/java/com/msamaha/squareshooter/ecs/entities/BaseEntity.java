package com.msamaha.squareshooter.ecs.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.msamaha.squareshooter.ecs.components.TransformComponent;
import com.msamaha.squareshooter.ecs.components.VelocityComponent;

/**
 * Abstract base class for all in-game entities.
 * Provides shared data structure and behavior for updating and rendering.
 *
 * <p>This class serves as the foundation of the game's OOP entity architecture,
 * promoting consistency, reusability, and cleaner system management through polymorphism.
 *
 * <p>Example usage:
 * <pre>
 * Player player = new Player(...);
 * Projectile bullet = new Projectile(...);
 * Enemy enemy = new Enemy(...);
 *
 * entityManager.addEntity(player);
 * entityManager.addEntity(bullet);
 * entityManager.addEntity(enemy);
 *
 * for (BaseEntity e : entityManager.getEntities()) {
 *     e.update(delta);
 *     e.render(batch);
 * }
 * </pre>
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public abstract class BaseEntity {
    /** Transform component for position, rotation, and scale */
    protected TransformComponent transform;

    /** Velocity component for movement */
    protected VelocityComponent velocity;

    /** Texture for rendering the entity sprite */
    protected TextureRegion texture;

    /** Hitbox for collision detection */
    protected Rectangle hitbox;

    /** Flag indicating if entity is currently active */
    protected boolean active;

    /**
     * Constructor - Initialize entity with texture and position
     * @param texture The texture region to render
     * @param x Initial X position
     * @param y Initial Y position
     * @param width Hitbox width
     * @param height Hitbox height
     */
    public BaseEntity(TextureRegion texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.transform = new TransformComponent(x, y);
        this.velocity = new VelocityComponent();
        this.hitbox = new Rectangle(x, y, width, height);
        this.active = true;
    }

    /**
     * Constructor - Initialize entity with texture, position, and rotation
     * @param texture The texture region to render
     * @param x Initial X position
     * @param y Initial Y position
     * @param rotation Initial rotation in degrees
     * @param width Hitbox width
     * @param height Hitbox height
     */
    public BaseEntity(TextureRegion texture, float x, float y, float rotation, float width, float height) {
        this.texture = texture;
        this.transform = new TransformComponent(x, y, rotation);
        this.velocity = new VelocityComponent();
        this.hitbox = new Rectangle(x, y, width, height);
        this.active = true;
    }

    /**
     * Update entity logic each frame.
     * Each subclass must override this to implement its specific behavior.
     * @param delta Time elapsed since last frame in seconds
     */
    public abstract void update(float delta);

    /**
     * Render entity sprite using a SpriteBatch.
     * Each subclass must override this to handle its specific rendering.
     * @param batch SpriteBatch to render with
     */
    public abstract void render(SpriteBatch batch);

    /**
     * Clean up resources if necessary.
     * Subclasses can override this when they need to dispose of specific resources.
     */
    public void dispose() {
        // Default implementation - subclasses override if needed
    }

    /**
     * Update the entity's position based on its velocity
     * @param delta Time elapsed since last frame in seconds
     */
    public void move(float delta) {
        transform.x += velocity.x * delta;
        transform.y += velocity.y * delta;
        hitbox.setPosition(transform.x, transform.y);
    }

    /**
     * Check if this entity collides with another entity
     * @param other The other entity to check collision with
     * @return true if the hitboxes overlap
     */
    public boolean collidesWith(BaseEntity other) {
        return this.hitbox.overlaps(other.hitbox);
    }

    /**
     * Check if this entity is outside the world bounds
     * @param worldWidth Width of the game world
     * @param worldHeight Height of the game world
     * @return true if entity is outside world bounds
     */
    public boolean isOutOfBounds(float worldWidth, float worldHeight) {
        return hitbox.x + hitbox.width < 0 ||
               hitbox.x > worldWidth ||
               hitbox.y + hitbox.height < 0 ||
               hitbox.y > worldHeight;
    }

    // ===== GETTERS AND SETTERS =====

    /**
     * Check if entity is currently active
     * @return true if entity is active and should be updated/rendered
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the active state of the entity
     * @param active New active state
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get the entity's hitbox for collision detection
     * @return The Rectangle representing the entity's hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Get the entity's transform component
     * @return The TransformComponent containing position, rotation, and scale
     */
    public TransformComponent getTransform() {
        return transform;
    }

    /**
     * Get the entity's velocity component
     * @return The VelocityComponent containing movement data
     */
    public VelocityComponent getVelocity() {
        return velocity;
    }

    /**
     * Get the entity's texture region
     * @return The TextureRegion used for rendering
     */
    public TextureRegion getTexture() {
        return texture;
    }

    /**
     * Set the entity's texture region
     * @param texture New texture region to use for rendering
     */
    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    /**
     * Get the entity's current X position
     * @return Current X coordinate
     */
    public float getX() {
        return transform.x;
    }

    /**
     * Get the entity's current Y position
     * @return Current Y coordinate
     */
    public float getY() {
        return transform.y;
    }

    /**
     * Set the entity's position
     * @param x New X coordinate
     * @param y New Y coordinate
     */
    public void setPosition(float x, float y) {
        transform.setPosition(x, y);
        hitbox.setPosition(x, y);
    }

    /**
     * Get the entity's rotation
     * @return Current rotation in degrees
     */
    public float getRotation() {
        return transform.rotation;
    }

    /**
     * Set the entity's rotation
     * @param rotation New rotation in degrees
     */
    public void setRotation(float rotation) {
        transform.setRotation(rotation);
    }

    /**
     * Get the entity's scale
     * @return Current scale factor
     */
    public float getScale() {
        return transform.scale;
    }

    /**
     * Set the entity's scale
     * @param scale New scale factor
     */
    public void setScale(float scale) {
        transform.setScale(scale);
    }
}
