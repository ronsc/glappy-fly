package sut.cpe.edp.core.screens;

import playn.core.*;
import playn.core.util.Callback;
import sut.cpe.edp.core.assets.GameContext;
import sut.cpe.edp.core.assets.LoadImage;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.json;

public class HighScore extends Screen {

    private ScreenStack ss;
    private GameContext gameContext;
    private static LoadImage loadImage;

    private GroupLayer base;

    public HighScore(ScreenStack ss, GameContext gameContext, LoadImage loadImage) {
        this.ss = ss;
        this.gameContext = gameContext;
        this.loadImage = loadImage;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        base = graphics().createGroupLayer();
        base.add(graphics().createImageLayer(loadImage.bgHighScore));

        assets().getText("textfile/score.txt", new Callback<String>() {
            @Override
            public void onSuccess(String result) {
                Layer textScore = createTextLayer(result, 0xffFFFFFF);
                textScore.setTranslation(width() / 2f, height() / 2f);
                layer.add(textScore);
                System.out.println(result);
            }

            @Override
            public void onFailure(Throwable cause) {
                cause.printStackTrace();
            }
        });

        Layer btnClose = graphics().createImageLayer(loadImage.btnClose);
        btnClose.setTranslation(width() - 100, 10);
        base.add(btnClose);

        btnClose.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                System.out.println("OK");
                ss.remove(ss.top());
                ss.push(new StartScreen(ss, gameContext, loadImage));
            }
        });

        this.layer.add(base);
    }

    static Layer createTextLayer(String text, Integer color) {
        Font font = graphics().createFont("Impact", Font.Style.PLAIN, 48);
        TextLayout layout = graphics().layoutText(text, new TextFormat().withFont(font));
        CanvasImage image = graphics().createImage(
                (int) Math.ceil(layout.width()) + 8,
                (int) Math.ceil(layout.height()) + 8
        );

        image.canvas().setFillColor(color);
        image.canvas().setStrokeWidth(4);
        image.canvas().setStrokeColor(0xFF000000);
        image.canvas().strokeText(layout, 0, 0);
        image.canvas().fillText(layout, 0, 0);

        return graphics().createImageLayer(image).setTranslation(320 - layout.width()/2f, 130);
    }
}
