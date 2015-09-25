package uk.co.redfruit.gdx.skyisfalling.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class Level {

    private PlayerShip playerShip;

    private World world;

    private Sprite background = Assets.getInstance().getBackground();

    private OrthographicCamera camera;


    public Level() {
        world = new World(new Vector2(0, -9.81f), true);
        playerShip = new PlayerShip(world);
        background.setBounds(0, 0, camera.viewportWidth, camera.viewportHeight);
        background.setScale(camera.viewportWidth, camera.viewportHeight);
    }

    public Level(World world) {
        this.world = world;
        playerShip = new PlayerShip(world);
        background.setBounds(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        background.setScale(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }

    public void render(SpriteBatch batch) {
        batch.disableBlending();
        background.draw(batch);
        batch.enableBlending();
        playerShip.render(batch);
;
    }

    public void update(float deltaTime) {
       world.step(deltaTime, 6, 2);
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

}
