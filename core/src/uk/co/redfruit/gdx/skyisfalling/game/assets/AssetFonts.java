package uk.co.redfruit.gdx.skyisfalling.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import org.jrenner.smartfont.SmartFontGenerator;

public class AssetFonts {

    private static final int SMALL_FONT = 16;
    private static final int NORMAL_FONT = 18;
    private static final int BIG_FONT = 28;
    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    public AssetFonts() {

        SmartFontGenerator generator = new SmartFontGenerator();
        FileHandle fontFile = Gdx.files.internal("skins/Gidolinya-Regular.otf");

        defaultSmall = generator.createFont(fontFile, "defaultSmall", SMALL_FONT);
        defaultNormal = generator.createFont(fontFile, "defaultNormal", NORMAL_FONT);
        defaultBig = generator.createFont(fontFile, "defaultLarge", BIG_FONT);

    }

}
