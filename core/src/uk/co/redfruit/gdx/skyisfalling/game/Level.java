package uk.co.redfruit.gdx.skyisfalling.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;

public class Level {

    private PlayerShip playerShip;

    private World world;

    public Level() {
        world = new World(new Vector2(0, -9.81f), true);
        playerShip = new PlayerShip(world);
    }

    public Level(World world) {
        this.world = world;
        playerShip = new PlayerShip(world);
    }

    public void render(SpriteBatch batch) {
        TextureRegion background = Assets.getInstance().getBackground();
        //background.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        batch.draw(background, 0, 0);
        playerShip.render(batch);
;
    }

    public void update(float deltaTime) {
       world.step(deltaTime, 6, 2);
    }

}
