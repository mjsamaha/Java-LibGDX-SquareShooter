package com.msamaha.squareshooter.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.msamaha.squareshooter.constants.Constants;
import com.msamaha.squareshooter.ecs.components.WeaponComponent;
import com.msamaha.squareshooter.entities.projectile.Projectile;

/**
 * ProjectileFactory - Centralized factory for creating Projectile objects.
 * Ensures consistent projectile creation with predefined parameters based on weapon types.
 *
 * <p>This factory handles:
 * <ul>
 *   <li>Projectile creation with proper texture, speed, and damage</li>
 *   <li>WeaponComponent integration for configuration</li>
 *   <li>Multi-projectile spawning for spread shots</li>
 *   <li>AssetManager integration for texture loading</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class ProjectileFactory {

    /** Reference to the asset manager for loading textures */
    private final AssetManager assetManager;

    /**
     * Constructor - Initialize factory with asset manager
     * @param assetManager The game's asset manager
     */
    public ProjectileFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Create a single projectile
     * @param x Spawn X position
     * @param y Spawn Y position
     * @param rotation Firing rotation in degrees
     * @param weapon Weapon component containing projectile configuration
     * @return New projectile instance
     */
    public Projectile createProjectile(float x, float y, float rotation, WeaponComponent weapon) {
        return new Projectile(
            x, y, rotation,
            weapon.getProjectileSpeed(),
            weapon.getDamage(),
            weapon.getProjectileRange() / weapon.getProjectileSpeed(),
            Projectile.ProjectileOwner.PLAYER,
            weapon.getProjectileType() == WeaponComponent.ProjectileType.PIERCING
        );
    }

    /**
     * Create multiple projectiles for spread shots
     * @param x Spawn X position
     * @param y Spawn Y position
     * @param baseRotation Base firing rotation in degrees
     * @param weapon Weapon component containing projectile configuration
     * @return Array of projectile instances
     */
    public Projectile[] createProjectileSpread(float x, float y, float baseRotation, WeaponComponent weapon) {
        int projectileCount = weapon.getProjectileCount();
        Projectile[] projectiles = new Projectile[projectileCount];

        if (projectileCount <= 1) {
            // Single projectile
            projectiles[0] = createProjectile(x, y, baseRotation, weapon);
        } else {
            // Multiple projectiles with spread
            float spreadAngle = weapon.getSpreadAngle();
            float startAngle = baseRotation - (spreadAngle / 2f);
            float angleStep = spreadAngle / (projectileCount - 1);

            for (int i = 0; i < projectileCount; i++) {
                float projectileAngle = startAngle + (angleStep * i);
                projectiles[i] = createProjectile(x, y, projectileAngle, weapon);
            }
        }

        return projectiles;
    }

    /**
     * Create a projectile from position and direction vector
     * @param position Spawn position
     * @param direction Direction vector (will be normalized)
     * @param weapon Weapon component containing projectile configuration
     * @return New projectile instance
     */
    public Projectile createProjectile(Vector2 position, Vector2 direction, WeaponComponent weapon) {
        // Calculate rotation from direction vector
        float rotation = MathUtils.atan2(direction.y, direction.x) * MathUtils.radiansToDegrees;

        return new Projectile(
            position.x, position.y, direction,
            weapon.getProjectileSpeed(),
            weapon.getDamage(),
            weapon.getProjectileRange() / weapon.getProjectileSpeed(),
            Projectile.ProjectileOwner.PLAYER,
            weapon.getProjectileType() == WeaponComponent.ProjectileType.PIERCING
        );
    }

    /**
     * Create an enemy projectile
     * @param x Spawn X position
     * @param y Spawn Y position
     * @param rotation Firing rotation in degrees
     * @param speed Movement speed
     * @param damage Damage dealt
     * @param range Maximum range before expiration
     * @return New enemy projectile instance
     */
    public Projectile createEnemyProjectile(float x, float y, float rotation, float speed, int damage, float range) {
        return new Projectile(
            x, y, rotation, speed, damage, range / speed,
            Projectile.ProjectileOwner.ENEMY, false
        );
    }

    /**
     * Create a piercing projectile (goes through enemies)
     * @param x Spawn X position
     * @param y Spawn Y position
     * @param rotation Firing rotation in degrees
     * @param weapon Weapon component containing projectile configuration
     * @return New piercing projectile instance
     */
    public Projectile createPiercingProjectile(float x, float y, float rotation, WeaponComponent weapon) {
        return new Projectile(
            x, y, rotation,
            weapon.getProjectileSpeed(),
            weapon.getDamage(),
            weapon.getProjectileRange() / weapon.getProjectileSpeed(),
            Projectile.ProjectileOwner.PLAYER,
            true // Piercing enabled
        );
    }

    /**
     * Calculate projectile spawn position offset from player center
     * @param playerX Player X position
     * @param playerY Player Y position
     * @param playerRotation Player rotation in degrees
     * @param offsetDistance Distance to offset from player center
     * @return Spawn position for projectile
     */
    public Vector2 calculateSpawnPosition(float playerX, float playerY, float playerRotation, float offsetDistance) {
        float centerX = playerX + Constants.Player.WIDTH / 2f;
        float centerY = playerY + Constants.Player.HEIGHT / 2f;

        // Offset spawn position forward from player center
        float spawnX = centerX + MathUtils.cosDeg(playerRotation) * offsetDistance;
        float spawnY = centerY + MathUtils.sinDeg(playerRotation) * offsetDistance;

        return new Vector2(spawnX, spawnY);
    }

    /**
     * Get projectile configuration for a specific weapon type
     * @param projectileType Type of projectile
     * @return Array containing [speed, damage, lifetime]
     */
    public float[] getProjectileConfig(WeaponComponent.ProjectileType projectileType) {
        switch (projectileType) {
            case BASIC:
                return new float[] {
                    Constants.Projectile.SPEED,
                    Constants.Projectile.DAMAGE,
                    2.0f // 2 second lifetime
                };

            case DOUBLE:
                return new float[] {
                    Constants.Projectile.SPEED * 0.9f,
                    Constants.Projectile.DAMAGE,
                    2.2f
                };

            case TRIPLE:
                return new float[] {
                    Constants.Projectile.SPEED * 0.85f,
                    Constants.Projectile.DAMAGE + 2,
                    2.5f
                };

            case SPREAD:
                return new float[] {
                    Constants.Projectile.SPEED * 1.2f,
                    Constants.Projectile.DAMAGE - 2,
                    1.8f
                };

            case PIERCING:
                return new float[] {
                    Constants.Projectile.SPEED * 0.8f,
                    Constants.Projectile.DAMAGE + 3,
                    3.0f // Longer lifetime for piercing shots
                };

            default:
                return new float[] {
                    Constants.Projectile.SPEED,
                    Constants.Projectile.DAMAGE,
                    2.0f
                };
        }
    }
}
