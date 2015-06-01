package sut.cpe.edp.core.screens;

import static playn.core.PlayN.*;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import playn.core.*;
import playn.core.util.Clock;
import sut.cpe.edp.core.assets.LoadImage;
import sut.cpe.edp.core.assets.LoadWorld;
import sut.cpe.edp.core.characters.Golang;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

public class StartScreen extends Screen {

    private ScreenStack ss;
    private static LoadImage loadImage;

    private World world;
    private boolean showDebugDraw = false;
    private DebugDrawBox2D debugDraw;

    private LoadWorld loadWorld;
    private Golang g;

    private Layer bgStripeLayer1, bgStripeLayer2;

    public StartScreen(ScreenStack ss, LoadImage loadImage) {
        this.ss = ss;
        this.loadImage = loadImage;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        // setup world and ground
        loadWorld = new LoadWorld();
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

        // text to start
        Layer text = createTextLayer("Click to StartGame", 0xff000000);
        text.setTranslation(width() / 2f - 120, height() / 2f - 30);
        this.layer.add(text);

        // auto go to gameplay
        //ss.push(new GamePlay(ss, loadImage));

        // call show debug draw
        showdebugdraw();
    }

    @Override
    public void wasAdded() {
        // event onclick to start game
        PlayN.pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.push(new GamePlay(ss, loadImage));
            }
        });
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

    static Layer createTextLayer(String text, Integer color) {
        Font font = graphics().createFont("Helvetica", Font.Style.PLAIN, 24);
        TextLayout layout = graphics().layoutText(text, new TextFormat().withFont(font));
        CanvasImage image = graphics().createImage(
                (int) Math.ceil(layout.width()),
                (int) Math.ceil(layout.height())
        );
        image.canvas().setFillColor(color);
        image.canvas().fillText(layout, 0, 0);

        return graphics().createImageLayer(image);
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
