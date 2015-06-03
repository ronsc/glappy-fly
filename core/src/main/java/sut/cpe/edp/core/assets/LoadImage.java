package sut.cpe.edp.core.assets;

import static playn.core.PlayN.*;

import playn.core.Image;

public class LoadImage {
    public Image bgStartScreen = assets().getImage("images/bg_startgame.png");
    public Image bgGamePlay = assets().getImage("images/bg_gameplay.png");
    public Image bgStripe = assets().getImage("images/bg_stripe2.png");
    public Image bgHighScore = assets().getImage("images/bg_gamescore.png");

    public Image btnStartGame = assets().getImage("images/btn/btn_startgame.png");
    public Image btnHighScore = assets().getImage("images/btn/btn_highscore.png");
    public Image btnClose = assets().getImage("images/btn/btn_close.png");

    public Image[][] imgPipe = new Image[][]{
            {
                    assets().getImage("images/pipe/one_top.png"),
                    assets().getImage("images/pipe/one_bot.png")
            },
            {
                    assets().getImage("images/pipe/two_top.png"),
                    assets().getImage("images/pipe/two_bot.png")
            },
            {
                    assets().getImage("images/pipe/three_top.png"),
                    assets().getImage("images/pipe/three_bot.png")
            }
    };
}
