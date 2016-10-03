package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 03/10/16.
 */
public class OptionsScreen extends RedfruitScreen {


    public OptionsScreen(Game game) {
        super(game);
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
    public void dispose() {
        stage.dispose();
        skinLibgdx.dispose();
    }

    private Table buildOptionsLayer() {
        Table layout = new Table();
        layout.center();
        Label title = new Label("Options", skinLibgdx);
        layout.add(title);

        return layout;
    }

    private void buildStage() {
        skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX));

        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.addActor(buildBackgroundLayer());
        stack.add(buildOptionsLayer());
    }
//methods end
}
