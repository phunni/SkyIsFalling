package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.listeners.PlayButtonListener;
import uk.co.redfruit.gdx.skyisfalling.listeners.QuitButtonListener;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class MenuScreen extends RedfruitScreen {

    private final String TAG = "MenuScreen";

    private Button playButton;
    private Button quitButton;


    private Image background;

    public MenuScreen(Game game) {
        super(game);
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
        music.play();
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
        controlsLayer.add(playButton).fill().pad(10);
        controlsLayer.row();
        Button optionsButton = new TextButton("Options", skinLibgdx);
        controlsLayer.add(optionsButton).fill().pad(10);
        controlsLayer.row();
        Button highScores = new TextButton("High Score", skinLibgdx);
        controlsLayer.add(highScores).fill().pad(10);
        controlsLayer.row();
        Button credits = new TextButton("Credits", skinLibgdx);
        controlsLayer.add(credits).fill().pad(10);
        controlsLayer.row();
        quitButton = new TextButton("Quit", skinLibgdx);
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
