package com.msamaha.squareshooter.constants;

import com.badlogic.gdx.graphics.Color;

/**
 * Global configuration constants for the Square Shooter arcade game.
 *
 * <p>Organized into nested classes by category for better maintainability:
 * <ul>
 *   <li>{@link Window} - Display and window settings</li>
 *   <li>{@link World} - Game world boundaries</li>
 *   <li>{@link Player} - Player ship configuration</li>
 *   <li>{@link Enemy} - Enemy ship configuration</li>
 *   <li>{@link Projectile} - Laser and projectile settings</li>
 *   <li>{@link Gameplay} - Scoring and mechanics</li>
 *   <li>{@link Upgrade} - Upgrade options configuration</li>
 *   <li>{@link Fonts} - Font configuration and sizes</li>
 *   <li>{@link Colors} - Color palette for entities and UI</li>
 *   <li>{@link Debug} - Debug visualization flags</li>
 * </ul>
 *
 * <p>Usage: {@code Constants.Player.SPEED}
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 * @see <a href="https://github.com/msamaha/libgdx-arcade-game-template">LibGDX Arcade Game Template</a>
 */

public final class Constants {

    /** Window and display configuration */
    public static final class Window {
        public static final int WIDTH = 960;
        public static final int HEIGHT = 960;
        public static final String TITLE = "Square Shooter";
        public static final int TARGET_FPS = 60;

        private Window() {}
    }

    /** Game world boundaries */
    public static final class World {
        public static final float MIN_X = 0f;
        public static final float MAX_X = Window.WIDTH;
        public static final float MIN_Y = 0f;
        public static final float MAX_Y = Window.HEIGHT;
        public static final float OFF_SCREEN_BUFFER = 50f;

        private World() {}
    }

    /** Player configuration */
    public static final class Player {
        public static final float WIDTH = 64f;
        public static final float HEIGHT = 64f;
        public static final float SPEED = 300f;
        public static final float SHOOT_COOLDOWN = 0.25f;
        public static final int MAX_HEALTH = 100;
        public static final float START_X = (Window.WIDTH - WIDTH) / 2f;
        public static final float START_Y = 50f;

        private Player() {}
    }

    /** Enemy configuration */
    public static final class Enemy {
        public static final float WIDTH = 32f;
        public static final float HEIGHT = 32f;
        public static final float SPEED_BASIC = 150f;
        public static final float SPEED_ZIGZAG = 120f;
        public static final float SPEED_SHOOTER = 100f;
        public static final float SPEED_DASHER = 250f;
        public static final int MAX_HEALTH_BASIC = 30;
        public static final int MAX_HEALTH_ZIGZAG = 25;
        public static final int MAX_HEALTH_SHOOTER = 20;
        public static final int MAX_HEALTH_DASHER = 50;
        public static final float SPAWN_INTERVAL = 2f;
        public static final int MAX_COUNT = 15;
        public static final float SPAWN_Y = Window.HEIGHT;
        public static final float SPAWN_PADDING = 20f;

        private Enemy() {}
    }

    /** Projectile configuration */
    public static final class Projectile {
        public static final float WIDTH = 16f;
        public static final float HEIGHT = 16f;
        public static final float SPEED = 400f;
        public static final int DAMAGE = 10;
        public static final int POOL_SIZE = 50;

        private Projectile() {}
    }

    /** Gameplay mechanics and scoring */
    public static final class Gameplay {
        public static final int STARTING_SCORE = 0;
        public static final int POINTS_PER_ENEMY = 100;

        private Gameplay() {}
    }

    /** Font configuration and sizes - uses FreeType for TTF rendering */
    public static final class Fonts {
        // Font sizes for different UI elements
        public static final int UI_SIZE = 24;
        public static final int HUD_SIZE = 16;
        public static final int TITLE_SIZE = 48;
        public static final int SCORE_SIZE = 32;

        // FreeType generation parameters
        public static final int BORDER_WIDTH = 1;
        public static final float BORDER_WIDTH_FLOAT = 0.5f;
        public static final float GAMMA = 1.8f; // Smoothing parameter
        public static final boolean FLIP = false; // Don't flip textures

        // Minecraft font specific parameters
        public static final int SHADOW_OFFSET_X = 1;
        public static final int SHADOW_OFFSET_Y = 1;
        public static final float SHADOW_OPACITY = 0.5f;

        private Fonts() {}
    }

    /** Upgrade system configuration */
    public static final class Upgrade {
        // Weapon upgrades
        public static final float DOUBLE_SHOT_ANGLE = 10f;
        public static final float TRIPLE_SHOT_ANGLE = 20f;
        public static final float FIRE_RATE_MULTIPLIER = 0.75f; // reduces cooldown
        public static final float WIDER_SPREAD_ANGLE = 45f;
        public static final boolean PIERCING_ENABLED = true;

        // Defensive upgrades
        public static final float SHIELD_RADIUS = 40f;
        public static final float SHIELD_SPEED = 100f;

        private Upgrade() {}
    }

    /** Color palette */
    public static final class Colors {
        public static final Color BACKGROUND = Color.BLACK;
        public static final Color PLAYER = Color.CYAN;
        public static final Color ENEMY_BASIC = Color.RED;
        public static final Color ENEMY_ZIGZAG = Color.ORANGE;
        public static final Color ENEMY_SHOOTER = Color.MAGENTA;
        public static final Color ENEMY_DASHER = Color.YELLOW;
        public static final Color LASER_PLAYER = Color.GREEN;
        public static final Color LASER_ENEMY = Color.PURPLE;
        public static final Color UI_TEXT = Color.WHITE;
        public static final Color HEALTH_FULL = Color.GREEN;
        public static final Color HEALTH_MEDIUM = Color.YELLOW;
        public static final Color HEALTH_LOW = Color.RED;
        public static final Color DEBUG_COLLISION = new Color(1, 1, 1, 0.5f);

        private Colors() {}
    }

    /** Debug flags */
    public static final class Debug {
        public static final boolean ENABLED = true;
        public static final boolean SHOW_FPS = true;
        public static final boolean SHOW_COLLISION_BOXES = true;
        public static final boolean LOG_ENTITY_SPAWNS = true;
        public static final boolean LOG_COLLISIONS = false;
        public static final boolean SHOW_ENTITY_COUNT = true;

        private Debug() {}
    }

    // Prevent instantiation
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
}
