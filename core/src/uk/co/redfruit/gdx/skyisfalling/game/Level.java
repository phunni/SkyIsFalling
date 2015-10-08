package uk.co.redfruit.gdx.skyisfalling.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.objects.EnemyShip;
import uk.co.redfruit.gdx.skyisfalling.game.objects.Laser;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.screens.MenuScreen;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class Level {

    private static final String TAG = "Level";

    private PlayerShip playerShip;
    private Array<EnemyShip> enemyShips = new Array<EnemyShip>();
    private Array<Laser> lasers = new Array<>();

    private final World world;

    private Sprite background = Assets.getInstance().getBackground();

    private float difficulty;
    private float levelNumber;
    private long startTimeForMovingShips;
    public long gameOverStartTime;
    private float score;
    public boolean gameOver;

    private Pool<EnemyShip> enemyShipPool;
    private Pool<Laser> laserPool;

    private long timeSinceLastShot;


    public Level(World newWorld) {
        this.world = newWorld;
        enemyShipPool = new Pool<EnemyShip>() {
            @Override
            protected EnemyShip newObject() {
                return new EnemyShip(world);
            }
        };
        laserPool = new Pool<Laser>() {
            @Override
            protected Laser newObject() {
                return new Laser(world);
            }
        };
        levelNumber = 1;
        setDifficulty();
        playerShip = new PlayerShip(world);
        setUpEnemyShips();
        startTimeForMovingShips = 1200000000;
        timeSinceLastShot = TimeUtils.nanoTime();

        background.setBounds(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        background.setScale(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }

    private void setDifficulty() {
        if (levelNumber < 2) {
            difficulty = 0;
        } else if (levelNumber < 5) {
            difficulty = 1;
        } else if (levelNumber < 8) {
            difficulty = 2;
        } else if (levelNumber < 12) {
            difficulty = 3;
        }
    }

    public void render(SpriteBatch batch) {
        if (!gameOver) {
            boolean moving = false;
            batch.disableBlending();
            background.draw(batch);
            batch.enableBlending();
            playerShip.render(batch);
            if (TimeUtils.timeSinceNanos(startTimeForMovingShips) > 1400000000) {
                moving = true;
            }
            for (EnemyShip enemy : enemyShips) {
                enemy.render(batch);
                if (moving) {
                    if (enemy.lastDirection == 0) {
                        enemy.movingRight = true;
                        enemy.lastDirection = 1;
                    } else if (enemy.lastDirection == 1) {
                        enemy.movingLeft = true;
                        enemy.lastDirection = 0;
                    }
                } else {
                    enemy.movingLeft = false;
                    enemy.movingRight = false;
                }
            }
            for (Laser laser : lasers) {
                laser.render(batch);
            }
            if (moving) {
                startTimeForMovingShips = TimeUtils.nanoTime();
            }
        }

    }

    public void update(float deltaTime) {
        if (playerShip.movingLeft) {
            playerShip.moveLeft();
        } else if (playerShip.movingRight) {
            playerShip.moveRight();
        } else {
            playerShip.stop();
        }

        if (getPlayerShip().lives <= 0 && !gameOver) {
            gameOverStartTime = TimeUtils.nanoTime();
            gameOver = true;
        }

        for (Laser laser : lasers) {
            if (laser.isCullable()) {
                lasers.removeValue(laser, false);
                laserPool.free(laser);
            }
        }

        for (EnemyShip ship : enemyShips) {
            if (ship.isDestroyed()) {
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Ship destroyed");
                }
                enemyShips.removeValue(ship, false);
                enemyShipPool.free(ship);
            }
        }

        //if on Android we need to shoot the lasers automatically
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (TimeUtils.timeSinceNanos(timeSinceLastShot) > 500000000) {
                shootLaser();
                timeSinceLastShot = TimeUtils.nanoTime();
            }
        }
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    public Pool<Laser> getLaserPool() {
        return laserPool;
    }

    public Array<Laser> getLasers() {
        return lasers;
    }

    public void increaseScore(int increase){
        score += increase;
    }

    public int getScore() {
        return (int)score;
    }

    public void shootLaser() {
        Laser laser = laserPool.obtain();
        laser.init("blue", playerShip.getPosition());
        lasers.add(laser);
    }

    private void setUpEnemyShips() {
        if (difficulty == 0) {
            addShips("black", 0.0f);
            addShips("black", 1.5f);
            addShips("black", 3.0f);
        }
    }

    private void addShips(String colour, float height) {
        for (int i =0; i < 6; i++) {
            EnemyShip enemy = enemyShipPool.obtain();
            enemy.init(world, this, colour, new Vector2(0.5f + (2.5f * i), Constants.WORLD_HEIGHT - height));
            /*if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Body is awake: " + enemy.isAWake());
            }*/
            enemyShips.add(enemy);
        }
    }


}
