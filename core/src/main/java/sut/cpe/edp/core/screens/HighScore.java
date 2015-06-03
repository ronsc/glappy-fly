package sut.cpe.edp.core.screens;

import playn.core.*;
import playn.core.util.Callback;
import sut.cpe.edp.core.assets.GameContext;
import sut.cpe.edp.core.assets.LoadImage;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

        if(getHighScore() == -1){
            Layer textScore = createTextLayer(String.valueOf(0), 0xffFFFFFF);
            textScore.setTranslation(width() / 2f, height() / 2f);
            base.add(textScore);
        } else {
            Layer textScore = createTextLayer(String.valueOf(getHighScore()), 0xffFFFFFF);
            textScore.setTranslation(width() / 2f, height() / 2f);
            base.add(textScore);
        }

        Layer btnClose = graphics().createImageLayer(loadImage.btnClose);
        btnClose.setTranslation(width() - 100, 10);
        base.add(btnClose);

        btnClose.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
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

    public int getHighScore() {
        String path = "score.txt";
        File file = new File(path);
        int score = -1;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                score = Integer.valueOf(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return score;
    }
}
