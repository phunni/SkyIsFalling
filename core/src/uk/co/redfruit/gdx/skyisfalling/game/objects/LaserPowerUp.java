package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 05/01/17.
 */

public class LaserPowerUp extends PowerUp {

    public static final String TAG = "LaserPowerUp";

    public LaserPowerUp() {
        sprite = new Sprite(Assets.getInstance().getLaserPowerUp());
    }

    public void init(Vector2 position) {
        sprite.setSize(SIZE, SIZE);
        sprite.setPosition(position.x, position.y);
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Size: " + sprite.getWidth() + " x " + sprite.getHeight());
            Gdx.app.log(TAG, "Position: " + sprite.getX() + " x " + sprite.getY());
        }
    }

    @Override
    public void reset() {
        setCullable(false);
    }

}
