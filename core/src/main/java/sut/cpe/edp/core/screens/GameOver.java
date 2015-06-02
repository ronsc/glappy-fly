package sut.cpe.edp.core.screens;

import playn.core.*;
import sut.cpe.edp.core.assets.GameContext;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.graphics;

public class GameOver extends Screen {

    private ScreenStack ss;
    private GameContext gameContext;

    public GameOver(ScreenStack ss, GameContext gameContext) {
        this.ss = ss;
        this.gameContext = gameContext;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        Layer textOver = createTextLayer("GameOver", 0xffFFFFFF);
        this.layer.add(textOver);

        Layer textYourScore = createTextLayer("Your Score", 0xffFFFFFF);
        textYourScore.setTranslation(textYourScore.tx(), textOver.ty() + 70f);
        this.layer.add(textYourScore);

        Layer textScore = createTextLayer(Integer.toString(gameContext.getScore()), 0xffFFFFFF);
        textScore.setTranslation(textScore.tx(), textYourScore.ty() + 70f);
        this.layer.add(textScore);
    }

    @Override
    public void wasAdded() {
        // event onclick to start game
        PlayN.pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gameContext = null;
                ss.remove(ss.top());
                ss.push(new StartScreen(ss, new GameContext()));
            }
        });
    }

    static Layer createTextLayer(String text, Integer color) {
        Font font = graphics().createFont("Impact", Font.Style.PLAIN, 38);
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

        return graphics().createImageLayer(image).setTranslation(320 - layout.width()/2f, 100);
    }
}
