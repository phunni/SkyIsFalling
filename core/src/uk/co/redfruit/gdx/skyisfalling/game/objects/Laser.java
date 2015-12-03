package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.assets.LaserAsset;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 02/10/15.
 */
public class Laser extends GameObject implements Pool.Poolable {

    private static final String TAG = "Laser";

    private final float LASER_WIDTH = 0.1f;

    private LaserAsset laserRegion;
    private OrthographicCamera camera;

    private Sprite sprite;

    public boolean isEnemyLaser;

    public Laser() {
        super();
    }

    public Laser(World world, OrthographicCamera camera) {
        super(world);
        this.camera = camera;
    }

    public void init(String colour, Vector2 position, Vector2 linearVelocity) {
        laserRegion = Assets.getInstance().getLasers();
        switch (colour) {
            case "green":
                sprite = laserRegion.greenLaser;
                isEnemyLaser = true;
                break;
            case "blue":
                sprite = laserRegion.blueLaser;
                break;
            case "red":
                sprite = laserRegion.redLaser;
        }
        sprite.setOriginCenter();
        this.origin = new Vector2(sprite.getOriginX(), sprite.getOriginY());
        if (isEnemyLaser) {
            sprite.rotate(180);
        }
        this.position = position;
        defaultDynamicBodyDef.position.set(position.x, position.y);
        defaultDynamicBodyDef.fixedRotation = true;
        //create shape
        PolygonShape square = new PolygonShape();
        square.setAsBox(LASER_WIDTH, LASER_WIDTH * (sprite.getHeight() / sprite.getWidth()));
        /*if (Constants.DEBUG) {
            Gdx.app.log(TAG,
                    "Laser height is: " + LASER_WIDTH * sprite.getHeight() / sprite.getWidth()
                            + ", Laser width is: " + LASER_WIDTH);
        }*/
        FixtureDef laserFixtureDef = new FixtureDef();
        laserFixtureDef.shape = square;
        laserFixtureDef.density = 1f;
        laserFixtureDef.friction = 0.0f;
        laserFixtureDef.restitution = 0.1f;
        body = world.createBody(defaultDynamicBodyDef);
        body.setGravityScale(0);
        body.createFixture(laserFixtureDef);
        body.setUserData(this);
        body.setLinearVelocity(linearVelocity);
        square.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {
        position = body.getPosition().sub(sprite.getOriginX(), sprite.getOriginY());
        sprite.setPosition(position.x, position.y);
        sprite.setBounds(position.x, position.y,
                LASER_WIDTH, LASER_WIDTH * sprite.getHeight() / sprite.getWidth());
        sprite.draw(batch);
        if (position.y > 11) {
            this.setCullable(true);
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "High laser culled");
            }
        }
    }

    @Override
    public void reset() {
        for (Fixture fixture : body.getFixtureList()) {
            body.destroyFixture(fixture);
        }
        sprite.setRotation(0);
        world.destroyBody(body);
        sprite = null;
        cullable = false;
        isEnemyLaser = false;
    }


}
