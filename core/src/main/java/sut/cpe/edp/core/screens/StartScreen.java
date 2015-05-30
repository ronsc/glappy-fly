package sut.cpe.edp.core.screens;

import static playn.core.PlayN.*;

import playn.core.ImageLayer;
import playn.core.util.Clock;
import sut.cpe.edp.core.characters.Golang;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

public class StartScreen extends Screen {

    private ScreenStack ss;

    private Golang g;

    public StartScreen(ScreenStack ss) {
        this.ss = ss;
    }

    @Override
    public void wasShown() {
        super.wasShown();
        ImageLayer imgLayer = graphics().createImageLayer(assets().getImage("images/bg_startgame.png"));
        this.layer.add(imgLayer);

        g = new Golang(200, 200);
        this.layer.add(g.layer());
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        g.update(delta);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
    }
}
