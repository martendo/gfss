import greenfoot.*;

public class SpriteStackingWorld extends World {
    public SpriteStackingWorld() {
        super(600, 400, 1, false);

        GreenfootImage background = getBackground();
        background.setColor(new Color(56, 56, 56));
        background.fill();

        Camera.resetTo(0, 0, 0, 3);
        Camera.setCloseness(0.2);

        addObject(new Player(), 0, 0);
        for (int i = 0; i < 100; i++) {
            addObject(new Crate(), Greenfoot.getRandomNumber(1000) - 500, Greenfoot.getRandomNumber(1000) - 500);
        }
    }

    public void act() {
        if (Greenfoot.isKeyDown("w")) {
            Camera.setZoom(Camera.getZoom() * 1.01);
        }
        if (Greenfoot.isKeyDown("s")) {
            Camera.setZoom(Camera.getZoom() * 0.99);
        }
    }
}
