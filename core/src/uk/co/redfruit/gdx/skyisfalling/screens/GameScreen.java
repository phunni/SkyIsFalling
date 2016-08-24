package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import uk.co.redfruit.gdx.skyisfalling.SkyIsFalling;
import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.ControllerManager;
import uk.co.redfruit.gdx.skyisfalling.listeners.GameInputListener;
import uk.co.redfruit.gdx.skyisfalling.listeners.WorldContactListener;
import uk.co.redfruit.gdx.skyisfalling.listeners.controllers.SkyIsFallingControllerListener;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class GameScreen extends RedfruitScreen {

    private static final String TAG = "GameScreen";
    public static OrthographicCamera camera;
    public static OrthographicCamera cameraGUI;
    private final float TIME_STEP = 1f / 60f;
    private SpriteBatch batch;
    private Viewport gameViewport;
    private Viewport guiViewport;
    private Skin skinSkyIsFalling;
    private Stage stage;
    private Label livesLabel;
    private Label scoreLabel;
    private Label fpsLabel;
    private BitmapFont normalFont = Assets.getInstance().getFonts().defaultNormal;
    private BitmapFont largeFont = Assets.getInstance().getFonts().defaultBig;
    private InputMultiplexer inputMultiplexer;
    private GameInputListener gameInputListener;
    private Sprite background = Assets.getInstance().getBackground();
    private Level level;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body groundBody;
    private Body leftWall;
    private Body rightWall;
    private float physicsStepAccumulator = 0;

    private State state = State.RUN;


    public GameScreen(Game game) {
        super((game));
    }

    public enum State {
        PAUSE,
        RUN
    }

//methods start
    @Override
    public void show() {

        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        gameViewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        guiViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        guiViewport.setCamera(cameraGUI);
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.setToOrtho(false);
        cameraGUI.update();

        world = new World(new Vector2(0, -9.8f), true);
        createGround();
        createSideWalls();
        level = new Level(world);
        world.setContactListener(new WorldContactListener(groundBody, leftWall, rightWall, level));
        if ( Constants.DEBUG ) {
            debugRenderer = new Box2DDebugRenderer();
        }

        if ( Gdx.app.getType() == Application.ApplicationType.Android) {
            background.setSize(Gdx.graphics.getWidth() * 1.5f, Gdx.graphics.getHeight() * 1.5f);
        }

        background.setPosition(0, 0);

        rebuildStage();


        SkyIsFallingControllerListener controllerListener = SkyIsFalling.getControllerListener();
        ControllerManager.setLevel(level);

        gameInputListener = new GameInputListener(camera, level);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(gameInputListener);
        Gdx.input.setInputProcessor(inputMultiplexer);
        //Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderBackground(batch);
        switch (state) {
            case RUN:


                level.update(deltaTime);

                renderWorld(batch);
                renderGameHUD(batch);

                if ( Constants.DEBUG ) {
                    debugRenderer.render(world, camera.combined);
                }

                if ( !level.gameOver && !level.showingWaveNumber ) {
                    doPhysicsWorldStep(Gdx.graphics.getDeltaTime());
                }
               break;
            case PAUSE:
                renderGameHUD(batch);
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        if ( Constants.DEBUG ) {
            Gdx.app.log(TAG, "Resize called: " + width + " x " + height);
        }
        gameViewport.update(width, height);
        float screenAR = width / (float) height;
        camera = new OrthographicCamera(20, 20 / screenAR);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        guiViewport.update(width, height);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        stage.getViewport().update(width, height);
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
        world.dispose();
        batch.dispose();
        debugRenderer.dispose();
        stage.dispose();
        skinSkyIsFalling.dispose();
    }

    private Table buildFPSLayer() {
        Table layer = new Table();
        layer.bottom().right();
        int fps = Gdx.graphics.getFramesPerSecond();
        // colour will default to red, but should change to reflect fps almost instantly
        Label.LabelStyle fpsStyle = new Label.LabelStyle(normalFont, Color.RED);
        fpsLabel = new Label("FPS: " + fps, fpsStyle);

        layer.add(fpsLabel).pad(25);
        return layer;
    }

    private Table buildLivesLayer() {
        Table layer = new Table();
        layer.top().right();

        Image livesImage = new Image(Assets.getInstance().getPlayerLife());
        livesImage.setScaling(Scaling.fit);

        layer.add(livesImage).pad(10).fill();

        livesLabel = new Label("" + level.getPlayerShip().lives, new Label.LabelStyle(normalFont, Color.WHITE));
        layer.add(livesLabel).pad(10,5,10,25);
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
        Table layer = new Table();
        layer.center();
        Label pausedLabel = new Label("Paused" , skinSkyIsFalling);
        layer.add(pausedLabel).pad(25);
        layer.row();
        TextButton continueButton = new TextButton("Continue", skinSkyIsFalling);
        continueButton.addListener(new ChangeListener() {
//methods start
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state = State.RUN;
                rebuildStage();
                refreshInputMultiplexer();
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Game resumed after pause");
                }
            }
//methods end
        });
        layer.add(continueButton).pad(25).minWidth(250);
        return layer;
    }

    private Table buildScoreLayer() {
        Table layer = new Table();
        layer.center().top();

        scoreLabel = new Label("" + level.getScore(), new Label.LabelStyle(largeFont, Color.WHITE));
        layer.add(scoreLabel).pad(10);

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

    private void doPhysicsWorldStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        physicsStepAccumulator += frameTime;
        while ( physicsStepAccumulator >= TIME_STEP ) {
            world.step(TIME_STEP, 6, 2);
            physicsStepAccumulator -= TIME_STEP;
        }
    }

    private void pauseGame() {
        state = State.PAUSE;
        rebuildStage();
        refreshInputMultiplexer();
    }

    private void rebuildStage() {

        skinSkyIsFalling = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX));
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        stage.clear();
        if ( Constants.DEBUG ) {
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



                stack.add(livesLayer);
                stack.add(scoreLayer);
                stack.add(pauseLayer);
                if ( Constants.DEBUG ) {
                    stack.add(buildFPSLayer());
                }
                break;
            case PAUSE:
                Table pausedLayer = buildPausedLayer();
                stack.add(pausedLayer);
                if ( Constants.DEBUG ) {
                    stack.add(buildFPSLayer());
                }
                break;
        }
    }

    private void refreshInputMultiplexer() {
        inputMultiplexer.clear();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(gameInputListener);
    }

    private void renderBackground(SpriteBatch batch) {
        /*if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Background Position: " + background.getX() + " ," + background.getY());
        }*/
        batch.begin();
        background.draw(batch);
        batch.end();
    }

    private void renderGameHUD(SpriteBatch batch) {
        livesLabel.setText("" + level.getPlayerShip().lives);
        scoreLabel.setText("" + level.getScore());
        if (Constants.DEBUG) {
            int fps = Gdx.graphics.getFramesPerSecond();
            fpsLabel.setText("FPS: " + fps);
            if ( fps >= 45 ) {
                fpsLabel.getStyle().fontColor = Color.GREEN;
            } else if ( fps >= 30 ) {
                fpsLabel.getStyle().fontColor = Color.YELLOW;
            } else {
                fpsLabel.getStyle().fontColor = Color.RED;
            }

        }

        if ( stage != null ) {
            stage.act();
            stage.draw();
        }

        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();

        if (level.gameOver) {
            renderGameOver(batch);
        }
        if (level.showingWaveNumber) {
            renderNewWave(batch);
        }
        batch.end();

        if (level.gameOver && TimeUtils.timeSinceNanos(level.gameOverStartTime) > 2000000000) {
            game.setScreen(new MenuScreen(game));
        }
    }

    private void renderGameOver(SpriteBatch batch) {
        BitmapFont fontGameOver = Assets.getInstance().getFonts().defaultBig;
        GlyphLayout gameOverLayout = new GlyphLayout();
        gameOverLayout.setText(fontGameOver, "GAME OVER!");

        float x = (cameraGUI.viewportWidth / 2) - (gameOverLayout.width / 2);
        float y = (cameraGUI.viewportHeight / 2) - (gameOverLayout.height / 2);

        fontGameOver.draw(batch, gameOverLayout, x, y);
    }

    private void renderNewWave(SpriteBatch batch) {
        BitmapFont fontGameOver = Assets.getInstance().getFonts().defaultBig;
        GlyphLayout gameOverLayout = new GlyphLayout();
        gameOverLayout.setText(fontGameOver, "Wave: " + MathUtils.floor(level.levelNumber));

        float x = (cameraGUI.viewportWidth / 2) - (gameOverLayout.width / 2);
        float y = (cameraGUI.viewportHeight / 2) - (gameOverLayout.height / 2);

        fontGameOver.setColor(Color.GREEN);

        fontGameOver.draw(batch, gameOverLayout, x, y);
    }

    private void renderWorld(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        level.render(batch);
        batch.end();
        if ( Constants.DEBUG ) {
            debugRenderer.render(world, camera.combined);
        }
    }
//methods end
}
