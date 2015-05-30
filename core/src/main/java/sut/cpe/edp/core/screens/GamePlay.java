package sut.cpe.edp.core.screens;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.*;
import playn.core.util.Clock;
import sut.cpe.edp.core.assets.LoadImage;
import sut.cpe.edp.core.assets.LoadWorld;
import sut.cpe.edp.core.characters.Golang;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.graphics;

public class GamePlay extends Screen {

    private ScreenStack ss;
    private static LoadImage loadImage;

    private World world;
    private boolean showDebugDraw = true;
    private DebugDrawBox2D debugDraw;

    private LoadWorld loadWorld;
    private Golang g;

    private Layer bgStripeLayer1, bgStripeLayer2;

    public GamePlay(ScreenStack ss, LoadImage loadImage) {
        this.ss = ss;
        this.loadImage = loadImage;
    }

    @Override
    public void wasShown() {
        super.wasShown();
        System.out.println("SCREEN : GamePlay");

        // setup world and ground
        loadWorld = new LoadWorld();
        world = loadWorld.getWorld();
        // background image
        ImageLayer imgLayer = graphics().createImageLayer(loadImage.bgStartScreen);
        //this.layer.add(imgLayer);
        // ground image
        bgStripeLayer1 = graphics().createImageLayer(loadImage.bgStripe);
        bgStripeLayer2 = graphics().createImageLayer(loadImage.bgStripe);
        bgStripeLayer1.setTranslation(0, this.height() - 65);
        bgStripeLayer2.setTranslation(640, this.height() - 65);
        this.layer.add(bgStripeLayer1);
        this.layer.add(bgStripeLayer2);

        Layer text = createTextLayer("0", 0xffFFFFFF);
        text.setTranslation(width() / 2f, 50);
        this.layer.add(text);

        g = new Golang(world, 150, 200);
        g.setHasStart(true);
        this.layer.add(g.layer());


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

    static Layer createTextLayer(String text, Integer color) {
        Font font = graphics().createFont("Helvetica", Font.Style.PLAIN, 36);
        TextLayout layout = graphics().layoutText(text, new TextFormat().withFont(font));
        CanvasImage image = graphics().createImage(
                (int) Math.ceil(layout.width()),
                (int) Math.ceil(layout.height())
        );
        image.canvas().setStrokeWidth(2);
        image.canvas().setStrokeColor(0xFF0000eb);
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
