package uk.co.redfruit.gdx.skyisfalling.listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;
import uk.co.redfruit.gdx.skyisfalling.utils.GamePreferences;

/**
 * Created by paul on 25/09/15.
 */
public class GameInputListener extends InputAdapter {

    private OrthographicCamera camera;
    private PlayerShip playerShip;
    private Level level;

    private GamePreferences preferences = GamePreferences.getInstance();

    public GameInputListener(OrthographicCamera camera, Level level) {
        this.camera = camera;
        this.playerShip = level.getPlayerShip();
        this.level = level;
    }

    //methods start
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            playerShip.movingLeft = true;
        } else if (keycode == Input.Keys.RIGHT) {
            playerShip.movingRight = true;
        } else if (keycode == Input.Keys.SPACE) {
            if (!preferences.autoShoot) {
                level.shootPlayerLaser();
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            playerShip.movingLeft = false;
        } else if (keycode == Input.Keys.RIGHT) {
            playerShip.movingRight = false;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 target = getTarget(screenX, screenY);
        if (target.x < Constants.WORLD_WIDTH / 2) {
            playerShip.movingRight = false;
            playerShip.movingLeft = true;
        } else if (target.x > Constants.WORLD_WIDTH / 2) {
            playerShip.movingLeft = false;
            playerShip.movingRight = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 target = getTarget(screenX, screenY);
        if (target.x < Constants.WORLD_WIDTH / 2) {
            playerShip.movingLeft = false;
        } else if (target.x > Constants.WORLD_WIDTH / 2) {
            playerShip.movingRight = false;
        }
        return true;
    }

    private Vector2 getTarget(int screenX, int screenY) {
        Vector3 target = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(target.x, target.y);
    }
//methods end

}
