package com.msamaha.squareshooter.ecs.components;

/**
 * VelocityComponent - Handles movement velocity and speed for game entities.
 * Part of the Entity Component System architecture for Square Shooter.
 *
 * <p>This component manages directional movement properties, allowing entities
 * to move with consistent velocity-based motion. Supports both directional
 * vectors and speed-based movement patterns.
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class VelocityComponent {
    /** Velocity in X direction (pixels per second) */
    public float x;

    /** Velocity in Y direction (pixels per second) */
    public float y;

    /** Maximum speed limit for the entity */
    public float maxSpeed;

    /** Current speed magnitude */
    public float speed;

    /**
     * Constructor - Initialize with zero velocity
     */
    public VelocityComponent() {
        this(0f, 0f, 0f);
    }

    /**
     * Constructor - Initialize with velocity vector
     * @param x Initial X velocity
     * @param y Initial Y velocity
     */
    public VelocityComponent(float x, float y) {
        this(x, y, Float.MAX_VALUE);
    }

    /**
     * Constructor - Initialize with velocity vector and max speed
     * @param x Initial X velocity
     * @param y Initial Y velocity
     * @param maxSpeed Maximum speed limit
     */
    public VelocityComponent(float x, float y, float maxSpeed) {
        this.x = x;
        this.y = y;
        this.maxSpeed = maxSpeed;
        this.speed = (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Set velocity components
     * @param x New X velocity
     * @param y New Y velocity
     */
    public void setVelocity(float x, float y) {
        this.x = x;
        this.y = y;
        updateSpeed();
    }

    /**
     * Set velocity from direction and speed
     * @param directionX Direction vector (will be normalized)
     * @param speed Movement speed
     */
    public void setVelocityFromDirection(float directionX, float directionY, float speed) {
        // Normalize direction vector
        float length = (float) Math.sqrt(directionX * directionX + directionY * directionY);
        if (length > 0) {
            directionX /= length;
            directionY /= length;
        }

        this.x = directionX * speed;
        this.y = directionY * speed;
        this.speed = speed;
    }

    /**
     * Set speed while maintaining direction
     * @param speed New speed value
     */
    public void setSpeed(float speed) {
        this.speed = speed;
        normalizeAndScale();
    }

    /**
     * Set maximum speed limit
     * @param maxSpeed New maximum speed
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Add velocity to current velocity
     * @param x X velocity to add
     * @param y Y velocity to add
     */
    public void addVelocity(float x, float y) {
        this.x += x;
        this.y += y;
        updateSpeed();
        clampToMaxSpeed();
    }

    /**
     * Scale current velocity by a factor
     * @param factor Scale factor
     */
    public void scaleVelocity(float factor) {
        this.x *= factor;
        this.y *= factor;
        this.speed *= factor;
    }

    /**
     * Stop all movement
     */
    public void stop() {
        this.x = 0f;
        this.y = 0f;
        this.speed = 0f;
    }

    /**
     * Check if entity is moving
     * @return true if velocity magnitude is greater than threshold
     */
    public boolean isMoving() {
        return speed > 0.1f; // Small threshold to handle floating point precision
    }

    /**
     * Get normalized direction vector
     * @return Array containing normalized [x, y] direction
     */
    public float[] getDirection() {
        if (speed == 0) {
            return new float[]{0f, 0f};
        }
        return new float[]{x / speed, y / speed};
    }

    /**
     * Update speed magnitude from velocity components
     */
    private void updateSpeed() {
        this.speed = (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Normalize direction and scale to current speed
     */
    private void normalizeAndScale() {
        if (speed > 0) {
            float length = (float) Math.sqrt(x * x + y * y);
            if (length > 0) {
                this.x = (x / length) * speed;
                this.y = (y / length) * speed;
            }
        }
    }

    /**
     * Clamp velocity to maximum speed if necessary
     */
    private void clampToMaxSpeed() {
        if (maxSpeed < Float.MAX_VALUE && speed > maxSpeed) {
            scaleVelocity(maxSpeed / speed);
        }
    }
}
