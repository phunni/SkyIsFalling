package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.listeners.GameInputListener;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class GameScreen extends RedfruitScreen {

    private static final String TAG = "GameScreen";

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private Viewport gameViewport;
    private Viewport guiViewport;

    private Level level;

    private World world;
    private Box2DDebugRenderer debugRenderer;


    public GameScreen(Game game) {
        super((game));
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        gameViewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        cameraGUI =  new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        guiViewport = new ScreenViewport(cameraGUI);
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.setToOrtho(true);
        cameraGUI.update();

        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        level = new Level(world);

        createGround();
        createSideWalls();

        Gdx.input.setInputProcessor(new GameInputListener(camera, level.getPlayerShip()));
        //Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.update(deltaTime);

        renderWorld(batch);
        renderGui(batch);


        debugRenderer.render(world, camera.combined);

        world.step(1f / 60f, 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        float screenAR = width / (float) height;
        camera = new OrthographicCamera(20, 20 / screenAR);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        guiViewport.update(width, height);


        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {
        super.pause();
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
    }

    private void createGround() {
        float halfGroundWidth = Constants.WORLD_WIDTH;
        float halfGroundHeight = 0.0f;
        Body groundBody;

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
        groundBody.createFixture(groundBox, 0.0f);
        // Free resources
        groundBox.dispose();
    }

    private void createSideWalls() {
        float halfHeight = camera.viewportHeight;
        float halfWidth = 0.1f;

        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.StaticBody;

        wallsDef.position.set(halfWidth, halfHeight);

        Body leftWall = world.createBody(wallsDef);

        wallsDef.position.set(camera.viewportWidth, halfHeight);

        Body rightWall = world.createBody(wallsDef);
        PolygonShape wallBox = new PolygonShape();
        wallBox.setAsBox(halfWidth, halfHeight);

        leftWall.createFixture(wallBox, 0.0f);
        rightWall.createFixture(wallBox, 0.0f);

        wallBox.dispose();
    }

    private void renderGui(SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        if (Constants.DEBUG) {
            renderGUIFPSCounter(batch);
        }
        batch.end();
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

    private void renderGUIFPSCounter(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;

        int fps = Gdx.graphics.getFramesPerSecond();

        BitmapFont fpsFont = Assets.getInstance().getFonts().defaultNormal;

        if (fps >= 45) {
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            fpsFont.setColor(1, 0, 0, 1);
        }
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1);
    }
}
