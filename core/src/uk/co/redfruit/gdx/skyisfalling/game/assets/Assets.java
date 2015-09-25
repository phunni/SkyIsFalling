package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class Assets implements Disposable, AssetErrorListener {

    private static final String TAG = "Assets";

    private static final Assets instance = new Assets();

    private AssetManager assetManager;

    private AssetFonts fonts;

    private Sprite background;
    private PlayerShipAsset player;


    private Assets(){}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.assetManager.setErrorListener(this);
        this.assetManager.load(Constants.TEXTURE_SKY_IS_FALLING, TextureAtlas.class);

        this.assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_SKY_IS_FALLING);

        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        background = atlas.createSprite("background");
        player = new PlayerShipAsset(atlas);

    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset, (Exception) throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public static Assets getInstance() {
        return instance;
    }

    public AssetFonts getFonts() {
        return fonts;
    }

    public Sprite getBackground() {
        return background;
    }

    public PlayerShipAsset getPlayer() {
        return player;
    }
}
