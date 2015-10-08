package uk.co.redfruit.gdx.skyisfalling.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import uk.co.redfruit.gdx.skyisfalling.game.Level;
import uk.co.redfruit.gdx.skyisfalling.game.objects.EnemyShip;
import uk.co.redfruit.gdx.skyisfalling.game.objects.GameObject;
import uk.co.redfruit.gdx.skyisfalling.game.objects.Laser;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;

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
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        Object aUserData = a.getUserData();
        Object bUserData = b.getUserData();


        if (aUserData instanceof GameObject && bUserData instanceof GameObject) {
            if (aUserData instanceof Laser && bUserData instanceof EnemyShip)  {
                laserHitsEnemyShip((EnemyShip) bUserData, (Laser) aUserData);
            } else if (bUserData instanceof Laser && aUserData instanceof EnemyShip) {
                laserHitsEnemyShip((EnemyShip) aUserData, (Laser) bUserData);
            } /*else if (aUserData instanceof PlayerShip && bUserData instanceof EnemyShip) {
                ((PlayerShip) aUserData).lives--;
            } else if (bUserData instanceof PlayerShip && aUserData instanceof EnemyShip){
                ((PlayerShip) bUserData).lives--;
            }*/
        }

        if (aUserData instanceof EnemyShip && groundBody.equals(b)) {
            ((EnemyShip) aUserData).setDestroyed(true);
        } else if (groundBody.equals(a) && bUserData instanceof EnemyShip) {
            ((EnemyShip) bUserData).setDestroyed(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        Object aUserData = a.getUserData();
        Object bUserData = b.getUserData();

        if (aUserData instanceof PlayerShip && bUserData instanceof EnemyShip) {
            ((PlayerShip) aUserData).lives--;
        } else if (bUserData instanceof PlayerShip && aUserData instanceof EnemyShip){
            ((PlayerShip) bUserData).lives--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        Object aUserData = a.getUserData();
        Object bUserData = b.getUserData();

        if (aUserData instanceof GameObject && bUserData instanceof GameObject) {
            contact.setEnabled(false);
        }

        if (aUserData instanceof EnemyShip && groundBody.equals(b)) {
            contact.setEnabled(false);
        } else if (groundBody.equals(a) && bUserData instanceof EnemyShip) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        Object aUserData = a.getUserData();
        Object bUserData = b.getUserData();


        if (aUserData instanceof EnemyShip && groundBody.equals(b)) {
            contact.setEnabled(false);
        } else if (groundBody.equals(a) && bUserData instanceof EnemyShip) {
            contact.setEnabled(false);
        }

    }

    private void laserHitsEnemyShip(EnemyShip aUserData, Laser bUserData) {
        bUserData.setCullable(true);
        aUserData.reduceHitPoints();
    }

}
