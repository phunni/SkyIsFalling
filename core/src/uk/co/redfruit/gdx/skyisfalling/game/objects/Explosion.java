package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;

/**
 * Created by paul on 09/10/15.
 */
public class Explosion extends GameObject {

    private Animation animation = Assets.getInstance().getExplosion().explosionAnimation;
    private float stateTime = 0f;
    private TextureRegion currentFrame;
    private Vector2 size;

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
}
