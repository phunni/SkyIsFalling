package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by paul on 09/10/15.
 */
public class ExplosionAsset {

    public Animation explosionAnimation;

    public ExplosionAsset(TextureAtlas atlas) {
        explosionAnimation = new Animation(0.05f, atlas.findRegions("explosion"));
    }


}
