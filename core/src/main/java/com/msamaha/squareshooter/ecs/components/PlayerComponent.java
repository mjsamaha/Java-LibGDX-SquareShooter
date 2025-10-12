package com.msamaha.squareshooter.ecs.components;

import com.msamaha.squareshooter.constants.Constants;

/**
 * PlayerComponent - Manages player-specific state and statistics.
 * Part of the Entity Component System architecture for Square Shooter.
 *
 * <p>This component handles all player-related data including health, score,
 * and upgrade progression. It encapsulates player state management and provides
 * methods for modifying player statistics during gameplay.
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class PlayerComponent {
    /** Current health points */
    private int currentHealth;

    /** Maximum health points */
    private int maxHealth;

    /** Player score */
    private int score;

    /** Current upgrade level */
    private int upgradeLevel;

    /** Lives remaining */
    private int lives;

    /** Experience points for upgrades */
    private int experience;

    /** Flag indicating if player is invincible (temporary after taking damage) */
    private boolean invincible;

    /** Duration of invincibility frames in seconds */
    private float invincibilityTime;

    /** Timer for invincibility frames */
    private float invincibilityTimer;

    /**
     * Constructor - Initialize player with default values
     */
    public PlayerComponent() {
        this(Constants.Player.MAX_HEALTH, 0, 1, 3);
    }

    /**
     * Constructor - Initialize player with specified health and score
     * @param maxHealth Maximum health points
     * @param score Initial score
     * @param upgradeLevel Initial upgrade level
     * @param lives Initial number of lives
     */
    public PlayerComponent(int maxHealth, int score, int upgradeLevel, int lives) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.score = score;
        this.upgradeLevel = upgradeLevel;
        this.lives = lives;
        this.experience = 0;
        this.invincible = false;
        this.invincibilityTime = 2.0f; // 2 seconds of invincibility
        this.invincibilityTimer = 0f;
    }

    /**
     * Update the invincibility timer
     * @param delta Time elapsed since last frame
     */
    public void update(float delta) {
        if (invincible) {
            invincibilityTimer += delta;
            if (invincibilityTimer >= invincibilityTime) {
                invincible = false;
                invincibilityTimer = 0f;
            }
        }
    }

    /**
     * Take damage if not invincible
     * @param damage Amount of damage to take
     * @return true if damage was taken, false if blocked by invincibility
     */
    public boolean takeDamage(int damage) {
        if (invincible || currentHealth <= 0) {
            return false;
        }

        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }

        // Trigger invincibility frames
        setInvincible(true);

        return true;
    }

    /**
     * Heal the player
     * @param healAmount Amount to heal
     */
    public void heal(int healAmount) {
        currentHealth += healAmount;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    /**
     * Add points to the player's score
     * @param points Points to add
     */
    public void addScore(int points) {
        this.score += points;
        addExperience(points / 10); // Gain experience based on score
    }

    /**
     * Add experience points
     * @param exp Experience points to add
     */
    public void addExperience(int exp) {
        this.experience += exp;

        // Check for level up (every 1000 experience points)
        if (experience >= (upgradeLevel * 1000)) {
            levelUp();
        }
    }

    /**
     * Level up the player
     */
    private void levelUp() {
        upgradeLevel++;
        experience = 0; // Reset experience for next level

        // Increase max health
        maxHealth += 10;
        currentHealth = maxHealth; // Full heal on level up
    }

    /**
     * Lose a life
     * @return true if player has lives remaining, false if game over
     */
    public boolean loseLife() {
        lives--;
        if (lives > 0) {
            // Respawn with full health
            currentHealth = maxHealth;
            setInvincible(true);
            return true;
        }
        return false; // Game over
    }

    /**
     * Check if player is alive
     * @return true if current health > 0
     */
    public boolean isAlive() {
        return currentHealth > 0;
    }

    /**
     * Check if player is at full health
     * @return true if current health equals max health
     */
    public boolean isFullHealth() {
        return currentHealth == maxHealth;
    }

    /**
     * Get health percentage (0.0f to 1.0f)
     * @return Current health as percentage of max health
     */
    public float getHealthPercentage() {
        return (float) currentHealth / maxHealth;
    }

    // ===== GETTERS AND SETTERS =====

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
        if (this.currentHealth > maxHealth) {
            this.currentHealth = maxHealth;
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(int upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
        if (invincible) {
            invincibilityTimer = 0f;
        }
    }

    public float getInvincibilityTime() {
        return invincibilityTime;
    }

    public void setInvincibilityTime(float invincibilityTime) {
        this.invincibilityTime = invincibilityTime;
    }

    public float getInvincibilityTimer() {
        return invincibilityTimer;
    }
}
