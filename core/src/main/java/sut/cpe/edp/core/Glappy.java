package sut.cpe.edp.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.util.Clock;
import sut.cpe.edp.core.assets.GameContext;
import sut.cpe.edp.core.assets.LoadImage;
import sut.cpe.edp.core.screens.HighScore;
import sut.cpe.edp.core.screens.StartScreen;
import tripleplay.game.ScreenStack;

public class Glappy extends Game.Default {

    public static final int UPDATE_RATE = 25;
    private final ScreenStack ss = new ScreenStack();
    protected final Clock.Source clock = new Clock.Source(UPDATE_RATE);

    public Glappy() {
        super(UPDATE_RATE);
    }

    @Override
    public void init() {
        ss.push(new StartScreen(ss, new GameContext(), new LoadImage()));
    }

    @Override
    public void update(int delta) {
        ss.update(delta);
    }

    @Override
    public void paint(float alpha) {
        clock.paint(alpha);
        ss.paint(clock);
    }
}
