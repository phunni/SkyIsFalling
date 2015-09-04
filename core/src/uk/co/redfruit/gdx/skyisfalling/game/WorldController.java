package uk.co.redfruit.gdx.skyisfalling.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
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
    }

    private Body createGround() {
        float halfWidth = Constants.VIEWPORT_WIDTH / 2f - 0.5f;
        float halfHeight = Constants.VIEWPORT_HEIGHT / 2f - 0.5f;
        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(new float[] {-halfWidth, -halfHeight, halfWidth, -halfHeight,
                halfWidth, halfHeight, -halfWidth, halfHeight});
        BodyDef chainBodyDef = new BodyDef();
        chainBodyDef.type = BodyDef.BodyType.StaticBody;
        Body ground= world.createBody(chainBodyDef);
        ground.createFixture(chainShape, 0);
        chainShape.dispose();
        return ground;
    }

}
