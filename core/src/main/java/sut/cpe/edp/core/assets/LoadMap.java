package sut.cpe.edp.core.assets;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class LoadMap {

    private World world;

    public LoadMap(World world) {
        this.world = world;
    }

    public Body initPhysicsBox(float x, float y, float w, float h) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        Body box = world.createBody(bodyDef);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(w * LoadWorld.M_PER_PIXEL, h * LoadWorld.M_PER_PIXEL);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.2f;
        box.createFixture(fixtureDef);

        box.setTransform(new Vec2(
                x * LoadWorld.M_PER_PIXEL,
                y * LoadWorld.M_PER_PIXEL
        ), 0f);

        return box;
    }

    public Body initPhysicsBoxSensor(float x, float y, float w, float h) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        Body box = world.createBody(bodyDef);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(w * LoadWorld.M_PER_PIXEL, h * LoadWorld.M_PER_PIXEL);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.2f;
        fixtureDef.isSensor = true;
        box.createFixture(fixtureDef);

        box.setTransform(new Vec2(
                x * LoadWorld.M_PER_PIXEL,
                y * LoadWorld.M_PER_PIXEL
        ), 0f);

        return box;
    }
}
