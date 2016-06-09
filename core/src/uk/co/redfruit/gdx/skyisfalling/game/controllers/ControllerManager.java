package uk.co.redfruit.gdx.skyisfalling.game.controllers;

import com.badlogic.gdx.controllers.Controller;
import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.listeners.controllers.MogaProControllerListener;
import uk.co.redfruit.gdx.skyisfalling.listeners.controllers.Xbox360ControllerListener;

/**
 * Created by paul on 06/06/16.
 */
public class ControllerManager {

    public static final MogaProControllerListener MOGA_PRO_CONTROLLER_LISTENER = new MogaProControllerListener();
    public static final Xbox360ControllerListener XBOX_360_CONTROLLER_LISTENER = new Xbox360ControllerListener();
    //Shield and Steam controllers required as well

    public static void setControllerListener(Controller controller) {
        String name = controller.getName();
        if (name != null) {
            if (name.contains("Moga Pro")) {
                controller.addListener(MOGA_PRO_CONTROLLER_LISTENER);
            } else if (name.contains("X-Box")) {
                controller.addListener(XBOX_360_CONTROLLER_LISTENER);
            } //TODO: add check for Shield controller & Steam Controller
        }
    }

    public static void setLevel(Level level) {
        MOGA_PRO_CONTROLLER_LISTENER.setLevel(level);
        XBOX_360_CONTROLLER_LISTENER.setLevel(level);
    }


}
