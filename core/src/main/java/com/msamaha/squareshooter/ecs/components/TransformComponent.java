package com.msamaha.squareshooter.ecs.components;

/**
 * TransformComponent - Handles position, rotation, and scale for game entities.
 * Part of the Entity Component System architecture for Square Shooter.
 *
 * <p>This component stores and manages the spatial properties of entities,
 * allowing for consistent transformation handling across all game objects.
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class TransformComponent {
    /** X position in world coordinates */
    public float x;

    /** Y position in world coordinates */
    public float y;

    /** Rotation angle in degrees */
    public float rotation;

    /** Scale factor (1.0f = normal size) */
    public float scale;

    /**
     * Constructor - Initialize transform with position
     * @param x Initial X position
     * @param y Initial Y position
     */
    public TransformComponent(float x, float y) {
        this(x, y, 0f, 1f);
    }

    /**
     * Constructor - Initialize transform with position and rotation
     * @param x Initial X position
     * @param y Initial Y position
     * @param rotation Initial rotation in degrees
     */
    public TransformComponent(float x, float y, float rotation) {
        this(x, y, rotation, 1f);
    }

    /**
     * Constructor - Initialize transform with all properties
     * @param x Initial X position
     * @param y Initial Y position
     * @param rotation Initial rotation in degrees
     * @param scale Initial scale factor
     */
    public TransformComponent(float x, float y, float rotation, float scale) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * Set the position of the transform
     * @param x New X position
     * @param y New Y position
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Translate the transform by the given offset
     * @param x Offset in X direction
     * @param y Offset in Y direction
     */
    public void translate(float x, float y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Get the position as a float array
     * @return Array containing [x, y]
     */
    public float[] getPosition() {
        return new float[]{x, y};
    }

    /**
     * Set rotation angle
     * @param rotation New rotation in degrees
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Rotate by the given angle
     * @param angle Angle to rotate by in degrees
     */
    public void rotate(float angle) {
        this.rotation += angle;
    }

    /**
     * Set scale factor
     * @param scale New scale factor
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Scale by the given factor
     * @param factor Factor to scale by
     */
    public void scale(float factor) {
        this.scale *= factor;
    }
}
