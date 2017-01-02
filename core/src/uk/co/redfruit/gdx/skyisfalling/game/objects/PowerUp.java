package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by paul on 02/01/17.
 */

public abstract class PowerUp implements Pool.Poolable {

    public Sprite sprite;

    public void render(SpriteBatch batch) {
        if (sprite.getY() > 0) {
            sprite.setY(sprite.getY() - 0.02f);
        }

        sprite.draw(batch);

    }
}
