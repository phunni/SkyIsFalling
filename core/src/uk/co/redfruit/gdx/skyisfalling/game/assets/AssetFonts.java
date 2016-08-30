package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import org.jrenner.smartfont.SmartFontGenerator;

public class AssetFonts {

    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    private static final int SMALL_FONT = 16;
    private static final int NORMAL_FONT = 18;
    private static final int BIG_FONT = 28;

    public AssetFonts() {

        SmartFontGenerator generator = new SmartFontGenerator();
        FileHandle fontFile = Gdx.files.internal("skins/DroidSans.ttf");

        defaultSmall = generator.createFont(fontFile, "defaultSmall", SMALL_FONT);
        defaultNormal = generator.createFont(fontFile, "defaultNormal", NORMAL_FONT);
        defaultBig = generator.createFont(fontFile, "defaultLarge", BIG_FONT);

    }

}
