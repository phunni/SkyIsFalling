package uk.co.redfruit.gdx.skyisfalling.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.objects.EnemyShip;
import uk.co.redfruit.gdx.skyisfalling.game.objects.GameObject;
import uk.co.redfruit.gdx.skyisfalling.game.objects.Laser;

/**
 * Created by paul on 02/10/15.
 */
public class WorldContactListener implements ContactListener {

    private static final String TAG = "WorldContactListener";

    private Body groundBody;
    private Body leftWall;
    private Body rightWall;
    private Level level;

    public WorldContactListener(Body groundBody, Body leftWall, Body rightWall, Level level) {
        this.groundBody = groundBody;
        this.leftWall = leftWall;
        this.rightWall = rightWall;
        this.level = level;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        Object aUserData = a.getUserData();
        Object bUserData = b.getUserData();

        if (aUserData == null || bUserData ==  null) {
            Gdx.app.log(TAG, "Found null userdata on Fixture");
        }

        if (aUserData instanceof GameObject && bUserData instanceof GameObject) {
            if (aUserData instanceof Laser && bUserData instanceof EnemyShip)  {
                ((Laser)aUserData).setCullable(true);
            } else if (bUserData instanceof Laser && aUserData instanceof EnemyShip) {
                ((Laser)bUserData).setCullable(true);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        Object aUserData = a.getUserData();
        Object bUserData = b.getUserData();

        if (aUserData instanceof GameObject && bUserData instanceof GameObject) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
