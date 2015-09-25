package uk.co.redfruit.gdx.skyisfalling.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class WorldController implements Disposable {

    protected Level level;

    private Game game;

    private int lives;
    private int score;

    //Box2D
    private World world;


    public WorldController(Game game) {
        this.game = game;
        initPhysics();
        level = new Level(world);

    }

    public void update(float deltaTime) {
        level.update(deltaTime);
    }

    @Override
    public void dispose() {
        if (world != null) {
            world.dispose();
        }
    }

    public Level getLevel() {
        return level;
    }

    public World getWorld() {
        return world;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    private void initPhysics() {
        if (world != null) {
            world.dispose();
        }
        world = new World(new Vector2(0, -9.81f), true);
        createGround();
        createSideWalls();
    }

    private void createGround() {
        float halfGroundWidth = Constants.WORLD_WIDTH;
        float halfGroundHeight = 0.0f;
        Body groundBody;

        // Create a static body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        // Set the ground position
        groundBodyDef.position.set(halfGroundWidth * 0.5f, halfGroundHeight);

        // Create a body from the definition and add it to the world
        groundBody = world.createBody(groundBodyDef);

        // Create a rectangle shape which will fit the world_width and 1 meter high
        // (setAsBox takes half-width and half-height as arguments)
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(halfGroundWidth * 0.5f, halfGroundHeight);
        // Create a fixture from our rectangle shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f);
        // Free resources
        groundBox.dispose();
    }

    private void createSideWalls() {
        float halfHeight = Constants.WORLD_HEIGHT;
        float halfWidth = 0.05f;

        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.StaticBody;

        wallsDef.position.set(halfWidth, halfHeight);

        Body leftWall = world.createBody(wallsDef);

        wallsDef.position.set(Constants.WORLD_WIDTH, halfHeight);

        Body rightWall = world.createBody(wallsDef);
        PolygonShape wallBox = new PolygonShape();
        wallBox.setAsBox(halfWidth, halfHeight);

        leftWall.createFixture(wallBox, 0.0f);
        rightWall.createFixture(wallBox, 0.0f);

        wallBox.dispose();
    }

}
