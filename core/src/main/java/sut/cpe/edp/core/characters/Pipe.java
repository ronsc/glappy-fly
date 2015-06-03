package sut.cpe.edp.core.characters;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.ImageLayer;
import sut.cpe.edp.core.assets.LoadImage;
import sut.cpe.edp.core.assets.LoadWorld;

import static playn.core.PlayN.graphics;

public class Pipe {
    private Body pipeBody;

    private World world;
    private static LoadImage loadImage;

    public Pipe(World world, LoadImage loadImage) {
        this.world = world;
        this.loadImage = loadImage;
    }

    public Body[] getBody(int index) {
        Body b[][] = new Body[3][3];

        switch (index) {
            case 0:
                b[0][0] = initPhysicsBox(680, 75, 20, 75);
                b[0][1] = initPhysicsBoxSensor(700, 210, 0, 60);
                b[0][2] = initPhysicsBox(680, 345, 20, 75);
                break;
            case 1:
                b[1][0] = initPhysicsBox(680, 50, 20, 50);
                b[1][1] = initPhysicsBoxSensor(700, 160, 0, 60);
                b[1][2] = initPhysicsBox(680, 320, 20, 100);
                break;
            case 2:
                b[2][0] = initPhysicsBox(680, 100, 20, 100);
                b[2][1] = initPhysicsBoxSensor(700, 260, 0, 60);
                b[2][2] = initPhysicsBox(680, 370, 20, 50);
                break;
        }

        return b[index];
    }

    public ImageLayer[] getImageLayer(int index) {
        ImageLayer[][] imgLayer = new ImageLayer[3][2];

        imgLayer[index][0] = graphics().createImageLayer(loadImage.imgPipe[index][0]);
        imgLayer[index][1] = graphics().createImageLayer(loadImage.imgPipe[index][1]);

        return imgLayer[index];
    }

    public Body initPhysicsBox(float x, float y, float w, float h) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        Body box = world.createBody(bodyDef);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(w * LoadWorld.M_PER_PIXEL, h * LoadWorld.M_PER_PIXEL);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = 0.0f;
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
