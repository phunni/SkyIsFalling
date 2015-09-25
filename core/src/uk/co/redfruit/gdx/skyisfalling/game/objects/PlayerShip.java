package uk.co.redfruit.gdx.skyisfalling.game.objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.assets.PlayerShipAsset;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;


public class PlayerShip extends GameObject {

    private PlayerShipAsset playerShipRegion;
    private static final float SHIP_WIDTH = 1;
    private static float SHIP_SPEED = 4;

    public PlayerShip(World world) {
        super(world);
        playerShipRegion = Assets.getInstance().getPlayer();
        position.set(5, 5);
        defaultDynamicBodyDef.position.set(position.x, position.y);
        defaultDynamicBodyDef.fixedRotation = true;
        FixtureDef playerShipFixtureDef = new FixtureDef();
        playerShipFixtureDef.density = 1f;
        playerShipFixtureDef.friction = 0.5f;
        playerShipFixtureDef.restitution = 0.5f;
        body = world.createBody(defaultDynamicBodyDef);
        position.set(body.getPosition().x, body.getPosition().y);
        loader.attachFixture(body, "player_ship", playerShipFixtureDef, 1);
        Vector2 bodyOrigin = loader.getOrigin("player_ship", SHIP_WIDTH).cpy();
        origin.set(bodyOrigin);
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector2 shipPosition = body.getPosition().sub(origin);
        playerShipRegion.ship.setPosition(shipPosition.x, shipPosition.y);
        playerShipRegion.ship.setBounds(shipPosition.x, shipPosition.y,
                SHIP_WIDTH, SHIP_WIDTH * playerShipRegion.ship.getHeight() / playerShipRegion.ship.getWidth());
        playerShipRegion.ship.setOrigin(origin.x, origin.y);

        playerShipRegion.ship.draw(batch);
    }
}
