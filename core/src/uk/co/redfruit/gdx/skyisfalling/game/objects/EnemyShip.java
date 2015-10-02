package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.assets.EnemyShipAsset;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 28/09/15.
 */
public class EnemyShip extends GameObject implements Poolable {

    private EnemyShipAsset enemyShipRegion;
    private static final float SHIP_WIDTH = Constants.SHIP_WIDTH;
    private static float SHIP_SPEED = 2;

    public boolean movingLeft;
    public boolean movingRight;
    public boolean movingDown;

    public float lastDirection;

    private Sprite sprite;

    public EnemyShip(World world) {
        super(world);
    }

    public EnemyShip(){
        super();
    }


    public void init(World world, String colour, Vector2 position) {
        this.world = world;
        enemyShipRegion = Assets.getInstance().getEnemies();
        switch (colour) {
            case "green":
                sprite = enemyShipRegion.greenEnemy;
                break;
            case "blue" :
                sprite = enemyShipRegion.blueEnemy;
                break;
            case "black" :
                sprite = enemyShipRegion.blackEnemy;
                break;
            case "red" :
                sprite = enemyShipRegion.redEnemy;
                break;
        }
        this.position = position;
        defaultDynamicBodyDef.position.set(position.x, position.y);
        defaultDynamicBodyDef.awake = false;
        defaultDynamicBodyDef.fixedRotation = true;
        FixtureDef enemyShipFixtureDef = new FixtureDef();
        enemyShipFixtureDef.density = 1f;
        enemyShipFixtureDef.friction = 0.8f;
        enemyShipFixtureDef.restitution = 0.2f;
        body = world.createBody(defaultDynamicBodyDef);
        body.setGravityScale(0);
        loader.attachFixture(body, "enemy_ship", enemyShipFixtureDef, SHIP_WIDTH);
        Vector2 bodyOrigin = loader.getOrigin("enemy_ship", SHIP_WIDTH).cpy();
        origin.set(bodyOrigin);
    }


    @Override
    public void reset() {
        world.destroyBody(body);
        sprite = null;
        movingLeft = false;
        movingDown = false;
        movingRight = false;
    }


    @Override
    public void render(SpriteBatch batch) {
        Vector2 shipPosition = body.getPosition().sub(origin);
        sprite.setPosition(shipPosition.x, shipPosition.y);
        sprite.setBounds(shipPosition.x, shipPosition.y,
                SHIP_WIDTH, SHIP_WIDTH * sprite.getHeight() / sprite.getWidth());

        sprite.draw(batch);
        if (movingLeft) {
            moveLeft();
        } else if (movingRight) {
            moveRight();
        }
    }

    public boolean isAWake() {
        return body.isAwake();
    }

    public Body getBody() {
        return body;
    }

    public void moveRight() {
        Vector2 velocity = body.getLinearVelocity();
        body.setLinearVelocity(SHIP_SPEED, velocity.y);
    }

    public void moveLeft(){
        Vector2 velocity = body.getLinearVelocity();
        body.setLinearVelocity(-SHIP_SPEED, velocity.y);
    }

    public void stop() {
        body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

}
