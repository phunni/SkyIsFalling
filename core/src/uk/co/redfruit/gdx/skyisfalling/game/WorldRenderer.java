package uk.co.redfruit.gdx.skyisfalling.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.*;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class WorldRenderer implements Disposable {

    private OrthographicCamera camera;
    private OrthographicCamera guiCamera;
    private Viewport gameViewPort;
    private Viewport guiViewport;
    private SpriteBatch batch;
    private WorldController worldController;

    private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
    private Box2DDebugRenderer debugRenderer;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }


    @Override
    public void dispose() {

    }

    public void render() {
        renderWorld(batch);
        renderGui(batch);
        //
        // ScreenshotFactory.saveScreenshot();
    }



    public void resize(int width, int height) {
        //camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
    }

    private void init() {
        camera = new OrthographicCamera();
        gameViewPort = new FillViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
        camera.position.set(0, 0, 0);
        camera.setToOrtho(false);
        camera.update();

        guiCamera = new OrthographicCamera();
        guiViewport = new ScreenViewport(guiCamera);
        guiCamera.position.set(0, 0, 0);
        guiCamera.setToOrtho(false);
        guiCamera.update();

        batch = new SpriteBatch();

        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawAABBs(true);
    }

    private void renderGui(SpriteBatch batch) {
    }

    private void renderWorld(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.getLevel().render(batch);
        batch.end();
        if (DEBUG_DRAW_BOX2D_WORLD) {
            debugRenderer.render(worldController.getWorld(), camera.combined);
        }
    }
}
