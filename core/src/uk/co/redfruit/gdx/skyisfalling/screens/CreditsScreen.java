package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 03/10/16.
 */
public class CreditsScreen extends RedfruitScreen {

    private static final String TAG = "CreditsScreen";

    private GooglePlayServices googlePlayServices;


    public CreditsScreen(Game game, GooglePlayServices googlePlayServices) {
        super(game);
        this.googlePlayServices = googlePlayServices;
    }

    //methods start
    @Override
    public void show() {
        super.show();
        stage = new Stage(new FillViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        buildStage();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "CreditsScreen disposed");
        }
        super.dispose();
    }

    private VerticalGroup buildCreditsLayer() {
        VerticalGroup layout = new VerticalGroup();
        layout.center();
        layout.space(10);

        Label.LabelStyle titleStyle = new Label.LabelStyle(largeFont, Color.GREEN);
        Label.LabelStyle creditsStyle = new Label.LabelStyle(normalFont, Color.WHITE);

        Label programming = new Label("Programming", titleStyle);
        layout.addActor(programming);

        Label hunnisett = new Label("Paul Hunnisett", creditsStyle);
        layout.addActor(hunnisett);

        Label graphics = new Label("Graphics", titleStyle);
        layout.addActor(graphics);

        Label kenney = new Label("Kenney", creditsStyle);
        layout.addActor(kenney);

        Label openGameArt = new Label("opengameart.org", creditsStyle);
        layout.addActor(openGameArt);

        Label music = new Label("Music", titleStyle);
        layout.addActor(music);

        Label modine = new Label("Frederic Modine", creditsStyle);
        layout.addActor(modine);

        Label sfx = new Label("Sound Effects", titleStyle);
        layout.addActor(sfx);

        //This is required because simply reusing the above label breaks the layout
        Label hunnisett_2 = new Label("Paul Hunnisett", creditsStyle);
        layout.addActor(hunnisett_2);

        TextButton back = new TextButton("Back", skinLibgdx);
        back.getLabel().getStyle().font = normalFont;
        back.pad(10f);
        back.addListener(new ChangeListener() {
            //methods start
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CreditsScreen.this.dispose();
                game.setScreen(new MenuScreen(game, googlePlayServices));
            }
        });
        layout.addActor(back);


        return layout;
    }

    private void buildStage() {
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(buildBackgroundLayer());
        stack.add(buildCreditsLayer());
    }
//methods end
}
