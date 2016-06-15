package uk.co.redfruit.gdx.skyisfalling.listeners.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.mappings.MogaProHD;

/**
 * Created by paul on 04/06/16.
 */
public class MogaProControllerListener extends SkyIsFallingControllerListener {

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (level != null) {
            if (buttonCode == MogaProHD.BUTTON_R2) {
                level.shootPlayerLaser();
            }
        }
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (level != null ) {
            if (axisCode == MogaProHD.L_AXIS_X || axisCode == MogaProHD.R_AXIS_X || axisCode == MogaProHD.D_PAD_AXIS_X) {
                if (playerShip != null) {
                    if (value < 0) {
                        playerShip.movingRight = false;
                        playerShip.movingLeft = true;
                    } else if (value > 0) {
                        playerShip.movingRight = true;
                        playerShip.movingLeft = false;
                    }
                }
            }
        }
        return true;
    }

}
