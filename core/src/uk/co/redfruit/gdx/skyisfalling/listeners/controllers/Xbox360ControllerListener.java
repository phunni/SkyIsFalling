package uk.co.redfruit.gdx.skyisfalling.listeners.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.mappings.XBOX360;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 04/06/16.
 */
public class Xbox360ControllerListener extends SkyIsFallingControllerListener {

    private static final String TAG = "Xbox360ControllerListener";

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (level != null) {
            if (buttonCode == XBOX360.BUTTON_RB) {
                level.shootPlayerLaser();
            } else if (buttonCode == XBOX360.BUTTON_B) {
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
        }
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (level != null ) {
            if (axisCode == XBOX360.L_AXIS_X || axisCode == XBOX360.R_AXIS_X || axisCode == XBOX360.D_PAD_AXIS_X) {
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
