package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver.Resolution;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class Assets implements Disposable, AssetErrorListener {

    private static final String TAG = "Assets";

    private static final Assets instance = new Assets();

    private AssetManager assetManager;

    private AssetFonts fonts;

    private Sprite background;
    private Sprite playerLife;
    private PlayerShipAsset player;
    private EnemyShipAsset enemies;
    private LaserAsset lasers;
    private ExplosionAsset explosion;
    private Sprite pause;


    private Assets(){}

    public void init(AssetManager assetManager) {
        ResolutionFileResolver fileResolver = new ResolutionFileResolver(new InternalFileHandleResolver(),
                new Resolution(800, 480, ""), new Resolution(1200, 1920, "high"));
        String textureAtlas = fileResolver.resolve(Constants.TEXTURE_SKY_IS_FALLING).path();
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Resolved File: " + textureAtlas);
        }

        this.assetManager = assetManager;
        this.assetManager.setErrorListener(this);
        this.assetManager.load(textureAtlas, TextureAtlas.class);

        this.assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(textureAtlas);

        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        background = atlas.createSprite("background");
        playerLife = atlas.createSprite("playerLife");
        player = new PlayerShipAsset(atlas);
        enemies = new EnemyShipAsset(atlas);
        lasers = new LaserAsset(atlas);
        fonts = new AssetFonts();
        explosion = new ExplosionAsset(atlas);
        pause = atlas.createSprite("pause");
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
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

    public EnemyShipAsset getEnemies() {
        return enemies;
    }

    public LaserAsset getLasers(){
        return lasers;
    }

    public Sprite getPlayerLife() {
        return playerLife;
    }

    public ExplosionAsset getExplosion() {
        return explosion;
    }

    public Sprite getPause() {
        return pause;
    }
}
