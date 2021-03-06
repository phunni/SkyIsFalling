package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.ControllerManager;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.listeners.GameInputListener;
import uk.co.redfruit.gdx.skyisfalling.listeners.WorldContactListener;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;
import uk.co.redfruit.gdx.skyisfalling.utils.GamePreferences;

public class GameScreen extends RedfruitScreen {

    private static final String TAG = "GameScreen";
    private static final float FIXED_TIME_STEP = 1f / 60f;
    public static OrthographicCamera camera;
    private static OrthographicCamera cameraGUI;
    public Table fpsLayer;
    double accumulator = 0.0;
    private SpriteBatch batch;
    private Viewport gameViewport;
    private Viewport guiViewport;
    private Stage stage;
    private Label livesLabel;
    private Label scoreLabel;
    private Label fpsLabel;
    private Label waveLabel;
    private Label gameOverLabel;
    private InputMultiplexer inputMultiplexer;
    private GameInputListener gameInputListener;
    private Sprite background = Assets.getInstance().getBackground();
    private Level level;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body groundBody;
    private Body leftWall;
    private Body rightWall;
    private boolean suspended = false;
    private GamePreferences preferences = GamePreferences.getInstance();

    private State state = State.RUN;

    private GooglePlayServices googlePlayServices;


    public GameScreen(Game game, GooglePlayServices googlePlayServices) {
        super((game));
        preferences.load();
        this.googlePlayServices = googlePlayServices;
    }

    //methods start
    @Override
    public void show() {

        super.show();
        if (!suspended) {
            batch = new SpriteBatch();
            camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
            gameViewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT,
                    camera);
            camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
            camera.update();
            batch.setProjectionMatrix(camera.combined);

            cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
                    Constants.VIEWPORT_GUI_HEIGHT);
            guiViewport = new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
                    Constants.VIEWPORT_GUI_HEIGHT);
            guiViewport.setCamera(cameraGUI);
            cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
            cameraGUI.setToOrtho(false);
            cameraGUI.update();

            world = new World(new Vector2(0, -9.8f), true);
            createGround();
            createSideWalls();
            level = new Level(world, googlePlayServices);
            world.setContactListener(new WorldContactListener(groundBody, leftWall, rightWall,
                    level));
            if (Constants.DEBUG) {
                debugRenderer = new Box2DDebugRenderer();
            }

            background.setPosition(0, 0);
            background.setSize(camera.viewportWidth, camera.viewportHeight);
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Background initial size: " + background.getWidth() + "  x "
                        + background.getHeight());
            }


            rebuildStage();

            //SkyIsFallingControllerListener controllerListener = SkyIsFalling.getControllerListener();
            ControllerManager.setLevel(level);

            gameInputListener = new GameInputListener(camera, level);

            inputMultiplexer = new InputMultiplexer();
            inputMultiplexer.addProcessor(stage);
            inputMultiplexer.addProcessor(gameInputListener);
            Gdx.input.setInputProcessor(inputMultiplexer);
            //Gdx.input.setCatchBackKey(true);
        } else {
            suspended = false;
            gameInputListener = new GameInputListener(camera, level);

            inputMultiplexer = new InputMultiplexer();
            inputMultiplexer.addProcessor(stage);
            inputMultiplexer.addProcessor(gameInputListener);
            Gdx.input.setInputProcessor(inputMultiplexer);
        }


    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (level.paused) {
            pauseGame();
        } else if (state == State.PAUSE && level.unpaused) {
            level.unpaused = false;
            state = State.RUN;
            rebuildStage();
        }
        batch.setProjectionMatrix(camera.combined);
        renderBackground(batch);
        switch (state) {
            case RUN:


                level.update();

                renderWorld(batch);
                renderGameHUD(batch);
                renderNewWaveAndGameOver();

                if (Constants.DEBUG) {
                    debugRenderer.render(world, camera.combined);
                }

                if (!level.gameOver && !level.showingWaveNumber && !level.playerExploding) {
                    double frameTime = Gdx.graphics.getDeltaTime();

                    accumulator += frameTime;

                    while (accumulator >= FIXED_TIME_STEP) {
                        doPhysicsWorldStep();
                        accumulator -= FIXED_TIME_STEP;
                    }
                }
                break;
            case PAUSE:
                renderGameHUD(batch);
                break;
        }
    }


    @Override
    public void resize(int width, int height) {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Resize called: " + width + " x " + height);
        }
        gameViewport.update(width, height);
        float screenAR = width / (float) height;
        float arHeight = 20 / screenAR;
        camera = new OrthographicCamera(20, arHeight);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        guiViewport.update(width, height);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        stage.getViewport().update(width, height);
        background.setSize(20, arHeight);
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Viewport resized: " + camera.viewportWidth + " x "
                    + camera.viewportHeight);
            Gdx.app.log(TAG, "Background resized: " + background.getWidth() + "  x "
                    + background.getHeight());
        }
    }

    @Override
    public void pause() {
        super.pause();
        pauseGame();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {

        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "GameScreen disposed");
        }
        super.dispose();
        world.dispose();
        batch.dispose();
        if (debugRenderer != null) {
            debugRenderer.dispose();
        }
    }

    private Table buildFPSLayer() {
        fpsLayer = new Table();
        fpsLayer.bottom().right();
        int fps = Gdx.graphics.getFramesPerSecond();
        // colour will default to red, but should change to reflect fps almost instantly
        Label.LabelStyle fpsStyle = new Label.LabelStyle(smallFont, Color.RED);
        fpsLabel = new Label("FPS: " + fps, fpsStyle);
        fpsLayer.add(fpsLabel).pad(25);

        if (!preferences.showFPS) {
            fpsLayer.setVisible(false);
        }

        return fpsLayer;
    }

    private Table buildGameOverLayer() {
        Table layer = new Table();
        layer.center();
        gameOverLabel = new Label("Game Over!", new Label.LabelStyle(largeFont, Color.WHITE));
        layer.add(gameOverLabel);
        gameOverLabel.setVisible(false);

        return layer;
    }

    private Table buildLivesLayer() {
        Table layer = new Table();
        layer.top().right();

        Image livesImage = new Image(Assets.getInstance().getPlayerLife());
        livesImage.setScaling(Scaling.fillX);

        layer.add(livesImage).pad(10).fill();

        livesLabel = new Label("" + level.getPlayerShip().lives,
                new Label.LabelStyle(smallFont, Color.WHITE));
        layer.add(livesLabel).pad(10, 5, 10, 25);
        return layer;
    }

    private Table buildPauseLayer() {
        Table layer = new Table();
        layer.top().left();
        Drawable pauseImage = new SpriteDrawable(Assets.getInstance().getPause());
        ImageButton pauseButton = new ImageButton(pauseImage);
        pauseButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseGame();
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Pause button pressed");
                }
            }
        });

        layer.add(pauseButton).pad(10);


        return layer;
    }

    private Table buildPausedLayer() {
        float pad = 5f;
        Table layer = new Table();
        layer.center();
        Label pausedLabel = new Label("Paused", new Label.LabelStyle(largeFont, Color.GREEN));
        layer.add(pausedLabel).fill().pad(pad);
        layer.row();
        TextButton continueButton = new TextButton("Continue", skinLibgdx);
        continueButton.getLabel().getStyle().font = normalFont;
        continueButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state = State.RUN;
                level.paused = false;
                rebuildStage();
                refreshInputMultiplexer();
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Game resumed after pause");
                }
            }

        });
        layer.add(continueButton).pad(pad);
        layer.row();

        TextButton options = new TextButton("Options", skinLibgdx);
        options.getLabel().getStyle().font = normalFont;
        options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                suspended = true;
                game.setScreen(new OptionsScreen(game, googlePlayServices, GameScreen.this));
            }
        });
        layer.add(options).fill().pad(pad);
        layer.row();

        TextButton quit = new TextButton("Quit", skinLibgdx);
        quit.getLabel().getStyle().font = normalFont;
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen.this.dispose();
                game.setScreen(new MenuScreen(game, googlePlayServices));
            }
        });
        layer.add(quit).fill().pad(pad);
        return layer;
    }

    private Table buildScoreLayer() {
        Table layer = new Table();
        layer.center().top();

        scoreLabel = new Label("" + level.getScore(), new Label.LabelStyle(largeFont, Color.WHITE));
        layer.add(scoreLabel).pad(10);

        return layer;
    }

    private Table buildWaveLayer() {
        Table layer = new Table();
        layer.center();
        waveLabel = new Label("Wave #" + +MathUtils.floor(level.levelNumber),
                new Label.LabelStyle(largeFont, Color.GREEN));
        waveLabel.setVisible(false);
        layer.add(waveLabel);

        return layer;
    }

    private void createGround() {
        float halfGroundWidth = Constants.WORLD_WIDTH;
        float halfGroundHeight = 0.0f;

        // Create a static body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        // Set the ground position
        groundBodyDef.position.set(halfGroundWidth * 0.5f, halfGroundHeight);

        // Create a body from the definition and add it to the world
        groundBody = world.createBody(groundBodyDef);

        // Create a rectangle shape which will fit the world_width and 1 meter high
        // (setAsBox takes half-width and half-height as arguments)
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(halfGroundWidth * 0.5f, halfGroundHeight);
        // Create a fixture from our rectangle shape and add it to our ground body
        Fixture groundFixture = groundBody.createFixture(groundBox, 0.0f);
        groundFixture.setUserData("ground");
        // Free resources
        groundBox.dispose();
    }

    private void createSideWalls() {
        float halfHeight = camera.viewportHeight;
        float halfWidth = 0.1f;

        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.StaticBody;

        wallsDef.position.set(halfWidth, halfHeight);

        leftWall = world.createBody(wallsDef);

        wallsDef.position.set(camera.viewportWidth, halfHeight);

        rightWall = world.createBody(wallsDef);
        PolygonShape wallBox = new PolygonShape();
        wallBox.setAsBox(halfWidth, halfHeight);

        Fixture leftFixture = leftWall.createFixture(wallBox, 0.0f);
        Fixture rightFixture = rightWall.createFixture(wallBox, 0.0f);

        leftFixture.setUserData("left wall");
        rightFixture.setUserData("right wall");

        wallBox.dispose();
    }

    private void doPhysicsWorldStep() {
        world.step(FIXED_TIME_STEP, 6, 2);
    }

    private void pauseGame() {
        state = State.PAUSE;
        rebuildStage();
        refreshInputMultiplexer();
    }

    private void rebuildStage() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT));

        stage.clear();
        if (Constants.DEBUG) {
            stage.setDebugAll(true);
        }

        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(stage.getWidth(), stage.getHeight());


        switch (state) {
            case RUN:
                Table livesLayer = buildLivesLayer();
                Table scoreLayer = buildScoreLayer();
                Table pauseLayer = buildPauseLayer();
                Table waveLayer = buildWaveLayer();
                Table gameOverLayer = buildGameOverLayer();


                stack.add(livesLayer);
                stack.add(scoreLayer);
                stack.add(pauseLayer);
                stack.add(waveLayer);
                stack.add(gameOverLayer);
                if (preferences.showFPS) {
                    stack.add(buildFPSLayer());
                }
                break;
            case PAUSE:
                Table pausedLayer = buildPausedLayer();
                stack.add(pausedLayer);
                stack.add(buildFPSLayer());
                break;
        }
    }

    private void refreshInputMultiplexer() {
        if (inputMultiplexer != null) {
            inputMultiplexer.clear();
            inputMultiplexer.addProcessor(stage);
            inputMultiplexer.addProcessor(gameInputListener);
        }
    }

    private void renderBackground(SpriteBatch batch) {
        batch.disableBlending();
        batch.begin();
        background.draw(batch);
        batch.end();
        batch.enableBlending();
    }

    private void renderGameHUD(SpriteBatch batch) {
        livesLabel.setText("" + level.getPlayerShip().lives);
        scoreLabel.setText("" + level.getScore());
        if (preferences.showFPS) {
            int fps = Gdx.graphics.getFramesPerSecond();
            fpsLabel.setText("FPS: " + fps);
            if (fps >= 45) {
                fpsLabel.getStyle().fontColor = Color.GREEN;
            } else if (fps >= 30) {
                fpsLabel.getStyle().fontColor = Color.YELLOW;
            } else {
                fpsLabel.getStyle().fontColor = Color.RED;
            }

        }

        if (stage != null) {
            stage.act();
            stage.draw();
        }

        batch.setProjectionMatrix(cameraGUI.combined);

        if (level.gameOver && TimeUtils.timeSinceNanos(level.gameOverStartTime) > 2000000000) {
            googlePlayServices.submitScore(level.getScore());
            game.setScreen(new MenuScreen(game, googlePlayServices));
        }
    }

    private void renderNewWaveAndGameOver() {
        if (level.showingWaveNumber) {
            waveLabel.setText("Wave #" + +MathUtils.floor(level.levelNumber));
        }
        if (waveLabel != null) {
            if (level.showingWaveNumber) {
                waveLabel.setVisible(true);
            } else {
                waveLabel.setVisible(false);
            }
        }
        if (gameOverLabel != null) {
            if (level.gameOver) {
                gameOverLabel.setVisible(true);
            } else {
                gameOverLabel.setVisible(false);
            }
        }

    }

    private void renderWorld(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        level.render(batch);
        batch.end();
        if (Constants.DEBUG) {
            debugRenderer.render(world, camera.combined);
        }
    }

    private enum State {
        PAUSE,
        RUN
    }
//methods end
}
