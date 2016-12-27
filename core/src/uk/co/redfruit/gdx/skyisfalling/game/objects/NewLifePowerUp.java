package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;

/**
 * Created by paul on 22/12/16.
 */

public class NewLifePowerUp implements Pool.Poolable {

    private static final String TAG = "NewLifePowerUp";
    private static final float SIZE = 0.5f;
    public Sprite sprite;

    public NewLifePowerUp() {
    }

    public void init(Vector2 position) {
        sprite = Assets.getInstance().getPlayerLife();
        sprite.setSize(SIZE, SIZE);
        sprite.setPosition(position.x, position.y);
        Gdx.app.log(TAG, "Size: " + sprite.getWidth() + " x " + sprite.getHeight());
        Gdx.app.log(TAG, "Position: " + sprite.getX() + " x " + sprite.getY());
    }

    public void render(SpriteBatch batch) {
        if (sprite.getY() > 0) {
            sprite.setY(sprite.getY() - 0.02f);
        }

        sprite.draw(batch);

    }

    @Override
    public void reset() {
        sprite = null;
    }

}
