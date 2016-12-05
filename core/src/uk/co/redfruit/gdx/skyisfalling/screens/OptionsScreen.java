package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;
import uk.co.redfruit.gdx.skyisfalling.utils.GamePreferences;

/**
 * Created by paul on 03/10/16.
 */
public class OptionsScreen extends RedfruitScreen {

    private static final String TAG = "OptionsScreen";

    private GamePreferences prefs;
    private CheckBox musicCheck;
    private Slider musicVolumeSlider;
    private CheckBox sfxCheck;
    private Slider sfxVolumeSlider;
    private CheckBox autoShootCheck;
    private CheckBox fpsCheck;
    private Music music = Assets.getInstance().getMusic();
    private TextButton back;

    private GooglePlayServices googlePlayServices;

    private GameScreen gameScreen = null;


    public OptionsScreen(Game game, GooglePlayServices googlePlayServices) {
        super(game);
        this.googlePlayServices = googlePlayServices;
        prefs = GamePreferences.getInstance();
        prefs.load();
    }

    public OptionsScreen(Game game, GooglePlayServices googlePlayServices, GameScreen gameScreen) {
        super(game);
        this.googlePlayServices = googlePlayServices;
        this.gameScreen = gameScreen;
        prefs = GamePreferences.getInstance();
        prefs.load();
    }


    //methods start
    @Override
    public void show() {
        super.show();
        stage = new Stage(new FillViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        buildStage();
        handleEvents();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "OptionsScreen disposed");
        }
    }

    private VerticalGroup buildOptionsLayer() {
        float pad = 7.5f;
        prefs.load();
        VerticalGroup layout = new VerticalGroup();
        layout.center();
        layout.columnLeft();

        musicCheck = new CheckBox("Music", skinLibgdx);
        musicCheck.getStyle().font = normalFont;
        musicCheck.setChecked(prefs.music);
        musicCheck.pad(pad);
        layout.addActor(musicCheck);

        musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, skinLibgdx);
        musicVolumeSlider.setValue(prefs.musicVolume);
        layout.addActor(musicVolumeSlider);

        sfxCheck = new CheckBox("SFX", skinLibgdx);
        sfxCheck.getStyle().font = normalFont;
        sfxCheck.setChecked(prefs.sfx);
        sfxCheck.pad(pad);
        layout.addActor(sfxCheck);


        sfxVolumeSlider = new Slider(0f, 1f, 0.1f, false, skinLibgdx);
        sfxVolumeSlider.setValue(prefs.sfxVolume);
        layout.addActor(sfxVolumeSlider);

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            autoShootCheck = new CheckBox("Auto Shoot", skinLibgdx);
            autoShootCheck.getStyle().font = normalFont;
            autoShootCheck.setChecked(prefs.autoShoot);
            autoShootCheck.pad(pad);
            layout.addActor(autoShootCheck);
        }

        fpsCheck = new CheckBox("Show FPS", skinLibgdx);
        fpsCheck.getStyle().font = normalFont;
        fpsCheck.setChecked(prefs.showFPS);
        fpsCheck.pad(pad);
        layout.addActor(fpsCheck);

        back = new TextButton("Back", skinLibgdx);
        back.getLabel().getStyle().font = normalFont;
        back.pad(pad);
        layout.addActor(back);


        return layout;
    }

    private void buildStage() {
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.addActor(buildBackgroundLayer());
        stack.add(buildTitleLayer());
        stack.add(buildOptionsLayer());
    }

    private Table buildTitleLayer() {
        Table layout = new Table();
        layout.center().top();
        Label.LabelStyle titleStyle = new Label.LabelStyle(largeFont, Color.WHITE);
        Label title = new Label("Options", titleStyle);
        layout.add(title);

        return layout;
    }

    private void handleEvents() {
        musicCheck.addListener(new ChangeListener() {
            //methods start
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.music = musicCheck.isChecked();
                if (music.isPlaying() && !musicCheck.isChecked()) {
                    music.stop();
                } else if (!music.isPlaying() && musicCheck.isChecked()) {
                    music.play();
                }
                prefs.save();
            }
//methods end
        });

        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = musicVolumeSlider.getValue();
                prefs.musicVolume = volume;
                music.setVolume(volume);
                prefs.save();
            }
        });

        sfxCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.sfx = sfxCheck.isChecked();
                prefs.save();
            }
        });

        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.sfxVolume = sfxVolumeSlider.getValue();
                prefs.save();
            }
        });
        if (autoShootCheck != null) {
            autoShootCheck.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    prefs.autoShoot = autoShootCheck.isChecked();
                    prefs.save();
                }
            });
        }

        fpsCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.showFPS = fpsCheck.isChecked();
                prefs.save();
            }
        });


        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameScreen == null) {
                    OptionsScreen.this.dispose();
                    game.setScreen(new MenuScreen(game, googlePlayServices));
                } else {
                    OptionsScreen.this.dispose();
                    game.setScreen(gameScreen);
                }
            }
        });
    }
//methods end
}
