package uk.co.redfruit.gdx.skyisfalling;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.ControllerManager;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.listeners.controllers.SkyIsFallingControllerListener;
import uk.co.redfruit.gdx.skyisfalling.screens.MenuScreen;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class SkyIsFalling extends Game {

	private static final String TAG = "SkyIsFalling";
    private static final SkyIsFallingControllerListener controllerListener = new SkyIsFallingControllerListener();

	private GooglePlayServices googlePlayServices;

	public SkyIsFalling(GooglePlayServices googlePlayServices) {
		this.googlePlayServices = googlePlayServices;
	}

	public static SkyIsFallingControllerListener getControllerListener() {
        return controllerListener;
    }

    //methods start
    @Override
	public void create () {
		Assets.getInstance().init(new AssetManager());

		googlePlayServices.signIn();

		if (Constants.DEBUG) {

			for (Controller controller : Controllers.getControllers()) {
				Gdx.app.log(TAG, controller.getName());
				ControllerManager.setControllerListener(controller);
				Gdx.app.log(TAG, "Controller class: " + controller.getClass().getName());
			}

			Gdx.app.log(TAG, "Number of Controllers: " + Controllers.getControllers().size);
		}
		Controllers.addListener(controllerListener);

		setScreen(new MenuScreen(this, googlePlayServices));
	}

    @Override
    public void dispose() {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Sky is Falling Game class disposed");
        }
        Assets.getInstance().dispose();
    }
//methods end

}
