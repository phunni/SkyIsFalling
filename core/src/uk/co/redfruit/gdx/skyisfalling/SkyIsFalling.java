package uk.co.redfruit.gdx.skyisfalling;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.ControllerManager;
import uk.co.redfruit.gdx.skyisfalling.listeners.controllers.SkyIsFallingControllerListener;
import uk.co.redfruit.gdx.skyisfalling.screens.MenuScreen;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class SkyIsFalling extends Game {

	private static final String TAG = "SkyIsFalling";
    private static final SkyIsFallingControllerListener controllerListener = new SkyIsFallingControllerListener();

    public static SkyIsFallingControllerListener getControllerListener() {
        return controllerListener;
    }

    //methods start
    @Override
	public void create () {
		Assets.getInstance().init(new AssetManager());

		if (Constants.DEBUG) {

			for (Controller controller : Controllers.getControllers()) {
				Gdx.app.log(TAG, controller.getName());
				ControllerManager.setControllerListener(controller);
				Gdx.app.log(TAG, "Controller class: " + controller.getClass().getName());
			}

			Gdx.app.log(TAG, "Number of Controllers: " + Controllers.getControllers().size);
		}
		Controllers.addListener(controllerListener);

		setScreen(new MenuScreen(this));
	}

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
    }
//methods end

}
