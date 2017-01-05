package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by paul on 02/01/17.
 */

public abstract class PowerUp implements Pool.Poolable {

    protected static final float SIZE = 0.5f;

    public Sprite sprite;
    private boolean cullable;
    private byte frames;

    public void render(SpriteBatch batch) {
        if (sprite.getY() > 0) {
            sprite.setY(sprite.getY() - 0.05f);
        }
        if ((sprite.getY() <= 0)) {
            frames++;
        }
        if (frames >= 1) {
            if (frames % 10 == 0) {
                if (frames % 20 == 0) {
                    sprite.setAlpha(1);
                } else {
                    sprite.setAlpha(0);
                }
            }
            if (frames % 70 == 0) {
                sprite.setAlpha(1);
                frames = 0;
                cullable = true;
            }
        }

        sprite.draw(batch);

    }

    public boolean isCullable() {
        return cullable;
    }

    public void setCullable(boolean cullable) {
        this.cullable = cullable;
    }
}
