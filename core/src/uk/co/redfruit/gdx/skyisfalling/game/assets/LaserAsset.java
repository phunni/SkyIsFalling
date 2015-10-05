package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by paul on 02/10/15.
 */
public class LaserAsset {

    public final Sprite blueLaser;
    public final Sprite greenLaser;
    public final Sprite redLaser;

    public LaserAsset(TextureAtlas atlas) {
        blueLaser = atlas.createSprite("laserBlue");
        greenLaser = atlas.createSprite("laserGreen");
        redLaser = atlas.createSprite("laserRed");
    }

}
