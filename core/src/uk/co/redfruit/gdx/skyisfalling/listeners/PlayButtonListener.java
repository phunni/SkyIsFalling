package uk.co.redfruit.gdx.skyisfalling.listeners;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.screens.GameScreen;

public class PlayButtonListener extends ChangeListener {

    private Game game;
    private GooglePlayServices googlePlayServices;

    public PlayButtonListener(Game game, GooglePlayServices googlePlayServices) {
        this.game = game;
        this.googlePlayServices = googlePlayServices;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new GameScreen(game, googlePlayServices));
    }
}
