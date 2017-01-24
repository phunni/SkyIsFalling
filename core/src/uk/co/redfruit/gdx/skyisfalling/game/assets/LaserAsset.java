package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by paul on 02/10/15.
 */
public class LaserAsset {

    public final TextureRegion blueLaser;
    public final TextureRegion greenLaser;
    public final TextureRegion redLaser;

    public LaserAsset(TextureAtlas atlas) {
        blueLaser = atlas.findRegion("laserBlue");
        greenLaser = atlas.findRegion("laserGreen");
        redLaser = atlas.findRegion("laserRed");
    }

}
