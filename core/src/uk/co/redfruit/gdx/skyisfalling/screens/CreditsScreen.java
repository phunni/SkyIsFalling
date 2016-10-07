package uk.co.redfruit.gdx.skyisfalling.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 03/10/16.
 */
public class CreditsScreen extends RedfruitScreen {

    public CreditsScreen(Game game) {
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
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    private Table buildBackButtonLayer() {
        Table layout = new Table();
        layout.center().bottom();
        TextButton back = new TextButton("Back", skinLibgdx);
        back.getLabel().getStyle().font = normalFont;
        back.pad(10f);
        back.addListener(new ChangeListener() {
            //methods start
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
//methods end
        });
        layout.add(back);
        return layout;
    }

    private void buildStage() {
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(buildBackgroundLayer());

        stack.add(buildBackButtonLayer());
    }
//methods end
}
