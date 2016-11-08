package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;
import uk.co.redfruit.gdx.skyisfalling.utils.GamePreferences;

public class MenuScreen extends RedfruitScreen {

    private final String TAG = "MenuScreen";

    private GamePreferences preferences = GamePreferences.getInstance();
    private GooglePlayServices googlePlayServices;

    private boolean isAndroid = false;


    public MenuScreen(Game game, GooglePlayServices googlePlayServices) {
        super(game);
        preferences.load();
        this.googlePlayServices = googlePlayServices;
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            isAndroid = true;
        }
    }

    //methods start

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
    public void dispose() {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "MenuScreen disposed");
        }
        super.dispose();
    }

    private Table buildControlsLayer() {
        Table controlsLayer = new Table();
        controlsLayer.center();
        TextButton play = new TextButton("Play", skinLibgdx);
        play.getLabel().getStyle().font = normalFont;
        controlsLayer.add(play).fill().pad(10);
        controlsLayer.row();
        TextButton options = new TextButton("Options", skinLibgdx);
        options.getLabel().getStyle().font = normalFont;
        controlsLayer.add(options).fill().pad(10);
        controlsLayer.row();
        TextButton highScores = new TextButton("High Score", skinLibgdx);
        highScores.getLabel().getStyle().font = normalFont;
        highScores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isAndroid) {
                    googlePlayServices.showScore();
                }
            }
        });
        controlsLayer.add(highScores).fill().pad(10);
        controlsLayer.row();
        if (isAndroid) {
            TextButton achievements = new TextButton("Achievements", skinLibgdx);
            achievements.getLabel().getStyle().font = normalFont;
            achievements.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    googlePlayServices.showAchievements();
                }
            });
            controlsLayer.add(achievements).fill().pad(10);
            controlsLayer.row();
        }
        TextButton credits = new TextButton("Credits", skinLibgdx);
        credits.getLabel().getStyle().font = normalFont;
        controlsLayer.add(credits).fill().pad(10);
        controlsLayer.row();
        TextButton quit = new TextButton("Quit", skinLibgdx);
        quit.getLabel().getStyle().font = normalFont;
        controlsLayer.add(quit).fill().pad(10);

        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.dispose();
                game.setScreen(new GameScreen(game, googlePlayServices));
            }
        });
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.dispose();
                Gdx.app.exit();
            }
        });

        options.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.dispose();
                game.setScreen(new OptionsScreen(game, googlePlayServices));
            }

        });
        credits.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.dispose();
                game.setScreen(new CreditsScreen(game, googlePlayServices));
            }
        });

        return controlsLayer;
    }

    private Table buildTitleLayer() {
        Table layer = new Table();
        layer.center().top();
        Image title = new Image(atlas.findRegion("icon"));
        layer.add(title).maxSize(75);
        return layer;
    }

    private void rebuildStage() {
        Table controlsLayer = buildControlsLayer();
        Table backgroundLayer = buildBackgroundLayer();
        Table titleTable = buildTitleLayer();

        stage.clear();
        if ( Constants.DEBUG ) {
            stage.setDebugAll(true);
        }
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(backgroundLayer);
        stack.add(titleTable);
        stack.add(controlsLayer);
    }
//methods end
}
