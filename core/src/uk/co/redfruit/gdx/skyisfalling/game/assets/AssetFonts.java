package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;

public class AssetFonts {

    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    private static final float FONT_SIZE_BASE = 0.004f;
    private static final float SMALL_FONT = 4;
    private static final float NORMAL_FONT = 5;
    private static final float BIG_FONT = 8;

    public AssetFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skins/DroidSans.ttf"));
        FreeTypeFontParameter smallParameter = new FreeTypeFontParameter();
        FreeTypeFontParameter normalParameter = new FreeTypeFontParameter();
        FreeTypeFontParameter bigParameter = new FreeTypeFontParameter();

        smallParameter.size = getSmallFontSize();
        normalParameter.size = getNormalFontSize();
        bigParameter.size = getBigFontSize();

        defaultSmall = generator.generateFont(smallParameter);
        defaultNormal = generator.generateFont(normalParameter);
        defaultBig = generator.generateFont(bigParameter);

        generator.dispose();

        defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private int getSmallFontSize() {
        return (int) (getBaseFontSize() * SMALL_FONT);
    }

    private int getNormalFontSize() {
        return (int) (getBaseFontSize() * NORMAL_FONT);
    }

    private int getBigFontSize() {
        return (int) (getBaseFontSize() * BIG_FONT);
    }

    private float getBaseFontSize() {
        return getViewportSize().x * FONT_SIZE_BASE;
    }

    private Vector2 getViewportSize() {
        return new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

}
