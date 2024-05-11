import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class SpriteStackingWorld extends World {
    private List<Sprite> sprites;

    public SpriteStackingWorld() {
        super(600, 400, 1, false);

        Camera.resetTo(0, 0, 0, 3);
        Camera.setCloseness(0.2);

        sprites = new ArrayList<>();
        addObject(new Player(), 0, 0);
        for (int i = 0; i < 100; i++) {
            addObject(new Crate(), Greenfoot.getRandomNumber(1000) - 500, Greenfoot.getRandomNumber(1000) - 500);
        }

        render();
    }

    public void act() {
        if (Greenfoot.isKeyDown("w")) {
            Camera.setZoom(Camera.getZoom() * 1.01);
        }
        if (Greenfoot.isKeyDown("s")) {
            Camera.setZoom(Camera.getZoom() * 0.99);
        }
        render();
    }

    private void render() {
        GreenfootImage background = getBackground();
        background.setColor(new Color(56, 56, 56));
        background.fill();
        for (Sprite sprite : sprites) {
            sprite.update();
            sprite.render(background);
        }
    }

    public void addObject(Sprack sprack, double x, double y) {
        sprites.add(sprack);
        sprack.setWorldLocation(x, y);
        sprack.setWorld(this);
        sprack.addedToWorld(this);
    }
}
