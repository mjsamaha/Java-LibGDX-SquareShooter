package com.msamaha.squareshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.msamaha.squareshooter.SquareShooter;
import com.msamaha.squareshooter.constants.Constants;
import com.msamaha.squareshooter.game.AssetManager;

/**
 * StarterGameScreen - The main menu screen of the application. Displayed after the application is created.
 * Implements the main menu with title, Play button, and Exit button using modern LibGDX Scene2D UI.
 *
 * @author Matthew Samaha
 * @version 1.0
 * @since 2025-10-12
 */
public class StarterGameScreen implements Screen {

    /** Reference to main game class for screen management */
    private final SquareShooter game;

    /** Scene2D stage for UI management */
    private Stage stage;

    /** Main table for UI layout */
    private Table table;

    /** UI Components */
    private Label titleLabel;
    private TextButton playButton;
    private TextButton exitButton;

    /**
     * Constructor - initializes the screen with a reference to the main game class.
     * @param game The main game class for screen management
     */
    public StarterGameScreen(SquareShooter game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Create stage with FitViewport for proper scaling
        stage = new Stage(new FitViewport(Constants.Window.WIDTH, Constants.Window.HEIGHT));
        Gdx.input.setInputProcessor(stage);

        // Create main table for layout
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Initialize UI components
        createUI();
    }

    /**
     * Creates and sets up all UI components
     */
    private void createUI() {
        // Create label style
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = AssetManager.getInstance().orbitronBold;

        // Create title label (no scaling needed since we generate at exact size)
        titleLabel = new Label("Square Shooter", titleStyle);

        // Create button styles
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = AssetManager.getInstance().orbitronRegular;
        buttonStyle.fontColor = Constants.Colors.UI_TEXT;
        buttonStyle.overFontColor = Constants.Colors.HEALTH_FULL; // Green hover effect

        // Create buttons
        playButton = new TextButton("Play", buttonStyle);
        exitButton = new TextButton("Exit", buttonStyle);

        // Add button listeners
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playButtonClicked();
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitButtonClicked();
            }
        });

        // Layout UI
        table.center(); // Center everything

        // Add title with padding
        table.add(titleLabel).padBottom(50f);
        table.row();

        // Add buttons with padding
        table.add(playButton).width(200f).height(50f).padBottom(20f);
        table.row();
        table.add(exitButton).width(200f).height(50f);
    }

    /**
     * Handles the Play button click event
     */
    private void playButtonClicked() {
        // Transition to gameplay screen
        game.setScreen(new GameScreen());
        Gdx.app.log("StarterGameScreen", "Play button clicked - transitioning to GameScreen");
    }

    /**
     * Handles the Exit button click event
     */
    private void exitButtonClicked() {
        Gdx.app.exit();
    }

    @Override
    public void render(float delta) {
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Update viewport for new dimensions
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Clean up resources
        if (stage != null) {
            stage.dispose();
        }
    }
}
