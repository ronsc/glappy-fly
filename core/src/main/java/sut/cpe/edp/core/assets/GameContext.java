package sut.cpe.edp.core.assets;

public class GameContext {

    public static final float SPEED = 0.05f;
    public static final int PIPE_TIME = 2500;
    private int score = 0;

    private LoadWorld loadWorld;

    public GameContext() {
        this.loadWorld = new LoadWorld();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public LoadWorld getLoadWorld() {
        return loadWorld;
    }
}
