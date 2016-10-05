package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.listeners.PlayButtonListener;
import uk.co.redfruit.gdx.skyisfalling.listeners.QuitButtonListener;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;
import uk.co.redfruit.gdx.skyisfalling.utils.GamePreferences;

public class MenuScreen extends RedfruitScreen {

    private final String TAG = "MenuScreen";

    private TextButton playButton;
    private TextButton quitButton;

    private GamePreferences preferences = GamePreferences.getInstance();


    public MenuScreen(Game game) {
        super(game);
        preferences.load();
    }

    //methods start
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void show() {
        super.show();
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Device density: " + Gdx.graphics.getDensity());
        }
        stage  = new Stage(new FillViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
        Music music = Assets.getInstance().getMusic();
        music.setLooping(true);
        if ( preferences.music ) {
            music.setVolume(preferences.musicVolume);
            music.play();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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

    private Table buildControlsLayer() {
        Table controlsLayer = new Table();
        controlsLayer.center();
        playButton = new TextButton("Play", skinLibgdx);
        playButton.getLabel().getStyle().font = normalFont;
        controlsLayer.add(playButton).fill().pad(10);
        controlsLayer.row();
        TextButton optionsButton = new TextButton("Options", skinLibgdx);
        optionsButton.getLabel().getStyle().font = normalFont;
        controlsLayer.add(optionsButton).fill().pad(10);
        controlsLayer.row();
        TextButton highScores = new TextButton("High Score", skinLibgdx);
        highScores.getLabel().getStyle().font = normalFont;
        controlsLayer.add(highScores).fill().pad(10);
        controlsLayer.row();
        TextButton credits = new TextButton("Credits", skinLibgdx);
        credits.getLabel().getStyle().font = normalFont;
        controlsLayer.add(credits).fill().pad(10);
        controlsLayer.row();
        quitButton = new TextButton("Quit", skinLibgdx);
        quitButton.getLabel().getStyle().font = normalFont;
        controlsLayer.add(quitButton).fill().pad(10);

        playButton.addListener(new PlayButtonListener(game));
        quitButton.addListener(new QuitButtonListener());
        optionsButton.addListener(new ChangeListener() {
            //methods start
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionsScreen(game));
            }
//methods end
        });

        return controlsLayer;
    }

    private void rebuildStage() {
        Table controlsLayer = buildControlsLayer();
        Table backgroundLayer = buildBackgroundLayer();

        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(backgroundLayer);
        stack.add(controlsLayer);
    }
//methods end
}
