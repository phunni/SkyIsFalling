package uk.co.redfruit.gdx.skyisfalling.listeners.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.mappings.NvidiaShield;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.mappings.XBOX360;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 14/06/16.
 */
public class NvidiaShieldControllerListener extends SkyIsFallingControllerListener {

    public static final String TAG = "NvidiaShieldControllerListener";

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == NvidiaShield.BUTTON_B) {
            if (level.paused) {
                level.paused = false;
                if ( Constants.DEBUG ) {
                    Gdx.app.log(TAG, "Game unpaused by 360 controller");
                }
            } else {
                level.paused = true;
                if ( Constants.DEBUG ) {
                    Gdx.app.log(TAG, "Game paused by 360 controller");
                }
            }
        }
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (level != null ) {
            if (axisCode == NvidiaShield.L_AXIS_X || axisCode == NvidiaShield.R_AXIS_X
                    || axisCode == NvidiaShield.D_PAD_AXIS_X) {
                if (playerShip != null) {
                    if (value < -0.2f) {
                        playerShip.movingRight = false;
                        playerShip.movingLeft = true;
                    } else if (value > 0.2f) {
                        playerShip.movingRight = true;
                        playerShip.movingLeft = false;
                    }
                }
            }
        }
        return true;
    }

}
