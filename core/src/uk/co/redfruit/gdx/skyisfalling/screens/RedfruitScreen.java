package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public abstract class RedfruitScreen implements Screen {

    protected Game game;
    protected Stage stage;
    protected TextureAtlas atlas;
    protected Skin skinLibgdx;

    //For use by subclasses
    protected BitmapFont largeFont = Assets.getInstance().getFonts().defaultBig;
    protected BitmapFont normalFont = Assets.getInstance().getFonts().defaultNormal;
    protected BitmapFont smallFont = Assets.getInstance().getFonts().defaultSmall;

    public RedfruitScreen(Game game) {
        this.game = game;
    }

    protected Table buildBackgroundLayer() {
        Table backgroundLayer = new Table();
        Image background = new Image(atlas.findRegion("background"));
        backgroundLayer.add(background);
        return backgroundLayer;
    }

    //methods start
    @Override
    public void show() {
        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX));
        atlas = new TextureAtlas(Constants.TEXTURE_SKY_IS_FALLING);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skinLibgdx.dispose();
    }
//methods end
}
