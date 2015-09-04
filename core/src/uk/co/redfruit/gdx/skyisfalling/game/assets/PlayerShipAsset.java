package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class PlayerShipAsset {

    public final AtlasRegion ship;

    public PlayerShipAsset(TextureAtlas atlas) {
        ship = atlas.findRegion("ship");
    }

}
