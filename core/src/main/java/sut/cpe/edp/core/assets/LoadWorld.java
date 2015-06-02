package sut.cpe.edp.core.assets;


import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class LoadWorld {

    public static float M_PER_PIXEL = 1 / 26.666667f;

    // size of world
    public static int width = 24;
    public static int height = 18;

    private World world;
    private Body ground;

    public LoadWorld() {
        setUpWorld();
        setUpTop();
        setUpGround();
    }

    public World getWorld() {
        return world;
    }

    public Body getGround() {
        return this.ground;
    }

    public void setUpWorld() {
        Vec2 gravity = new Vec2(0.0f, 10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
    }

    public void setUpGround() {
        ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, height - 2.4f), new Vec2(width, height - 2.4f));
        ground.createFixture(groundShape, 0.0f);
    }

    public void setUpTop() {
        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, 0), new Vec2(width, 0));
        ground.createFixture(groundShape, 0.0f);
    }
}
