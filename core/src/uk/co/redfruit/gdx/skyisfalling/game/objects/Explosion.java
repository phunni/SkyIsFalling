package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 09/10/15.
 */
public class Explosion extends GameObject implements Pool.Poolable {

    private static final String TAG = "Explosion";

    private Animation<TextureRegion> animation =
            Assets.getInstance().getExplosion().explosionAnimation;
    private float stateTime = 0f;
    private TextureRegion currentFrame;
    private Vector2 size;

    public Explosion() {}

    public Explosion (Vector2 position, Vector2 size) {
        this.position = position;
        this.size = size;
    }

    @Override
    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, position.x, position.y, size.x, size.y);
    }

    @Override
    public void reset() {
        stateTime = 0f;
        currentFrame = null;
        size = null;
        position = null;
        origin = new Vector2();
        defaultDynamicBodyDef = null;
        cullable = false;
    }

    public void init(Vector2 position, Vector2 size) {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Explosion length: " + animation.getAnimationDuration());
        }
        this.position = position;
        this.size = size;
        defaultDynamicBodyDef = null;
    }
}
