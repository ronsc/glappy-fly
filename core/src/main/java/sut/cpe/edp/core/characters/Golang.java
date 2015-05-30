package sut.cpe.edp.core.characters;

import playn.core.Layer;
import playn.core.util.Callback;
import sut.cpe.edp.core.sprites.Sprite;
import sut.cpe.edp.core.sprites.SpriteLoader;

public class Golang {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;

    public enum State { IDLE };

    private int e = 0, offset = 0;

    public Golang(final float x, final float y) {
        sprite = SpriteLoader.getSprite("json/golang.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width(), sprite.height());
                sprite.layer().setTranslation(x, y);
                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }

    public void update(int delta) {
        if(!hasLoaded) return;
        e += delta;

        if(e > 150) {
            spriteIndex = offset + ((spriteIndex + 1) % 2);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }

    public Layer layer() {
        return sprite.layer();
    }
}
