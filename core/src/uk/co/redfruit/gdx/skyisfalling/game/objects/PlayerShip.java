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

    public PlayerShip(World world) {
        super(world);
        playerShipRegion = Assets.getInstance().getPlayer();
        position.set(0, 5);
        defaultDynamicBodyDef.position.set(position.x, position.y);
        FixtureDef playerShipFixtureDef = new FixtureDef();
        playerShipFixtureDef.density = 1f;
        playerShipFixtureDef.friction = 0.5f;
        playerShipFixtureDef.restitution = 0.5f;
        body = world.createBody(defaultDynamicBodyDef);
        position.set(body.getPosition().x, body.getPosition().y);
        float scale = playerShipRegion.ship.getTexture().getWidth() * Constants.INV_SCALE;
        loader.attachFixture(body, "player_ship", playerShipFixtureDef, scale);
        Vector2 bodyOrigin = loader.getOrigin("player_ship", scale);
        origin.set(bodyOrigin);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(playerShipRegion.ship, position.x, position.y);
    }
}
