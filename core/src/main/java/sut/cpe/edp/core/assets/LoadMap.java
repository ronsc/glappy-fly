package sut.cpe.edp.core.assets;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.ImageLayer;
import playn.core.Layer;

import java.util.Random;

public class LoadMap {

    private World world;

    public LoadMap(World world) {
        this.world = world;
    }

    public Body[] getBody() {
        Body b[][] = new Body[3][3];
        Random ran = new Random();
        int index = ran.nextInt(3);

        System.out.println("random : " + index);

        switch (index) {
            case 0:
                b[0][0] = initPhysicsBox(640, 75, 20, 75);
                b[0][1] = initPhysicsBoxSensor(640, 210, 20, 60);
                b[0][2] = initPhysicsBox(640, 345, 20, 75);
                break;
            case 1:
                b[1][0] = initPhysicsBox(640, 50, 20, 50);
                b[1][1] = initPhysicsBoxSensor(640, 160, 20, 60);
                b[1][2] = initPhysicsBox(640, 320, 20, 100);
                break;
            case 2:
                b[2][0] = initPhysicsBox(640, 100, 20, 100);
                b[2][1] = initPhysicsBoxSensor(640, 260, 20, 60);
                b[2][2] = initPhysicsBox(640, 370, 20, 50);
                break;
        }

        return b[index];
    }

    public Layer[] getLayer() {
        Layer[] layer = new Layer[2];

        return layer;
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
