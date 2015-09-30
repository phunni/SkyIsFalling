package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetFonts {

    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    public AssetFonts() {
        defaultSmall = new BitmapFont(Gdx.files.internal("skins/arial-15.fnt"), true);
        defaultNormal = new BitmapFont(Gdx.files.internal("skins/arial-15.fnt"), true);
        defaultBig = new BitmapFont(Gdx.files.internal("skins/arial-15.fnt"), true);

        defaultSmall.getData().setScale(0.7f);
        defaultNormal.getData().setScale(1.0f);
        defaultBig.getData().setScale(2.0f);

        defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

}
