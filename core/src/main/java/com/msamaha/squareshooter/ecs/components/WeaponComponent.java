package com.msamaha.squareshooter.ecs.components;

import com.msamaha.squareshooter.constants.Constants;

/**
 * WeaponComponent - Manages weapon properties and shooting mechanics.
 * Part of the Entity Component System architecture for Square Shooter.
 *
 * <p>This component handles all weapon-related functionality including fire rates,
 * damage, projectile types, and cooldown management. It encapsulates shooting
 * logic and provides methods for weapon upgrades and customization.
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class WeaponComponent {
    /** Time between shots in seconds */
    private float fireRate;

    /** Cooldown timer for next shot */
    private float fireCooldown;

    /** Damage per projectile */
    private int damage;

    /** Speed of projectiles */
    private float projectileSpeed;

    /** Range of projectiles before they despawn */
    private float projectileRange;

    /** Number of projectiles fired per shot (for spread shots) */
    private int projectileCount;

    /** Angle spread between multiple projectiles in degrees */
    private float spreadAngle;

    /** Current upgrade level of the weapon */
    private int weaponLevel;

    /** Flag indicating if weapon can fire */
    private boolean canFire;

    /** Type of projectile to fire */
    private ProjectileType projectileType;

    /** Enumeration of available projectile types */
    public enum ProjectileType {
        BASIC,      // Single shot
        DOUBLE,     // Two projectiles
        TRIPLE,     // Three projectiles
        SPREAD,     // Wide spread pattern
        PIERCING    // Goes through enemies
    }

    /**
     * Constructor - Initialize weapon with default values
     */
    public WeaponComponent() {
        this(Constants.Player.SHOOT_COOLDOWN, Constants.Projectile.DAMAGE, Constants.Projectile.SPEED);
    }

    /**
     * Constructor - Initialize weapon with specified properties
     * @param fireRate Time between shots in seconds
     * @param damage Damage per projectile
     * @param projectileSpeed Speed of projectiles
     */
    public WeaponComponent(float fireRate, int damage, float projectileSpeed) {
        this.fireRate = fireRate;
        this.damage = damage;
        this.projectileSpeed = projectileSpeed;
        this.fireCooldown = 0f;
        this.projectileRange = 1000f; // Default range
        this.projectileCount = 1;
        this.spreadAngle = 0f;
        this.weaponLevel = 1;
        this.canFire = true;
        this.projectileType = ProjectileType.BASIC;
    }

    /**
     * Update the weapon cooldown timer
     * @param delta Time elapsed since last frame
     */
    public void update(float delta) {
        if (fireCooldown > 0) {
            fireCooldown -= delta;
            if (fireCooldown < 0) {
                fireCooldown = 0;
            }
        }
    }

    /**
     * Attempt to fire the weapon
     * @return true if weapon fired successfully, false if still in cooldown
     */
    public boolean tryFire() {
        if (canFire && fireCooldown <= 0) {
            fireCooldown = fireRate;
            return true;
        }
        return false;
    }

    /**
     * Check if weapon can currently fire
     * @return true if weapon is ready to fire
     */
    public boolean canFire() {
        return canFire && fireCooldown <= 0;
    }

    /**
     * Upgrade the weapon to the next level
     */
    public void upgrade() {
        weaponLevel++;

        switch (weaponLevel) {
            case 2:
                // Level 2: Double shot
                projectileType = ProjectileType.DOUBLE;
                projectileCount = 2;
                spreadAngle = 15f;
                fireRate *= Constants.Upgrade.FIRE_RATE_MULTIPLIER;
                break;

            case 3:
                // Level 3: Triple shot
                projectileType = ProjectileType.TRIPLE;
                projectileCount = 3;
                spreadAngle = 20f;
                damage += 2;
                break;

            case 4:
                // Level 4: Spread shot
                projectileType = ProjectileType.SPREAD;
                projectileCount = 5;
                spreadAngle = Constants.Upgrade.WIDER_SPREAD_ANGLE;
                projectileSpeed *= 1.2f;
                break;

            case 5:
                // Level 5: Piercing shots
                projectileType = ProjectileType.PIERCING;
                damage += 3;
                projectileRange *= 1.5f;
                break;

            default:
                // Max level reached
                break;
        }
    }

    /**
     * Get the firing angle for a specific projectile in a multi-projectile shot
     * @param projectileIndex Index of the projectile (0-based)
     * @param playerRotation Current rotation of the player
     * @return Angle in degrees for this projectile
     */
    public float getProjectileAngle(int projectileIndex, float playerRotation) {
        if (projectileCount <= 1) {
            return playerRotation;
        }

        // Calculate spread around player rotation
        float startAngle = playerRotation - (spreadAngle / 2f);
        float angleStep = spreadAngle / (projectileCount - 1);

        return startAngle + (angleStep * projectileIndex);
    }

    /**
     * Reset the weapon to its initial state
     */
    public void reset() {
        fireCooldown = 0f;
        weaponLevel = 1;
        projectileType = ProjectileType.BASIC;
        projectileCount = 1;
        spreadAngle = 0f;
        fireRate = Constants.Player.SHOOT_COOLDOWN;
        damage = Constants.Projectile.DAMAGE;
        projectileSpeed = Constants.Projectile.SPEED;
        projectileRange = 1000f;
    }

    /**
     * Force fire the weapon (ignores cooldown)
     * @return true if weapon was forced to fire
     */
    public boolean forceFire() {
        if (canFire) {
            fireCooldown = fireRate;
            return true;
        }
        return false;
    }

    // ===== GETTERS AND SETTERS =====

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public float getFireCooldown() {
        return fireCooldown;
    }

    public void setFireCooldown(float fireCooldown) {
        this.fireCooldown = fireCooldown;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public void setProjectileSpeed(float projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public float getProjectileRange() {
        return projectileRange;
    }

    public void setProjectileRange(float projectileRange) {
        this.projectileRange = projectileRange;
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    public void setProjectileCount(int projectileCount) {
        this.projectileCount = projectileCount;
    }

    public float getSpreadAngle() {
        return spreadAngle;
    }

    public void setSpreadAngle(float spreadAngle) {
        this.spreadAngle = spreadAngle;
    }

    public int getWeaponLevel() {
        return weaponLevel;
    }

    public void setWeaponLevel(int weaponLevel) {
        this.weaponLevel = weaponLevel;
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    /**
     * Get cooldown progress as percentage (0.0f to 1.0f)
     * @return How much of the cooldown has elapsed
     */
    public float getCooldownProgress() {
        if (fireRate == 0) return 1.0f;
        return Math.max(0f, 1.0f - (fireCooldown / fireRate));
    }

    /**
     * Check if weapon is currently in cooldown
     * @return true if weapon cannot fire due to cooldown
     */
    public boolean isOnCooldown() {
        return fireCooldown > 0;
    }
}
