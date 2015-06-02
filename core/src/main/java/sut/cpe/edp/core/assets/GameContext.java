package sut.cpe.edp.core.assets;

public class GameContext {

    public static final float SPEED = 0.05f;
    public static final int PIPE_TIME = 2500;
    private int score = 0;

    private LoadWorld loadWorld;
    private LoadImage loadImage;

    public GameContext() {
        this.loadWorld = new LoadWorld();
        this.loadImage = new LoadImage();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public LoadImage getLoadImage() {
        return loadImage;
    }

    public LoadWorld getLoadWorld() {
        return loadWorld;
    }
}
