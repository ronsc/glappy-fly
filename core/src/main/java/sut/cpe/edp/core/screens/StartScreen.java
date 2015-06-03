package sut.cpe.edp.core.screens;

import static playn.core.PlayN.*;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import playn.core.*;
import playn.core.util.Clock;
import sut.cpe.edp.core.assets.GameContext;
import sut.cpe.edp.core.assets.LoadImage;
import sut.cpe.edp.core.assets.LoadWorld;
import sut.cpe.edp.core.characters.Golang;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

public class StartScreen extends Screen {

    private ScreenStack ss;
    private GameContext gameContext;
    private static LoadImage loadImage;

    private World world;
    private boolean showDebugDraw = false;
    private DebugDrawBox2D debugDraw;

    private LoadWorld loadWorld;
    private Golang g;

    private Layer bgStripeLayer1, bgStripeLayer2;

    public StartScreen(ScreenStack ss, GameContext gameContext, LoadImage loadImage) {
        this.ss = ss;
        this.gameContext = gameContext;
        this.loadImage = loadImage;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        // setup world and ground
        loadWorld = gameContext.getLoadWorld();
        world = loadWorld.getWorld();

        // background image
        ImageLayer imgLayer = graphics().createImageLayer(loadImage.bgStartScreen);
        this.layer.add(imgLayer);

        // ground image
        bgStripeLayer1 = graphics().createImageLayer(loadImage.bgStripe);
        bgStripeLayer2 = graphics().createImageLayer(loadImage.bgStripe);
        bgStripeLayer1.setTranslation(0, this.height() - 58);
        bgStripeLayer2.setTranslation(640, this.height() - 58);
        this.layer.add(bgStripeLayer1);
        this.layer.add(bgStripeLayer2);

        // init character golang
        g = new Golang(world, 150, 200);
        g.setHasStart(false);
        this.layer.add(g.layer());

        ImageLayer btnStartGame = graphics().createImageLayer(loadImage.btnStartGame);
        this.layer.add(btnStartGame.setTranslation(width() / 2f - 50, height() / 2f - 80));

        ImageLayer btnHighSocre = graphics().createImageLayer(loadImage.btnHighScore);
        this.layer.add(btnHighSocre.setTranslation(width() / 2f - 50, height() / 2f));

        btnStartGame.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gameContext.getLoadWorld().getWorld().destroyBody(g.body());
                ss.remove(ss.top());
                ss.push(new GamePlay(ss, gameContext, loadImage));
            }
        });

        btnHighSocre.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gameContext.getLoadWorld().getWorld().destroyBody(g.body());
                ss.remove(ss.top());
                ss.push(new HighScore(ss, gameContext, loadImage));
            }
        });

        // call show debug draw
        showdebugdraw();
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        world.step(0.033f, 10, 10);

        g.update(delta);

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
