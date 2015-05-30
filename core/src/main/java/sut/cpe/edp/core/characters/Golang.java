package sut.cpe.edp.core.characters;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.cpe.edp.core.assets.LoadWorld;
import sut.cpe.edp.core.sprites.Sprite;
import sut.cpe.edp.core.sprites.SpriteLoader;

public class Golang {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false, hasStart = false;

    public enum State { IDLE };

    private int e = 0, offset = 0;

    private Body golangBody;

    public Golang(final World world, final float x, final float y) {
        sprite = SpriteLoader.getSprite("json/golang.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
                sprite.layer().setTranslation(x, y);
                hasLoaded = true;

                golangBody = initPhysicsBox(world, x, y, sprite.width()/2f, sprite.height()/2f);

                PlayN.keyboard().setListener(new Keyboard.Adapter() {
                    @Override
                    public void onKeyUp(Keyboard.Event event) {
                        if (event.key().equals(Key.SPACE)) {
                            if(hasStart) {
                                golangBody.applyForce(new Vec2(0, -150), golangBody.getPosition());
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }

    public void update(int delta) {
        if(!hasLoaded) return;
        e += delta;

        if(e > 150) {
            spriteIndex = offset + ((spriteIndex + 1) % 2);
            sprite.setSprite(spriteIndex);
            e = 0;
        }

        if(golangBody.getPosition().y > 9 && !hasStart) {
            golangBody.applyForce(new Vec2(0, -40), golangBody.getPosition());
        }
    }

    public void paint(Clock clock){
        if(!hasLoaded) return;
//        sprite.layer().setTranslation(
//                golangBody.getPosition().x / LoadWorld.M_PER_PIXEL,
//                golangBody.getPosition().y / LoadWorld.M_PER_PIXEL
//        );
    }

    public void setHasStart(boolean hasStart) {
        this.hasStart = hasStart;
    }

    public Layer layer() {
        return sprite.layer();
    }

    public Body initPhysicsBox(World world, float x, float y, float w, float h) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;

        Body box = world.createBody(bodyDef);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(w * LoadWorld.M_PER_PIXEL, h * LoadWorld.M_PER_PIXEL);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.5f;
        box.createFixture(fixtureDef);

        box.setTransform(new Vec2(
                x * LoadWorld.M_PER_PIXEL,
                y * LoadWorld.M_PER_PIXEL
        ), 0f);

        return box;
    }
}
