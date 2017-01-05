package uk.co.redfruit.gdx.skyisfalling.listeners;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

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
            if (aUserData instanceof Laser && bUserData instanceof EnemyShip) {
                laserEnemyCollision((Laser) aUserData, (EnemyShip) bUserData);
            } else if (bUserData instanceof Laser && aUserData instanceof EnemyShip) {
                laserEnemyCollision((Laser) bUserData, (EnemyShip) aUserData);
            } else if (aUserData instanceof PlayerShip && bUserData instanceof EnemyShip) {
                if (!level.playerExploding) {
                    ((PlayerShip) aUserData).loseALife();
                }
            } else if (bUserData instanceof PlayerShip && aUserData instanceof EnemyShip) {
                if (!level.playerExploding) {
                    ((PlayerShip) bUserData).loseALife();
                }
            } else if (aUserData instanceof Laser && bUserData instanceof PlayerShip) {
                if (((Laser) aUserData).isEnemyLaser) {
                    playerShipHitByLaser((Laser) aUserData, (PlayerShip) bUserData);
                }
            } else if (bUserData instanceof Laser && aUserData instanceof PlayerShip) {
                if (((Laser) bUserData).isEnemyLaser) {
                    playerShipHitByLaser((Laser) bUserData, (PlayerShip) aUserData);
                }
            }
        } else if (aUserData instanceof EnemyShip && groundBody.equals(b)) {
            ((EnemyShip) aUserData).setDestroyed(true);
        } else if (groundBody.equals(a) && bUserData instanceof EnemyShip) {
            ((EnemyShip) bUserData).setDestroyed(true);
        } else if (aUserData instanceof Laser && groundBody.equals(b)) {
            laserHitsGround((Laser) aUserData);
        } else if (bUserData instanceof Laser && groundBody.equals(a)) {
            laserHitsGround((Laser) bUserData);
        }


    }


    @Override
    public void endContact(Contact contact) {

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

    }

    private void laserHitsEnemyShip(EnemyShip aUserData, Laser bUserData) {
        bUserData.setCullable(true);
        if ("blue".equals(bUserData.getColour())) {
            //bUserData.setCullable(true);
            if (!aUserData.isFalling()) {
                aUserData.reduceHitPoints();
            } else {
                aUserData.setDestroyed(true);
            }
        } else if ("red".equals(bUserData.getColour())) {
            aUserData.setDestroyed(true);
        }
    }

    private void playerShipHitByLaser(Laser laser, PlayerShip ship) {
        if (laser.isEnemyLaser && !level.playerExploding) {
            laser.setCullable(true);
            ship.loseALife();
        }
    }

    private void laserEnemyCollision(Laser laser, EnemyShip enemyShip) {
        if (!laser.isEnemyLaser) {
            laserHitsEnemyShip(enemyShip, laser);
        }
    }

    private void laserHitsGround(Laser laser) {
        if (laser.isEnemyLaser) {
            laser.setCullable(true);
        }
    }

}
