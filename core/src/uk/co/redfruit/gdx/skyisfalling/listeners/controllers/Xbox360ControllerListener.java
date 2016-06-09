package uk.co.redfruit.gdx.skyisfalling.listeners.controllers;

import com.badlogic.gdx.controllers.Controller;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.mappings.XBOX360;

/**
 * Created by paul on 04/06/16.
 */
public class Xbox360ControllerListener extends SkyIsFallingControllerListener {

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (level != null) {
            if (buttonCode == XBOX360.BUTTON_RB) {
                level.shootPlayerLaser();
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


    public void stopMoving() {
        if (level != null ) {
            playerShip.movingRight = false;
            playerShip.movingLeft = false;
        }
    }

}
