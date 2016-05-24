package uk.co.redfruit.gdx.skyisfalling.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.mappings.MogaProHD;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 23/05/16.
 */
public class SkyIsFallingControllerListener implements ControllerListener {

    private static final String TAG = "SkyIsFallingControllerListener";
    private Level level;
    private PlayerShip playerShip;

    @Override
    public void connected(Controller controller) {
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Controller connected: " + controller.getName());
        }
    }

    @Override
    public void disconnected(Controller controller) {
        Gdx.app.log(TAG, "Controller disconnected: " + controller.getName());
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        if (level != null) {
            if (buttonCode == MogaProHD.BUTTON_L2) {
                level.shootPlayerLaser();
            }
        }

        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Button pressed: " + buttonCode);
        }
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        Gdx.app.log(TAG, "Button release: " + buttonCode);
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (axisCode == MogaProHD.L_AXIS_X || axisCode == MogaProHD.R_AXIS_X) {
            if (playerShip != null) {
                if (value < 0) {
                    playerShip.movingRight = false;
                    playerShip.movingLeft = true;
                } else if (value > 0){
                    playerShip.movingRight = true;
                    playerShip.movingLeft = false;
                } else {
                    playerShip.movingRight = false;
                    playerShip.movingLeft = false;
                }
            }
        }
        if (Constants.DEBUG) {
            Gdx.app.log(TAG, "Controller Axis value: " + value);
        }
        return true;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if (playerShip != null) {
            if (value.toString().equals(MogaProHD.POV_W)) {
                playerShip.movingRight = false;
                playerShip.movingLeft = true;
            } else if (value.toString().equals(MogaProHD.POV_E)) {
                playerShip.movingRight = true;
                playerShip.movingLeft = false;
            } else if (value.toString().equals(MogaProHD.POV_C)) {
                playerShip.movingRight = false;
                playerShip.movingLeft = false;
            }
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
}
