package uk.co.redfruit.gdx.skyisfalling.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import aurelienribon.bodyeditor.BodyEditorLoader;

public abstract class GameObject {

    protected static BodyEditorLoader loader;
    protected Vector2 position;
    protected Vector2 origin;
    protected Body body;
    protected World world;
    protected BodyDef defaultDynamicBodyDef;

    protected boolean cullable;

    public GameObject(World world) {
        position = new Vector2();
        origin = new Vector2();
        this.world = world;
        loader = new BodyEditorLoader(Gdx.files.internal("box2d/sky_is_falling.json"));
        defaultDynamicBodyDef = new BodyDef();
        defaultDynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        defaultDynamicBodyDef.position.set(position.x, position.y);
    }

    public GameObject(){
       this(new World(new Vector2(0, -9.8f), true));
    }

    public void update(float deltaTime) {
        position.set(body.getPosition());
    }

    public abstract void render(SpriteBatch batch);

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public boolean isCullable() {
        return cullable;
    }

    public void setCullable(boolean cullable) {
        this.cullable = cullable;
    }

    public Body getBody() {
        return body;
    }
}
