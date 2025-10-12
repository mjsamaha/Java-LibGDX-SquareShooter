package com.msamaha.squareshooter.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.msamaha.squareshooter.ecs.entities.BaseEntity;

/**
 * EntityManager - Centralized entity lifecycle management system.
 * Manages the creation, updating, rendering, and disposal of all game entities.
 *
 * <p>This class serves as the central hub for entity management, ensuring consistent
 * updates, rendering, and cleanup across different entity types (Player, Projectile, Enemy).
 * Implements safe iteration patterns and optional entity pooling for performance optimization.
 *
 * <p>Key Features:
 * <ul>
 *   <li>Safe iteration during updates (handles removals after iteration)</li>
 *   <li>Entity pooling for projectiles and enemies to reduce GC pressure</li>
 *   <li>Bulk operations for efficient entity management</li>
 *   <li>Debug utilities for monitoring entity counts</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>
 * EntityManager entityManager = new EntityManager();
 *
 * // Add entities
 * entityManager.addEntity(player);
 * entityManager.addEntity(enemy);
 * entityManager.addEntity(projectile);
 *
 * // Game loop
 * entityManager.update(delta);
 * entityManager.render(batch);
 *
 * // Cleanup
 * entityManager.dispose();
 * </pre>
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class EntityManager {

    /** Main collection of active entities */
    private final Array<BaseEntity> entities;

    /** Entities to be added at the end of the current frame */
    private final Array<BaseEntity> entitiesToAdd;

    /** Entities to be removed at the end of the current frame */
    private final Array<BaseEntity> entitiesToRemove;

    /** Pool for projectile entities to reduce garbage collection */
    private final Pool<BaseEntity> projectilePool;

    /** Pool for enemy entities to reduce garbage collection */
    private final Pool<BaseEntity> enemyPool;

    /** Flag to track if we're currently iterating (for safe modification) */
    private boolean updating;

    /**
     * Constructor - Initialize the entity management system
     */
    public EntityManager() {
        this.entities = new Array<BaseEntity>();
        this.entitiesToAdd = new Array<BaseEntity>();
        this.entitiesToRemove = new Array<BaseEntity>();
        this.updating = false;

        // Initialize pools - these will be properly typed when specific entity classes exist
        this.projectilePool = new Pool<BaseEntity>() {
            @Override
            protected BaseEntity newObject() {
                return null; // Will be overridden when Projectile class is implemented
            }
        };

        this.enemyPool = new Pool<BaseEntity>() {
            @Override
            protected BaseEntity newObject() {
                return null; // Will be overridden when Enemy class is implemented
            }
        };
    }

    /**
     * Add an entity to the manager.
     * Entity will be added at the end of the current frame if update() is running,
     * or immediately if the manager is not currently updating.
     *
     * @param entity The entity to add
     */
    public void addEntity(BaseEntity entity) {
        if (entity == null) {
            return;
        }

        if (updating) {
            // Safe to add during iteration - will be processed after current update cycle
            if (!entitiesToAdd.contains(entity, true)) {
                entitiesToAdd.add(entity);
            }
        } else {
            // Not updating, safe to add immediately
            if (!entities.contains(entity, true)) {
                entities.add(entity);
            }
        }
    }

    /**
     * Remove an entity from the manager.
     * Entity will be marked for removal and actually removed at the end of the current frame.
     *
     * @param entity The entity to remove
     */
    public void removeEntity(BaseEntity entity) {
        if (entity == null) {
            return;
        }

        if (updating) {
            // Safe to mark for removal during iteration
            if (!entitiesToRemove.contains(entity, true)) {
                entitiesToRemove.add(entity);
            }
        } else {
            // Not updating, safe to remove immediately
            entities.removeValue(entity, true);
        }
    }

    /**
     * Update all active entities.
     * Calls update() on each entity and handles pending additions/removals safely.
     *
     * @param delta Time elapsed since last frame in seconds
     */
    public void update(float delta) {
        updating = true;

        // Update all active entities
        for (BaseEntity entity : entities) {
            if (entity.isActive()) {
                entity.update(delta);
            }
        }

        updating = false;

        // Process pending additions
        if (entitiesToAdd.size > 0) {
            for (BaseEntity entity : entitiesToAdd) {
                if (!entities.contains(entity, true)) {
                    entities.add(entity);
                }
            }
            entitiesToAdd.clear();
        }

        // Process pending removals
        if (entitiesToRemove.size > 0) {
            for (BaseEntity entity : entitiesToRemove) {
                entities.removeValue(entity, true);
                entity.dispose(); // Clean up entity resources
            }
            entitiesToRemove.clear();
        }

        // Remove inactive entities
        clearInactiveEntities();
    }

    /**
     * Render all active entities.
     * Calls render() on each active entity using the provided SpriteBatch.
     *
     * @param batch SpriteBatch to render with
     */
    public void render(SpriteBatch batch) {
        if (batch == null) {
            return;
        }

        // Render all active entities
        for (BaseEntity entity : entities) {
            if (entity.isActive()) {
                entity.render(batch);
            }
        }
    }

    /**
     * Dispose of all entities and clean up resources.
     * Should be called when the game screen is disposed or when switching screens.
     */
    public void dispose() {
        // Dispose all entities
        for (BaseEntity entity : entities) {
            entity.dispose();
        }

        // Clear all collections
        entities.clear();
        entitiesToAdd.clear();
        entitiesToRemove.clear();

        // Clear pools
        projectilePool.clear();
        enemyPool.clear();
    }

    /**
     * Remove all inactive entities from the manager.
     * This is called automatically during update() but can also be called manually.
     */
    public void clearInactiveEntities() {
        // Remove entities that are marked as inactive
        for (int i = entities.size - 1; i >= 0; i--) {
            BaseEntity entity = entities.get(i);
            if (!entity.isActive()) {
                entities.removeIndex(i);
                entity.dispose();
            }
        }
    }

    /**
     * Get all active entities.
     * Returns a defensive copy to prevent external modification.
     *
     * @return Array of all active entities
     */
    public Array<BaseEntity> getEntities() {
        return new Array<BaseEntity>(entities);
    }

    /**
     * Get the count of active entities.
     *
     * @return Number of currently active entities
     */
    public int getEntityCount() {
        return entities.size;
    }

    /**
     * Get the count of entities of a specific type.
     * This is a utility method for debugging and monitoring.
     *
     * @param entityClass The class type to count
     * @return Number of entities of the specified type
     */
    public int getEntityCount(Class<?> entityClass) {
        int count = 0;
        for (BaseEntity entity : entities) {
            if (entityClass.isInstance(entity)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Check if an entity is currently managed by this EntityManager.
     *
     * @param entity The entity to check
     * @return true if the entity is in the manager
     */
    public boolean containsEntity(BaseEntity entity) {
        return entities.contains(entity, true);
    }

    /**
     * Clear all entities immediately.
     * This is a more aggressive version of clearInactiveEntities that removes everything.
     */
    public void clearAllEntities() {
        for (BaseEntity entity : entities) {
            entity.dispose();
        }
        entities.clear();
        entitiesToAdd.clear();
        entitiesToRemove.clear();
    }

    /**
     * Get entities within a specific area (for optimization).
     * This method can be useful for broad-phase collision detection.
     *
     * @param x X coordinate of the area center
     * @param y Y coordinate of the area center
     * @param radius Radius of the search area
     * @return Array of entities within the specified area
     */
    public Array<BaseEntity> getEntitiesInArea(float x, float y, float radius) {
        Array<BaseEntity> entitiesInArea = new Array<BaseEntity>();

        for (BaseEntity entity : entities) {
            if (entity.isActive()) {
                float dx = entity.getX() - x;
                float dy = entity.getY() - y;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance <= radius) {
                    entitiesInArea.add(entity);
                }
            }
        }

        return entitiesInArea;
    }

    // ===== POOLING METHODS (to be enhanced when specific entity types are implemented) =====

    /**
     * Obtain a projectile from the pool or create a new one.
     * This method will be properly implemented when the Projectile class is created.
     *
     * @return A projectile entity from the pool
     */
    public BaseEntity obtainProjectile() {
        // TODO: Implement when Projectile class is created
        return projectilePool.obtain();
    }

    /**
     * Return a projectile to the pool for reuse.
     *
     * @param projectile The projectile to return to the pool
     */
    public void freeProjectile(BaseEntity projectile) {
        projectilePool.free(projectile);
    }

    /**
     * Obtain an enemy from the pool or create a new one.
     * This method will be properly implemented when the Enemy class is created.
     *
     * @return An enemy entity from the pool
     */
    public BaseEntity obtainEnemy() {
        // TODO: Implement when Enemy class is created
        return enemyPool.obtain();
    }

    /**
     * Return an enemy to the pool for reuse.
     *
     * @param enemy The enemy to return to the pool
     */
    public void freeEnemy(BaseEntity enemy) {
        enemyPool.free(enemy);
    }
}
