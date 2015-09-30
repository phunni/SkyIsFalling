package uk.co.redfruit.gdx.skyisfalling.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.TimeUtils;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.game.objects.EnemyShip;
import uk.co.redfruit.gdx.skyisfalling.game.objects.PlayerShip;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class Level {

    private static final String TAG = "Level";

    private PlayerShip playerShip;
    private Array<EnemyShip> enemyShips = new Array<EnemyShip>();

    private final World world;

    private Sprite background = Assets.getInstance().getBackground();

    private float difficulty;
    private float levelNumber;
    private long startTime;
    private float score;

    private Pool<EnemyShip> enemyShipPool;


    public Level(World newWorld) {
        this.world = newWorld;
        enemyShipPool = new Pool<EnemyShip>() {
            @Override
            protected EnemyShip newObject() {
                return new EnemyShip(world);
            }
        };
        levelNumber = 1;
        setDifficulty();
        playerShip = new PlayerShip(newWorld);
        setUpEnemyShips();
        startTime = TimeUtils.nanoTime();

        background.setBounds(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        background.setScale(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }

    private void setDifficulty() {
        if (levelNumber < 5) {
            difficulty = 0;
        } else if (levelNumber < 10) {
            difficulty = 1;
        } else if (levelNumber < 15) {
            difficulty = 2;
        } else if (levelNumber < 20) {
            difficulty = 3;
        }
    }

    public void render(SpriteBatch batch) {
        boolean moving = false;
        batch.disableBlending();
        background.draw(batch);
        batch.enableBlending();
        playerShip.render(batch);
        if (TimeUtils.timeSinceNanos(startTime) > 1400000000) {
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
        if (moving) {
            startTime = TimeUtils.nanoTime();
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



       world.step(deltaTime, 6, 2);
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
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
            enemy.init(world, colour, new Vector2(0.5f + (2.5f * i), Constants.WORLD_HEIGHT - height));
            /*if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Body is awake: " + enemy.isAWake());
            }*/
            enemyShips.add(enemy);
        }
    }
}
