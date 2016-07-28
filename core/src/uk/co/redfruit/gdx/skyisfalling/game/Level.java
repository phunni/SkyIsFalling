package uk.co.redfruit.gdx.skyisfalling.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.objects.EnemyShip;
import uk.co.redfruit.gdx.skyisfalling.game.objects.Explosion;
import uk.co.redfruit.gdx.skyisfalling.game.objects.Laser;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.screens.GameScreen;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class Level {

    private static final String TAG = "Level";

    private PlayerShip playerShip;
    private Array<EnemyShip> enemyShips = new Array<>();
    private Array<Laser> lasers = new Array<>();
    private Array<Explosion> explosions = new Array<>();

    private final World world;

    private Sprite background = Assets.getInstance().getBackground();

    private float difficulty;
    public float levelNumber;
    private long startTimeForMovingShips;
    public long gameOverStartTime;
    private float score;
    public boolean gameOver;
    private long timeStartedShowingWaveNumber;
    public boolean showingWaveNumber;

    private Pool<EnemyShip> enemyShipPool;
    private Pool<Laser> laserPool;
    private Pool<Explosion> explosionPool;

    private long timeSinceLastShot;
    private long lastEnemyShot;
    private long timeSinceLastExplosion = TimeUtils.millis();


    public Level(World newWorld) {
        this.world = newWorld;
        init();

        playerShip = new PlayerShip(world);

        background.setSize(GameScreen.camera.viewportWidth * 1.5f, GameScreen.camera.viewportHeight * 1.5f);
        background.setPosition(0, 0);
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
        batch.disableBlending();
        background.draw(batch);
        batch.enableBlending();
        if (!gameOver && !showingWaveNumber) {
            boolean moving = false;
            playerShip.render(batch);
            if (TimeUtils.timeSinceNanos(startTimeForMovingShips) > 1400000000) {
                moving = true;
            }
            for (EnemyShip enemy : enemyShips) {
                enemy.render(batch);
                if (!enemy.isFalling()) {
                    if (moving) {
                        if (enemy.lastDirection == 0) {
                            enemy.movingRight = true;
                            enemy.lastDirection = 1;
                        } else if (enemy.lastDirection == 1) {
                            enemy.movingLeft = true;
                            enemy.lastDirection = 2;
                        } else if (enemy.lastDirection == 2) {
                            enemy.movingDown = true;
                            enemy.lastDirection = 0;
                        }
                    } else {
                        stopShips(enemy);
                    }
                } else {
                    stopShips(enemy);
                }
            }
            for (Laser laser : lasers) {
                laser.render(batch);
            }
            for (Explosion explosion : explosions) {
                explosion.render(batch);
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

        if (TimeUtils.timeSinceMillis(timeStartedShowingWaveNumber) > 1500) {
            showingWaveNumber = false;
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

        if (!gameOver && !showingWaveNumber && TimeUtils.timeSinceMillis(lastEnemyShot) > 100) {
            if (TimeUtils.timeSinceMillis(lastEnemyShot) > 2000) {
                if (enemyShips.size > 0) {
                    shootEnemyLaser(getRandomEnemyShip());
                    lastEnemyShot = TimeUtils.millis();
                }
            }

            if (MathUtils.randomBoolean(0.01f * difficulty)) {
                if (enemyShips.size > 0) {
                    shootEnemyLaser(getRandomEnemyShip());
                    lastEnemyShot = TimeUtils.millis();
                }
            }
        }

        if (enemyShips.size <= 0) {
            init();
        }

        //if on Android we need to shoot the lasers automatically
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (TimeUtils.timeSinceNanos(timeSinceLastShot) > 250000000) {
                shootPlayerLaser();
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

    public void shootPlayerLaser() {

        if (!gameOver && !showingWaveNumber) {
            Laser laser = laserPool.obtain();
            laser.init("blue", playerShip.getPosition(), new Vector2(0, 15));
            lasers.add(laser);
        }
    }

    public void shootEnemyLaser(EnemyShip ship) {
        if (!ship.isFalling()) {
            Laser laser = laserPool.obtain();
            laser.init("green", ship.getCentre(), new Vector2(0, -9));
            lasers.add(laser);
        }
    }

    public void blowUp(Vector2 position, Vector2 size) {
        if (TimeUtils.timeSinceMillis(timeSinceLastExplosion) >250) {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "New explosion at: " + position.x + ", " + position.y);
            }
            Explosion explosion = explosionPool.obtain();
            explosion.init(position, size);
            explosions.add(explosion);
            timeSinceLastExplosion = TimeUtils.millis();
        }
    }

    private void setUpEnemyShips() {
        if (difficulty == 0) {
            addShips("black", 0.0f);
            addShips("black", 1.5f);
            addShips("black", 3.0f);
        } else if (difficulty == 1) {
            addShips("blue", 0.0f);
            addShips("black", 1.5f);
            addShips("black", 3.0f);
        } else if (difficulty == 2) {
            addShips("green", 0.0f);
            addShips("blue", 1.5f);
            addShips("black", 3.0f);
        } else if (difficulty >= 3) {
            addShips("red", 0.0f);
            addShips("green", 1.5f);
            addShips("blue", 3.0f);
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

    private EnemyShip getRandomEnemyShip() {
        if (enemyShips.size > 0) {
            int ship = MathUtils.random(enemyShips.size - 1);
            return enemyShips.get(ship);
        } else {
            return null;
        }
    }

    private void init() {
        enemyShipPool = new Pool<EnemyShip>() {
            @Override
            protected EnemyShip newObject() {
                return new EnemyShip(world);
            }
        };
        laserPool = new Pool<Laser>() {
            @Override
            protected Laser newObject() {
                return new Laser(world, GameScreen.camera);
            }
        };
        explosionPool = new Pool<Explosion>() {
            @Override
            protected Explosion newObject() {
                return new Explosion();
            }
        };

        for (Laser laser : lasers) {
            laser.setCullable(true);
        }

        levelNumber++;
        setDifficulty();

        setUpEnemyShips();
        startTimeForMovingShips = 1200000000;
        timeSinceLastShot = TimeUtils.nanoTime();

        timeStartedShowingWaveNumber = TimeUtils.millis();
        showingWaveNumber = true;
    }

    private void stopShips(EnemyShip enemy) {
        enemy.movingLeft = false;
        enemy.movingRight = false;
        enemy.movingDown = false;
    }


}
