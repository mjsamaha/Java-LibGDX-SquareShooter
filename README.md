# SquareShooter

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

---

## Square Shooter — Game Design Document

- Engine: Java + LibGDX
- Art: Aseprite (pixel art)
- Audio: OpenGameArt.org
- Design Style: Fast-paced, wave-based arcade shooter with modern effects
- Structure: OOP + ECS hybrid architecture



## Game Overview

### Concept

Square Shooter is a fast-paced 2D arcade shooter where the player controls a small square ship capable of automatic firing and mouse-based aiming.
Survive through waves of increasingly difficult enemies, unlock upgrades, and push for the highest score possible.

### Core Pillars

- Fluid Controls — Responsive WASD + mouse aiming.
- Wave Progression — Escalating challenge with 5 unique enemy types.
- Upgrades — Meaningful weapon and defensive evolution.
- Replayability — Score-chasing and randomized wave patterns.


## Core Gameplay

| Mechanic        | Description                                                   |
| --------------- | ------------------------------------------------------------- |
| **Movement**    | Controlled with WASD for precise control.                     |
| **Aiming**      | Follows mouse cursor; player rotates smoothly toward it.      |
| **Shooting**    | Automatic or left-click to fire lasers.                       |
| **Progression** | Defeat all enemies in a wave to advance.                      |
| **Scoring**     | Points awarded per enemy destroyed.                           |
| **Upgrades**    | Player selects new abilities between waves.                   |
| **Death**       | On death, player transitions to DeathScreen with final score. |


## Enemies

| Enemy Type         | Behavior                                                    |
| ------------------ | ----------------------------------------------------------- |
| **Basic Enemy**    | Standard forward movement toward player.                    |
| **Zigzag Enemy**   | Moves toward player while alternating horizontal direction. |
| **Shooter Enemy**  | Fires single projectiles aimed at player.                   |
| **Dasher Enemy**   | Periodically charges quickly toward player position.        |
| **Spread Shooter** | Fires multiple projectiles in a spread pattern.             |


## Upgrade System

Weapon Upgrades
- Double Shot — Fires two parallel lasers.
- Triple Shot — Fires three spread lasers.
- Piercing Shot — Bullets pass through multiple enemies.
- Increased Fire Rate — Reduces firing delay.
- Wider Spread — Increases angle of spread pattern.

Defensive Upgrades
- Bullet Shield — Orbiting shield that destroys incoming projectiles.
- Speed Boost — Slightly increases player movement speed.
- Health Increase — Raises maximum hit points.


## Visual Effects

| Effect           | Description                                            |
| ---------------- | ------------------------------------------------------ |
| **Laser Trails** | Small particles trailing behind fired shots.           |
| **Explosions**   | Sprite-based animation with particle burst.            |
| **Shield**       | Rotating transparent circle with glow.                 |
| **HUD Elements** | Minimal pixel-font score, wave, and enemies remaining. |
| **Menu/UI**      | Simple geometric style with subtle transitions.        |


## Audio Design

| Category             | Source                     | Example                    |
| -------------------- | -------------------------- | -------------------------- |
| **Background Music** | OpenGameArt                | Looping retro synth track. |
| **Laser Shots**      | OpenGameArt                | Short “pew” effect.        |
| **Explosions**       | OpenGameArt                | Pixel explosion sound.     |
| **Wave Complete**    | Custom chime / bell.       |                            |
| **Menu UI**          | Simple click/hover sounds. |                            |


## Controls

| Action                | Input             |
| --------------------- | ----------------- |
| **Move Up**           | W                 |
| **Move Down**         | S                 |
| **Move Left**         | A                 |
| **Move Right**        | D                 |
| **Shoot**             | Left Mouse Button |
| **Aim**               | Mouse Position    |
| **Pause**             | ESC               |
| **Upgrade Selection** | Mouse Click       |


## Project Structure

core/src/com/squareshooter
│
├── GameMain.java
├── constants/
│   └── G.java
├── screens/
│   ├── StarterGameScreen.java
│   ├── GameScreen.java
│   ├── UpgradeOverlayScreen.java
│   └── DeathScreen.java
├── ecs/
│   ├── Entity.java
│   ├── Component.java
│   ├── System.java
│   ├── components/
│   ├── systems/
│   └── managers/
├── game/
│   ├── WaveManager.java
│   ├── UpgradeManager.java
│   ├── PlayerFactory.java
│   ├── EnemyFactory.java
│   └── ProjectileFactory.java
└── ui/
    ├── HUD.java
    └── UpgradeOverlay.java


---

## Screen Designs

### StarterGameScreen

- Title: “Square Shooter”
- Buttons: Play / Exit
- Background: subtle particle or rotating square animation.
- Music: looping menu theme.
- Transitions to GameScreen on Play.

### GameScreen

Core gameplay loop.

**Handles:**
Player movement & shooting.
Enemy spawning & AI systems.
Collision detection.
Scoring & wave tracking.

On wave completion → open UpgradeOverlayScreen.
On death → open DeathScreen.


### UpgradeOverlayScreen

- Dimmed overlay above current game scene.
- Displays 2–3 upgrade cards with icons, name, and description.
- Player selects one upgrade to continue.
- Returns control to GameScreen for next wave.


## DeathScreen

Displays:
“You Died!” text.
Final score

Buttons: Play Again / Exit to Menu.
Optional fade-in animation and game-over sound.


### HUD Elements

- Score: (top-left corner, increments on kills)
- Wave Number (top-centre)
- Enemies Remaining (top-right corner)


---

## Technical Systems

| System              | Responsibility                              |
| ------------------- | ------------------------------------------- |
| **MovementSystem**  | Updates entity positions each frame.        |
| **RenderSystem**    | Draws entities using SpriteBatch.           |
| **CollisionSystem** | Detects overlaps and triggers damage/death. |
| **ShootingSystem**  | Handles bullet creation and firing rate.    |
| **WaveManager**     | Spawns enemies per wave configuration.      |
| **UpgradeManager**  | Applies and tracks player upgrades.         |



## Art Direction
Minimal pixtel art with neon highlights

| Asset Type       | Resolution       | Tool     |
| ---------------- | ---------------- | -------- |
| Player / Enemies | 32×32            | Aseprite |
| Bullets          | 8×8              | Aseprite |
| Effects          | 16×16 - 32×32    | Aseprite |
| UI Elements      | Vector/pixel mix | Aseprite |






