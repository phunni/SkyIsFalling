package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class AssetFonts {

    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    public AssetFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skins/DroidSans.ttf"));
        FreeTypeFontParameter smallParameter = new FreeTypeFontParameter();
        FreeTypeFontParameter normalParameter = new FreeTypeFontParameter();
        FreeTypeFontParameter bigParameter = new FreeTypeFontParameter();

        smallParameter.size = 8;
        normalParameter.size = 12;
        bigParameter.size = 16;

        defaultSmall = generator.generateFont(smallParameter);
        defaultNormal = generator.generateFont(normalParameter);
        defaultBig = generator.generateFont(bigParameter);

        generator.dispose();

        /*defaultSmall = new BitmapFont(Gdx.files.internal("skins/arial-15.fnt"), true);
        defaultNormal = new BitmapFont(Gdx.files.internal("skins/arial-15.fnt"), true);
        defaultBig = new BitmapFont(Gdx.files.internal("skins/arial-15.fnt"), true);

        defaultSmall.getData().setScale(0.7f);
        defaultNormal.getData().setScale(1.0f);
        defaultBig.getData().setScale(2.0f);*/

        defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

}
