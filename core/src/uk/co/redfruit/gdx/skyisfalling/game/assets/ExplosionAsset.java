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

    private Texture explosionSheet;
    private TextureRegion[] explosionFrames;
    public Animation explosionAnimation;

    public ExplosionAsset() {
        explosionSheet = new Texture(Gdx.files.internal("images/explosion.png"));
        TextureRegion[][] tmp = TextureRegion.split(explosionSheet,
                explosionSheet.getWidth()/4, explosionSheet.getHeight()/4);
        explosionFrames = new TextureRegion[4 * 4];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                explosionFrames[index++] = tmp[i][j];
            }
        }
        explosionAnimation = new Animation(0.05f, explosionFrames);
    }


}
