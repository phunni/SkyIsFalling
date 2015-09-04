package uk.co.redfruit.gdx.skyisfalling.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class QuitButtonListener extends ChangeListener {

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        Gdx.app.exit();
    }
}
