package uk.co.redfruit.gdx.skyisfalling.listeners.controllers;

import com.badlogic.gdx.controllers.Controller;
import uk.co.redfruit.gdx.skyisfalling.game.controllers.mappings.NvidiaShield;

/**
 * Created by paul on 14/06/16.
 */
public class NvidiaShieldControllerListener extends SkyIsFallingControllerListener {


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
