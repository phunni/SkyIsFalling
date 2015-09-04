package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import io.piotrjastrzebski.playground.box2dtest.Box2dLoader;

public abstract class GameObject {

    protected Vector2 position;
    protected Vector2 origin;

    protected Body body;

    protected World world;

    protected static Box2dLoader loader;
    protected BodyDef defaultDynamicBodyDef;

    public GameObject(World world) {
        position = new Vector2();
        origin = new Vector2();
        this.world = world;
        loader = new Box2dLoader(Gdx.files.internal("box2d/sky_is_falling.json"));
        defaultDynamicBodyDef = new BodyDef();
        defaultDynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        defaultDynamicBodyDef.position.set(position.x, position.y);
    }

    public void update(float deltaTime) {
        position.set(body.getPosition());
    }

    public abstract void render(SpriteBatch batch);

}
