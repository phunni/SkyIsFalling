package uk.co.redfruit.gdx.skyisfalling.listeners.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.ControllerManager;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 23/05/16.
 */
public class SkyIsFallingControllerListener implements ControllerListener {

    private static final String TAG = "SkyIsFallingControllerListener";
    protected Level level;
    protected PlayerShip playerShip;

    @Override
    public void connected(Controller controller) {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Controller connected: " + controller.getName());
        }
        ControllerManager.setControllerListener(controller);
    }

    @Override
    public void disconnected(Controller controller) {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Controller disconnected: " + controller.getName());
        }
        controller.removeListener(ControllerManager.NVIDIA_SHIELD_CONTROLLER_LISTENER);
        controller.removeListener(ControllerManager.XBOX_360_CONTROLLER_LISTENER);
        controller.removeListener(ControllerManager.MOGA_PRO_CONTROLLER_LISTENER);
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Button pressed: " + buttonCode);
        }
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Button release: " + buttonCode);
        }
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (Constants.DEBUG) {
            if (value < -0.2f || value > 0.2f) {
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Controller Axis: + " + axisCode + " value: " + value);
                }
            }
        }
        return true;
    }



    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Controller POV: + " + povCode + " value: " + value);
        }
        return true;
    }


    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        Gdx.app.log(TAG, "X Slider Moved: " + sliderCode + " with value " + value);
        return true;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        Gdx.app.log(TAG, "Y Slider Moved: " + sliderCode + " with value " + value);
        return true;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        Gdx.app.log(TAG, "Accellerometer moved: " + accelerometerCode + " : " + value.x + "," + value.y);
        return true;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.playerShip = level.getPlayerShip();
    }

    protected void moveLeft(float value) {
        if (playerShip != null) {
            playerShip.movingRight = false;
            playerShip.movingLeft = true;
        }
    }

    protected void moveRight(float value) {
        if (playerShip != null) {
            playerShip.movingRight = true;
            playerShip.movingLeft = false;
        }
    }
}
