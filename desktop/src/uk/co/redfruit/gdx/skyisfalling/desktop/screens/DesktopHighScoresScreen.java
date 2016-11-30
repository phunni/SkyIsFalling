package uk.co.redfruit.gdx.skyisfalling.desktop.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;

import uk.co.redfruit.gdx.skyisfalling.desktop.scores.HighScores;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.screens.MenuScreen;
import uk.co.redfruit.gdx.skyisfalling.screens.RedfruitScreen;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 30/11/16.
 */

public class DesktopHighScoresScreen extends RedfruitScreen {

    private HighScores scores;
    private GooglePlayServices playServices;

    public DesktopHighScoresScreen(Game game, GooglePlayServices playServices, HighScores scores) {
        super(game);
        this.scores = scores;
        this.playServices = playServices;
    }

    @Override
    public void show() {
        super.show();
        stage = new Stage(new FillViewport(Constants.VIEWPORT_GUI_WIDTH
                , Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        buildStage();
    }

    private void buildStage() {
        Table backgroundLayer = buildBackgroundLayer();
        VerticalGroup highScoresLayer = buildHighScoresLayer();

        stage.clear();

        if (Constants.DEBUG) {
            stage.setDebugAll(true);
        }

        Stack stack = new Stack();
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stage.addActor(stack);

        stack.add(backgroundLayer);
        stack.add(highScoresLayer);
    }

    private VerticalGroup buildHighScoresLayer() {
        VerticalGroup highScoresLayer = new VerticalGroup();
        highScoresLayer.center();
        highScoresLayer.setFillParent(true);
        highScoresLayer.space(10);
        highScoresLayer.columnLeft();

        Label.LabelStyle titleStyle = new Label.LabelStyle(largeFont, Color.GREEN);
        Label.LabelStyle scoresStyle = new Label.LabelStyle(normalFont, Color.WHITE);

        highScoresLayer.addActor(new Label("High Scores", titleStyle));

        for (int i = 0; i < scores.scores.size; i++) {
            int score = scores.scores.get(i);
            highScoresLayer.addActor(new Label((i + 1) + ". " + score, scoresStyle));
        }

        TextButton backButton = new TextButton("Back", skinLibgdx);
        backButton.getLabel().getStyle().font = normalFont;
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DesktopHighScoresScreen.this.dispose();
                game.setScreen(new MenuScreen(game, playServices));
            }
        });

        highScoresLayer.addActor(backButton);

        highScoresLayer.pack();

        return highScoresLayer;
    }

}
