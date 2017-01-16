package uk.co.redfruit.gdx.skyisfalling.game.objects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.assets.PlayerShipAsset;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;


public class PlayerShip extends GameObject {

    private static final String TAG = "PlayerShip";
    private static final float SHIP_WIDTH = Constants.SHIP_WIDTH;
    private static float SHIP_SPEED = 4;
    public boolean movingLeft;
    public boolean movingRight;
    public boolean destroyed;
    public long lastTimeLifeLost = TimeUtils.millis();
    public int lives;
    public PlayerShipAsset playerShipRegion;
    private Level level;

    public PlayerShip(World world, Level level) {
        super(world);
        this.level = level;
        lives = 3;
        playerShipRegion = Assets.getInstance().getPlayer();
        position.set(Constants.WORLD_WIDTH / 2, 0.1f);
        defaultDynamicBodyDef.position.set(position.x, position.y);
        defaultDynamicBodyDef.fixedRotation = true;
        FixtureDef playerShipFixtureDef = new FixtureDef();
        playerShipFixtureDef.density = 1f;
        playerShipFixtureDef.friction = 0.8f;
        playerShipFixtureDef.restitution = 0.2f;
        body = world.createBody(defaultDynamicBodyDef);
        body.setUserData(this);
        position.set(body.getPosition().x, body.getPosition().y);
        loader.attachFixture(body, "player_ship", playerShipFixtureDef, SHIP_WIDTH);
        Vector2 bodyOrigin = loader.getOrigin("player_ship", SHIP_WIDTH).cpy();
        origin.set(bodyOrigin);
    }

    @Override
    public void render(SpriteBatch batch) {
        position = body.getPosition().sub(origin);
        playerShipRegion.ship.setPosition(position.x, position.y);
        playerShipRegion.ship.setBounds(position.x, position.y,
                SHIP_WIDTH, SHIP_WIDTH * playerShipRegion.ship.getHeight() / playerShipRegion.ship.getWidth());
        playerShipRegion.ship.setOrigin(origin.x, origin.y);

        playerShipRegion.ship.draw(batch);
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(playerShipRegion.ship.getX() + playerShipRegion.ship.getWidth() / 2,
                playerShipRegion.ship.getY() + playerShipRegion.ship.getHeight() / 2);
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

    public void loseALife() {
        if (!(TimeUtils.timeSinceMillis(lastTimeLifeLost) < 250)) {
            lives--;
            lastTimeLifeLost = TimeUtils.millis();
            Sprite ship = playerShipRegion.ship;
            level.playerExploding = true;

            for (Laser laser : level.lasers) {
                laser.isCullable();
            }

            level.blowUp(position.cpy()
                    , new Vector2(playerShipRegion.ship.getWidth(), ship.getHeight()));
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    level.playerDied();
                }
            }, 1);

        }
    }

}
