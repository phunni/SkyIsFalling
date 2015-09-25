package uk.co.redfruit.gdx.skyisfalling.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.*;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class WorldRenderer implements Disposable {

    private OrthographicCamera camera;
    private OrthographicCamera guiCamera;
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
    }



    public void resize(int width, int height) {
        float screenAR = width / (float) height;
        camera = new OrthographicCamera(20, 20 / screenAR);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        camera.update();
    }

    private void init() {
        resize((int)Constants.WORLD_WIDTH, (int)Constants.WORLD_HEIGHT);
        worldController.getLevel().setCamera(camera);

        guiCamera = new OrthographicCamera();
        guiCamera.position.set(0, 0, 0);
        guiCamera.setToOrtho(false);
        guiCamera.update();



        debugRenderer = new Box2DDebugRenderer();
        //debugRenderer.setDrawAABBs(true);
    }

    private void renderGui(SpriteBatch batch) {
    }

    private void renderWorld(SpriteBatch batch) {
        batch.begin();
        worldController.getLevel().render(batch);
        batch.end();
        if (DEBUG_DRAW_BOX2D_WORLD) {
            debugRenderer.render(worldController.getWorld(), camera.combined);
        }
    }
}
