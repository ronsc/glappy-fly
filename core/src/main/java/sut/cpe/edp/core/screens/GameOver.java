package sut.cpe.edp.core.screens;

import playn.core.*;
import playn.core.util.Callback;
import sut.cpe.edp.core.assets.GameContext;
import sut.cpe.edp.core.assets.LoadImage;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.io.*;
import java.util.Date;

import static playn.core.PlayN.*;

public class GameOver extends Screen {

    private ScreenStack ss;
    private GameContext gameContext;
    private LoadImage loadImage;

    public GameOver(ScreenStack ss, GameContext gameContext, LoadImage loadImage) {
        this.ss = ss;
        this.gameContext = gameContext;
        this.loadImage = loadImage;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        if(gameContext.getScore() > getOldScore() || getOldScore() == -1)
            WriteToTextFile();

        this.layer.add(graphics().createImageLayer(loadImage.bgStartScreen));

        Layer textOver = createTextLayer("GameOver", 0xffFFFFFF);
        this.layer.add(textOver);

        Layer textYourScore = createTextLayer("Your Score   " + Integer.toString(gameContext.getScore()), 0xffFFFFFF);
        textYourScore.setTranslation(textYourScore.tx(), textOver.ty() + 70f);
        this.layer.add(textYourScore);

        ImageLayer btnStartGame = graphics().createImageLayer(loadImage.btnStartGame);
        this.layer.add(btnStartGame.setTranslation(width() / 2f - 50, textYourScore.ty() + 70f));

        btnStartGame.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gameContext = null;
                ss.remove(ss.top());
                ss.push(new StartScreen(ss, new GameContext(), loadImage));
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

        return graphics().createImageLayer(image).setTranslation(320 - layout.width()/2f, 130);
    }

    public void WriteToTextFile() {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = "score.txt";
            File logFile = new File(timeLog);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
//            writer.write(new Date().toLocaleString());
//            writer.newLine();
            writer.write(Integer.toString(gameContext.getScore()));
//            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    public int getOldScore() {
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
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return score;
    }
}
