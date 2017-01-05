package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by paul on 09/10/15.
 */
public class ExplosionAsset {

    public Animation<TextureRegion> explosionAnimation;

    public ExplosionAsset(TextureAtlas atlas) {
        explosionAnimation = new Animation<TextureRegion>(0.05f, atlas.findRegions("explosion"));
    }


}
