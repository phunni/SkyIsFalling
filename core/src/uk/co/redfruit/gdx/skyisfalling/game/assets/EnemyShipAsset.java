package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by paul on 28/09/15.
 */
public class EnemyShipAsset {

    public final Sprite redEnemy;
    public final Sprite blueEnemy;
    public final Sprite blackEnemy;
    public final Sprite greenEnemy;

    public EnemyShipAsset(TextureAtlas atlas) {
        redEnemy = atlas.createSprite("enemyRed");
        blueEnemy = atlas.createSprite("enemyBlue");
        blackEnemy = atlas.createSprite("enemyBlack");
        greenEnemy = atlas.createSprite("enemyGreen");
    }

}
