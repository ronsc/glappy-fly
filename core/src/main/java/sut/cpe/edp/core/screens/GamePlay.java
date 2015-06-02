package sut.cpe.edp.core.screens;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import sut.cpe.edp.core.assets.GameContext;
import sut.cpe.edp.core.assets.LoadImage;
import sut.cpe.edp.core.assets.LoadWorld;
import sut.cpe.edp.core.characters.Golang;
import sut.cpe.edp.core.characters.Pipe;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static playn.core.PlayN.graphics;

public class GamePlay extends Screen {

    private ScreenStack ss;
    private static LoadImage loadImage;

    private World world;
    private boolean showDebugDraw = true;
    private DebugDrawBox2D debugDraw;

    private LoadWorld loadWorld;
    private Golang g;
    private Pipe p;

    private Layer bgStripeLayer1, bgStripeLayer2;
    private Layer scoreLayer;
    private int t=0, score=0;

    Body b[] = new Body[3];
    HashMap<Body[], ImageLayer[]> pipeMap = new HashMap<Body[], ImageLayer[]>();

    private GameContext gameContext;

    public GamePlay(ScreenStack ss, GameContext gameContext) {
        this.ss = ss;
        this.gameContext = gameContext;

        this.loadImage = gameContext.getLoadImage();
    }

    @Override
    public void wasShown() {
        super.wasShown();

        // setup world and ground
        loadWorld = gameContext.getLoadWorld();
        world = loadWorld.getWorld();

        // background image
        ImageLayer imgLayer = graphics().createImageLayer(loadImage.bgGamePlay);
        this.layer.add(imgLayer);

        // ground image
        bgStripeLayer1 = graphics().createImageLayer(loadImage.bgStripe);
        bgStripeLayer2 = graphics().createImageLayer(loadImage.bgStripe);
        bgStripeLayer1.setTranslation(0, this.height() - 58);
        bgStripeLayer2.setTranslation(640, this.height() - 58);
        this.layer.add(bgStripeLayer1);
        this.layer.add(bgStripeLayer2);

        // init character golang
        g = new Golang(world, 100, 200);
        g.setHasStart(true);
        this.layer.add(g.layer());

        // init score
        updateScore();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(contact.getFixtureA().getBody() == g.body() || contact.getFixtureB().getBody() == g.body()) {
                    System.out.println("conn");

                    for(Body[] pipeBody : pipeMap.keySet()) {
                        if(contact.getFixtureA().getBody() == pipeBody[1] || contact.getFixtureB().getBody() == pipeBody[1]) {
                            gameContext.setScore(gameContext.getScore() + 1);
                            updateScore();
                        }

                        if(contact.getFixtureA().getBody() == pipeBody[0]
                                || contact.getFixtureB().getBody() == pipeBody[0]
                                || contact.getFixtureA().getBody() == pipeBody[2]
                                || contact.getFixtureB().getBody() == pipeBody[2]){

                            System.out.println("GameOver");

                            ss.remove(ss.top());
                            ss.push(new GameOver(ss, gameContext));
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        // call show debug draw
        showdebugdraw();
    }

    @Override
    public void wasAdded() {
        PlayN.pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) { }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        world.step(0.033f, 10, 10);

        g.update(delta);

        t += delta;
        if(t > gameContext.PIPE_TIME) {
            Random ran = new Random();
            int index = ran.nextInt(3);
            System.out.println("random : " + index);

            Pipe p = new Pipe(world, loadImage);
            Body[] body = p.getBody(index);
            ImageLayer[] imgLayer = p.getImageLayer(index);
            pipeMap.put(body, imgLayer);

            this.layer.add(imgLayer[0]);
            this.layer.add(imgLayer[1]);

            t = 0;
        }

        if(bgStripeLayer1.tx() == 0) {
            bgStripeLayer2.setTranslation(this.width(), bgStripeLayer2.ty());
        }
        if(bgStripeLayer2.tx() == 0) {
            bgStripeLayer1.setTranslation(this.width(), bgStripeLayer1.ty());
        }
        bgStripeLayer1.setTranslation(bgStripeLayer1.tx() - 2, bgStripeLayer1.ty());
        bgStripeLayer2.setTranslation(bgStripeLayer2.tx() - 2, bgStripeLayer2.ty());

    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);

        g.paint(clock);

        if(showDebugDraw) {
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }

        for(Map.Entry<Body[], ImageLayer[]> pipe : pipeMap.entrySet()){
            for(Body body : pipe.getKey()) {
                body.setTransform(new Vec2(
                        body.getPosition().x - gameContext.SPEED,
                        body.getPosition().y
                ),0);
            }

            pipe.getValue()[0].setTranslation(
                    pipe.getKey()[0].getPosition().x / LoadWorld.M_PER_PIXEL - 20,
                    pipe.getKey()[0].getPosition().y / LoadWorld.M_PER_PIXEL - pipe.getValue()[0].height()/2f
            );

            pipe.getValue()[1].setTranslation(
                    pipe.getKey()[2].getPosition().x / LoadWorld.M_PER_PIXEL - 20,
                    pipe.getKey()[2].getPosition().y / LoadWorld.M_PER_PIXEL - pipe.getValue()[1].height()/2f
            );
        }
    }

    public void updateScore(){
        if(scoreLayer != null)
            scoreLayer.destroy();

        scoreLayer = createTextLayer(gameContext.getScore(), 0xffFFFFFF);
        this.layer.add(scoreLayer);
    }

    static Layer createTextLayer(int score, Integer color) {
        Font font = graphics().createFont("Impact", Font.Style.PLAIN, 38);
        TextLayout layout = graphics().layoutText(String.valueOf(score), new TextFormat().withFont(font));
        CanvasImage image = graphics().createImage(
                (int) Math.ceil(layout.width()) + 8,
                (int) Math.ceil(layout.height()) + 8
        );

        image.canvas().setFillColor(color);
        image.canvas().setStrokeWidth(4);
        image.canvas().setStrokeColor(0xFF000000);
        image.canvas().strokeText(layout, 0, 0);
        image.canvas().fillText(layout, 0, 0);

        return graphics().createImageLayer(image).setTranslation(320 - layout.width()/2f, 50);
    }

    public void showdebugdraw() {
        if(showDebugDraw) {
            CanvasImage image = graphics().createImage(
                    (int) (loadWorld.width / loadWorld.M_PER_PIXEL),
                    (int) (loadWorld.height / loadWorld.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 1f / loadWorld.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }
    }
}
