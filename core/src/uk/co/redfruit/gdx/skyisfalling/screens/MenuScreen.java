package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import uk.co.redfruit.gdx.skyisfalling.listeners.PlayButtonListener;
import uk.co.redfruit.gdx.skyisfalling.listeners.QuitButtonListener;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class MenuScreen extends RedfruitScreen {

    private final String TAG = "MenuScreen";

    private Stage stage;
    private Skin skinSkyIsFalling;
    private Skin skinLibgdx;

    private Button playButton;
    private Button quitButton;

    private TextureAtlas menuAtlas;
    private Image background;

    public MenuScreen(Game game) {
        super(game);
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
       stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skinLibgdx.dispose();
        skinSkyIsFalling.dispose();
    }

    @Override
    public void show() {
        stage  = new Stage(new FillViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    private void rebuildStage() {
        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX));
        menuAtlas = new TextureAtlas(Constants.TEXTURE_SKY_IS_FALLING);
        Table controlsLayer = buildControlsLayer();
        Table backgroundLayer = buildBackgroundLayer();

        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(backgroundLayer);
        stack.add(controlsLayer);
    }

    private Table buildBackgroundLayer() {
        Table backgroundLayer = new Table();
        background = new Image(menuAtlas.findRegion("background"));
        backgroundLayer.add(background);
        return backgroundLayer;
    }

    private Table buildControlsLayer() {
        Table controlsLayer = new Table();
        controlsLayer.center();
        playButton = new TextButton("Play", skinLibgdx);
        controlsLayer.add(playButton);
        controlsLayer.row();
        quitButton = new TextButton("Quit", skinLibgdx);
        controlsLayer.add(quitButton);

        playButton.addListener(new PlayButtonListener(game));
        quitButton.addListener(new QuitButtonListener());

        return controlsLayer;
    }
}
