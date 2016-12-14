package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.TimeUtils;

import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.assets.EnemyShipAsset;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 28/09/15.
 */
public class EnemyShip extends GameObject implements Poolable {

    private static final String TAG = "EnemyShip";
    private static final float SHIP_WIDTH = Constants.SHIP_WIDTH;
    private static float SHIP_SPEED = 3f;
    public boolean movingLeft;
    public boolean movingRight;
    public boolean movingDown;
    public float lastDirection;
    private Sprite sprite;
    private Level level;

    private float hitPoints;
    private boolean destroyed;

    private boolean hit;
    private long hitFlash;

    private boolean isFalling;

    public EnemyShip(World world) {
        super(world);
    }


    public void init(World world, Level level, String colour, Vector2 position) {
        this.level = level;
        EnemyShipAsset enemyShipRegion = Assets.getInstance().getEnemies();
        switch (colour) {
            case "green":
                sprite = enemyShipRegion.greenEnemy;
                hitPoints = 5;
                break;
            case "blue" :
                sprite = enemyShipRegion.blueEnemy;
                hitPoints = 3;
                break;
            case "black" :
                sprite = enemyShipRegion.blackEnemy;
                hitPoints = 2;
                break;
            case "red" :
                sprite = enemyShipRegion.redEnemy;
                hitPoints = 6;
                break;
        }
        this.position = position;
        defaultDynamicBodyDef.position.set(position.x, position.y);
        defaultDynamicBodyDef.fixedRotation = true;
        FixtureDef enemyShipFixtureDef = new FixtureDef();
        enemyShipFixtureDef.density = 1f;
        enemyShipFixtureDef.friction = 0.8f;
        enemyShipFixtureDef.restitution = 0.2f;
        body = world.createBody(defaultDynamicBodyDef);
        body.setGravityScale(0);
        loader.attachFixture(body, "enemy_ship", enemyShipFixtureDef, SHIP_WIDTH);
        body.setUserData(this);
        Vector2 bodyOrigin = loader.getOrigin("enemy_ship", SHIP_WIDTH).cpy();
        origin.set(bodyOrigin);
    }


    @Override
    public void reset() {
        world.destroyBody(body);
        sprite = null;
        level = null;
        movingLeft = false;
        movingDown = false;
        movingRight = false;
        destroyed = false;
        hitPoints = 0;
        isFalling = false;
        lastDirection = 0;
    }


    @Override
    public void render(SpriteBatch batch) {
        Color original  = sprite.getColor();
        position = body.getPosition().sub(origin);
        sprite.setPosition(position.x, position.y);
        sprite.setBounds(position.x, position.y,
                SHIP_WIDTH, SHIP_WIDTH * sprite.getHeight() / sprite.getWidth());

        if (hit && TimeUtils.timeSinceNanos(hitFlash) < 250000000) {
            sprite.setColor(Color.RED);
        }  else if (TimeUtils.timeSinceNanos(hitFlash) > 250000000) {
            hit = false;
        }

        sprite.draw(batch);
        if (movingLeft) {
            moveLeft();
        } else if (movingRight) {
            moveRight();
        } else if (movingDown) {
            moveDown();
        }

        sprite.setColor(original);
    }


    public Body getBody() {
        return body;
    }

    private void moveRight() {
        Vector2 velocity = body.getLinearVelocity();
        body.setLinearVelocity(SHIP_SPEED, velocity.y);
    }

    private void moveLeft() {
        Vector2 velocity = body.getLinearVelocity();
        body.setLinearVelocity(-SHIP_SPEED, velocity.y);
    }

    private void moveDown() {
        body.setLinearVelocity(0, -0.2f);
    }

    public void stop() {
        body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    public void fullStop() {
        body.setLinearVelocity(0, 0);
    }

    public void reduceHitPoints() {
        hitPoints--;
        hit = true;
        hitFlash = TimeUtils.nanoTime();
        if(Constants.DEBUG) {
            Gdx.app.log(TAG, "hit points: " + hitPoints);
        }
        if (hitPoints == 0) {
            level.increaseScore(50);
            body.setGravityScale(1);
            isFalling = true;
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
        level.blowUp(position.cpy(), new Vector2(sprite.getWidth(), sprite.getHeight()));
    }

    public boolean isFalling() {
        return isFalling;
    }

    public Vector2 getCentre() {
        return new Vector2(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
    }

}
