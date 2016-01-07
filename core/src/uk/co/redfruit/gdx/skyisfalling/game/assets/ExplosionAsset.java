package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by paul on 09/10/15.
 */
public class ExplosionAsset {

    public Animation explosionAnimation;

    public ExplosionAsset(TextureAtlas atlas) {
        explosionAnimation = new Animation(0.05f, atlas.findRegions("explosion"));
    }


}
