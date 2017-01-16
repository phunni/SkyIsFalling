package uk.co.redfruit.gdx.skyisfalling.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
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
import uk.co.redfruit.gdx.skyisfalling.game.objects.LaserPowerUp;
import uk.co.redfruit.gdx.skyisfalling.game.objects.NewLifePowerUp;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PowerUp;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.screens.GameScreen;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;
import uk.co.redfruit.gdx.skyisfalling.utils.GamePreferences;

public class Level {

    private static final String TAG = "Level";

    private final World world;
    public float levelNumber;
    public long gameOverStartTime;
    public boolean gameOver;
    public boolean showingWaveNumber;
    public boolean paused;
    public boolean unpaused;
    public boolean playerExploding;
    private PlayerShip playerShip;
    private Array<EnemyShip> enemyShips = new Array<>();
    private Array<Laser> lasers = new Array<>();
    private Array<Explosion> explosions = new Array<>();
    private Array<PowerUp> powerUps = new Array<>();
    private float difficulty;
    private float score;
    private long timeStartedShowingWaveNumber;
    private Pool<EnemyShip> enemyShipPool;
    private Pool<Laser> laserPool;
    private Pool<Explosion> explosionPool;
    private Pool<NewLifePowerUp> newLifePowerUpPool;
    private Pool<LaserPowerUp> laserPowerUpPool;
    private long timeSinceLastShot;
    private long lastEnemyShot;
    private long timeSinceLastExplosion = TimeUtils.millis();
    private boolean isAndroid = false;
    private Sound playerPew = Assets.getInstance().getPlayerPew();
    private Sound enemyPew = Assets.getInstance().getEnemyPew();
    private Sound boom = Assets.getInstance().getBoom();
    private Sound redPew = Assets.getInstance().getRedPew();
    private boolean timeForNewPowerup;
    private Vector2 powerUpPosition;
    private boolean laserPowerUpActive = false;
    private int laserPowerUpFrames;

    private GamePreferences preferences = GamePreferences.getInstance();
    private GooglePlayServices googlePlayServices;


    public Level(World newWorld, GooglePlayServices googlePlayServices) {
        this.world = newWorld;
        this.googlePlayServices = googlePlayServices;

        enemyShipPool = new Pool<EnemyShip>(18) {
            @Override
            protected EnemyShip newObject() {
                return new EnemyShip(world);
            }
        };
        laserPool = new Pool<Laser>(6) {
            @Override
            protected Laser newObject() {
                return new Laser(world, GameScreen.camera);
            }
        };
        explosionPool = new Pool<Explosion>(6) {
            @Override
            protected Explosion newObject() {
                return new Explosion();
            }
        };
        newLifePowerUpPool = new Pool<NewLifePowerUp>(6) {
            @Override
            protected NewLifePowerUp newObject() {
                return new NewLifePowerUp();
            }
        };
        laserPowerUpPool = new Pool<LaserPowerUp>() {
            @Override
            protected LaserPowerUp newObject() {
                return new LaserPowerUp();
            }
        };

        init();

        playerShip = new PlayerShip(world, this);
        preferences.load();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            isAndroid = true;
        }


    }

    //methods start
    public void blowUp(Vector2 position, Vector2 size) {
        if (TimeUtils.timeSinceMillis(timeSinceLastExplosion) > 250) {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "New explosion at: " + position.x + ", " + position.y);
            }
            Explosion explosion = explosionPool.obtain();
            explosion.init(position, size);
            explosions.add(explosion);
            timeSinceLastExplosion = TimeUtils.millis();
            if (preferences.sfx) {
                boom.play(preferences.sfxVolume);
            }
            if (position.y > 3 && difficulty >= 1) {
                int dice = 0;
                if (laserPowerUpActive) {
                    dice = 15;
                } else if (difficulty == 1) {
                    dice = 10;
                } else if (difficulty == 2) {
                    dice = 8;
                } else if (difficulty == 3) {
                    dice = 6;
                } else if (difficulty == 4) {
                    dice = 4;
                } else if (difficulty >= 5) {
                    dice = 3;
                }
                int showPowerUp = MathUtils.random(0, dice);
                if (showPowerUp == 0) {
                    timeForNewPowerup = true;
                    powerUpPosition = position.cpy();
                }
            }
        }
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    public int getScore() {
        return (int) score;
    }

    public void increaseScore(int increase) {
        score += increase;
        if (score == 1000) {
            googlePlayServices.unlockAchievement("achievement_1k_club");
        } else if (score == 5000) {
            googlePlayServices.unlockAchievement("achievement_5k_club");
        } else if (score == 10000) {
            googlePlayServices.unlockAchievement("achievement_10k_club");
        }
    }

    public void render(SpriteBatch batch) {
        if (!gameOver && !showingWaveNumber) {
            if (!playerExploding) {
                playerShip.render(batch);

                for (EnemyShip enemy : enemyShips) {
                    enemy.render(batch);
                }
                for (Laser laser : lasers) {
                    laser.render(batch);
                }
            }

            for (Explosion explosion : explosions) {
                explosion.render(batch);
            }

            for (PowerUp powerUp : powerUps) {
                powerUp.render(batch);
            }

        }

    }

    public void shootPlayerLaser() {

        if (!gameOver && !showingWaveNumber) {
            Laser laser = laserPool.obtain();
            if (!laserPowerUpActive) {
                laser.init("blue", playerShip.getPosition(), new Vector2(0, 15));
            } else {
                laser.init("red", playerShip.getPosition(), new Vector2(0, 15));
            }
            lasers.add(laser);
            if (preferences.sfx) {
                long pewID;
                if (!laserPowerUpActive) {
                    pewID = playerPew.play(preferences.sfxVolume);
                } else {
                    pewID = redPew.play(preferences.sfxVolume);
                }
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Pew ID: " + pewID);
                }
            }
        }
    }

    public void update() {
        if (timeForNewPowerup) {
            if (MathUtils.random(0, 2) == 0) {
                if (playerShip.lives < 9) {
                    NewLifePowerUp newLifePowerUp = newLifePowerUpPool.obtain();
                    newLifePowerUp.init(powerUpPosition.cpy());
                    powerUps.add(newLifePowerUp);
                }
            } else {
                LaserPowerUp laserPowerUp = laserPowerUpPool.obtain();
                laserPowerUp.init(powerUpPosition.cpy());
                powerUps.add(laserPowerUp);
            }
            powerUpPosition = null;
            timeForNewPowerup = false;
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Number of power ups: " + powerUps.size);
            }
        }

        if (laserPowerUpActive) {
            laserPowerUpFrames++;
        }

        if (laserPowerUpFrames >= 180) {
            laserPowerUpActive = false;
            laserPowerUpFrames = 0;
        }

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
            if (laser.isCullable() || playerExploding) {
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

        for (PowerUp powerUp : powerUps) {
            if (Intersector.overlaps(playerShip.playerShipRegion.ship.getBoundingRectangle()
                    , powerUp.sprite.getBoundingRectangle())) {
                powerUp.setCullable(true);
                if (powerUp instanceof NewLifePowerUp) {
                    if (playerShip.lives < 9) {
                        playerShip.lives++;
                    }
                    if (preferences.sfx) {
                        Assets.getInstance().getOneUp().play(preferences.sfxVolume);
                    }
                } else if (powerUp instanceof LaserPowerUp) {
                    laserPowerUpActive = true;
                    laserPowerUpFrames = 0;
                }
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Player has collided with power up");
                }
            }
            if (powerUp.isCullable()) {
                powerUps.removeIndex(powerUps.indexOf(powerUp, false));
                if (powerUp instanceof NewLifePowerUp) {
                    newLifePowerUpPool.free((NewLifePowerUp) powerUp);
                }
            }
        }

        for (Explosion explosion : explosions) {
            if (explosion.isCullable()) {
                explosions.removeValue(explosion, false);
                explosionPool.free(explosion);
            }
        }

        if (!gameOver && !showingWaveNumber && TimeUtils.timeSinceMillis(lastEnemyShot) > 200
                && !playerExploding) {

            if (MathUtils.randomBoolean(0.01f * difficulty)) {
                if (enemyShips.size > 0) {
                    shootEnemyLaser(getRandomEnemyShip());
                    lastEnemyShot = TimeUtils.millis();
                }
            }

            if (TimeUtils.timeSinceMillis(lastEnemyShot) > 2000) {
                if (enemyShips.size > 0) {
                    shootEnemyLaser(getRandomEnemyShip());
                    lastEnemyShot = TimeUtils.millis();
                }
            }

        }

        if (enemyShips.size <= 0 && !playerExploding) {
            init();
        }

        //if on Android we need to shoot the lasers automatically
        if ((isAndroid || preferences.autoShoot) && !playerExploding) {
            if (TimeUtils.timeSinceNanos(timeSinceLastShot) > Constants.SECOND * 0.186) {
                shootPlayerLaser();
                timeSinceLastShot = TimeUtils.nanoTime();
            }
        }
    }

    private void addShips(String colour, float height) {
        for (int i = 0; i < 6; i++) {
            EnemyShip enemy = enemyShipPool.obtain();
            enemy.init(this, colour, new Vector2(0.8f + (2.5f * i), Constants.WORLD_HEIGHT - height));
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

        for (Laser laser : lasers) {
            laser.setCullable(true);
        }

        for (PowerUp powerUp : powerUps) {
            powerUp.setCullable(true);
        }

        for (Explosion explosion : explosions) {
            explosion.setCullable(true);
        }

        laserPowerUpActive = false;
        laserPowerUpFrames = 0;

        levelNumber++;
        setDifficulty();

        setUpEnemyShips();
        timeSinceLastShot = TimeUtils.nanoTime();

        timeStartedShowingWaveNumber = TimeUtils.millis();
        showingWaveNumber = true;
        if (levelNumber == 2) {
            googlePlayServices.unlockAchievement("achievement_first_wave");
        } else if (levelNumber == 6) {
            googlePlayServices.unlockAchievement("achievement_fifth_wave");
        } else if (levelNumber == 8) {
            googlePlayServices.unlockAchievement("achievement_7th_wave");
        }
    }

    public void playerDied() {
        if (playerShip.lives > 0) {
            timeStartedShowingWaveNumber = TimeUtils.millis();
            showingWaveNumber = true;
            playerExploding = false;
        }
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
        } else if (levelNumber < 20) {
            difficulty = 4;
        } else if (levelNumber >= 24) {
            difficulty = 5;
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
        } else if (difficulty == 3) {
            addShips("red", 0.0f);
            addShips("green", 1.5f);
            addShips("blue", 3.0f);
        } else if (difficulty == 4) {
            addShips("red", 0.0f);
            addShips("red", 1.5f);
            addShips("green", 3.0f);
        } else if (difficulty >= 5) {
            addShips("red", 0.0f);
            addShips("red", 1.5f);
            addShips("red", 3.0f);
        }
    }

    private void shootEnemyLaser(EnemyShip ship) {
        if (!ship.isFalling()) {
            Laser laser = laserPool.obtain();
            laser.init("green", ship.getCentre(), new Vector2(0, -9));
            lasers.add(laser);
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Sprites flip: " + laser.isFlipX() + " : " + laser.isFlipY());
            }
            if (preferences.sfx) {
                long enemyPewID = enemyPew.play(preferences.sfxVolume);
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Pew ID: " + enemyPewID);
                }
            }
        }
    }

//methods end


}
