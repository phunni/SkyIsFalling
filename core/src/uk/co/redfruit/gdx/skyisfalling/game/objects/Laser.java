package uk.co.redfruit.gdx.skyisfalling.game.objects;

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

/**
 * Created by paul on 02/10/15.
 */
public class Laser extends GameObject implements Pool.Poolable {

    private final float LASER_WIDTH = 0.1f;

    private LaserAsset laserRegion;

    private Sprite sprite;

    public Laser() {
        super();
    }

    public Laser(World world) {
        super(world);
    }

    public void init(String colour, Vector2 position) {
        laserRegion = Assets.getInstance().getLasers();
        switch (colour) {
            case "green":
                sprite = laserRegion.greenLaser;
                break;
            case "blue":
                sprite = laserRegion.blueLaser;
                break;
            case "red":
                sprite = laserRegion.redLaser;
        }
        sprite.setOriginCenter();
        this.position = position;
        defaultDynamicBodyDef.position.set(position.x, position.y);
        defaultDynamicBodyDef.fixedRotation = true;
        //create shape
        PolygonShape square = new PolygonShape();
        square.setAsBox(LASER_WIDTH, LASER_WIDTH * sprite.getHeight() / sprite.getWidth());
        FixtureDef laserFixtureDef = new FixtureDef();
        laserFixtureDef.shape = square;
        laserFixtureDef.density = 1f;
        laserFixtureDef.friction = 0.0f;
        laserFixtureDef.restitution = 0.1f;
        body = world.createBody(defaultDynamicBodyDef);
        body.setGravityScale(-1f);
        body.createFixture(laserFixtureDef);
        body.setUserData(this);
        square.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector2 laserPosition = body.getPosition().sub(sprite.getOriginX(), sprite.getOriginY());
        sprite.setPosition(laserPosition.x, laserPosition.y);
        sprite.setBounds(laserPosition.x, laserPosition.y,
                LASER_WIDTH, LASER_WIDTH * sprite.getHeight() / sprite.getWidth());
        sprite.draw(batch);
    }

    @Override
    public void reset() {
        for (Fixture fixture : body.getFixtureList()) {
            body.destroyFixture(fixture);
        }
        world.destroyBody(body);
        sprite = null;
        cullable = false;
    }
}
