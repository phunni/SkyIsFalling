package uk.co.redfruit.gdx.skyisfalling.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

/**
 * Created by paul on 23/05/16.
 */
public class SkyIsFallingControllerListener implements ControllerListener {

    private static final String TAG = "SkyIsFallingControllerListener";

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
        Gdx.app.log(TAG, "Button pressed: " + buttonCode);
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        Gdx.app.log(TAG, "Button release: " + buttonCode);
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Gdx.app.log(TAG, "Axis Moved: " + axisCode + " with value " + value);
        return true;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        Gdx.app.log(TAG, "Pov Moved: " + povCode + " with value " + value);
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
        return false;
    }
}
