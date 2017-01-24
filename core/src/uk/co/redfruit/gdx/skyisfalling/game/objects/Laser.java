package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.assets.LaserAsset;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 02/10/15.
 */
public class Laser extends GameObject implements Pool.Poolable {

    private static final String TAG = "Laser";

    private final float LASER_WIDTH = 0.065f;
    public boolean isEnemyLaser;
    private LaserAsset laserRegion = Assets.getInstance().getLasers();
    private OrthographicCamera camera;
    private Box2DSprite sprite;
    private String colour;

    public Laser(World world, OrthographicCamera camera) {
        super(world);
        this.camera = camera;
    }

    public void init(String colour, Vector2 position, Vector2 linearVelocity) {
        this.colour = colour;
        switch (colour) {
            case "green":
                sprite = new Box2DSprite(laserRegion.greenLaser);
                isEnemyLaser = true;
                break;
            case "blue":
                sprite = new Box2DSprite(laserRegion.blueLaser);
                break;
            case "red":
                sprite = new Box2DSprite(laserRegion.redLaser);
        }
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Sprites Initial flip: " + sprite.isFlipX() + " : " + sprite.isFlipY());
            Gdx.app.log(TAG, "Sprite colour is:" + colour + " and is enemy is: " + isEnemyLaser);
        }
        sprite.setOriginCenter();
        this.origin = new Vector2(sprite.getOriginX(), sprite.getOriginY());
        if (isEnemyLaser && !sprite.isFlipY()) {
            //sprite.rotate(180);
            sprite.flip(false, true);
        }
        this.position = position;
        defaultDynamicBodyDef.position.set(position.x, position.y);
        defaultDynamicBodyDef.fixedRotation = true;
        //sprite.setBounds(position.x, position.y, LASER_WIDTH,
        //        LASER_WIDTH * (sprite.getHeight() / sprite.getWidth()));
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

        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Body Position: " + position.x + " : " + position.y);
            Gdx.app.log(TAG, "Sprite Position: " + sprite.getX() + " : " + sprite.getY());
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        position = body.getPosition().sub(sprite.getOriginX(), sprite.getOriginY());
        sprite.draw(batch, body);
        if (position.y > 11) {
            this.setCullable(true);
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "High laser culled");
            }
        }
    }

    @Override
    public void reset() {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "reset called on laser");
        }
        for (Fixture fixture : body.getFixtureList()) {
            body.destroyFixture(fixture);
        }
        if (sprite != null) {
            sprite.flip(false, false);
        } else {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "why is sprite null?");
            }
        }
        world.destroyBody(body);
        sprite = null;
        cullable = false;
        isEnemyLaser = false;
        position = null;
        origin = null;

    }

    public String getColour() {
        return colour;
    }

    public boolean isFlipX() {
        return sprite.isFlipX();
    }

    public boolean isFlipY() {
        return sprite.isFlipY();
    }


}
