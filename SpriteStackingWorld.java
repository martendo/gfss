import greenfoot.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class SpriteStackingWorld extends PixelWorld {
	private static final int OBJECT_SPAWN_RANGE = 2000;

	static {
		SprackView.loadAll();
	}

	private List<Sprite> sprites;
	private List<Sprack> spracks;

	public SpriteStackingWorld() {
		super(256, 196);

		Camera.resetTo(0, 0, 0, 3);
		Camera.setCloseness(0.2);

		sprites = new ArrayList<>();
		spracks = new ArrayList<>();
		addObject(new Player(), 0, 0);
		for (int i = 0; i < 100; i++) {
			addObject(new Sprack("crate"), Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
		}
		/*
		for (int i = 0; i < 100; i++) {
			addObject(new Sprack("building"), Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
		}
		for (int i = 0; i < 100; i++) {
			addObject(new Sprack("tree"), Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
		}
		*/
		addObject(new Sprack("tower"), 0, 0);

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
		GreenfootImage background = getCanvas();
		background.setColor(new Color(56, 56, 56));
		background.fill();
		for (Sprite sprite : sprites) {
			sprite.update();
		}
		List<Sprack> orderedSpracks = new ArrayList<>(spracks);
		orderedSpracks.sort(Comparator.comparing(Sprack::getScreenY));
		for (Sprack sprack : orderedSpracks) {
			sprack.render(background);
		}
		updateImage();
	}

	public void addObject(Sprite sprite, double x, double y) {
		sprites.add(sprite);
		if (sprite instanceof Sprack) {
			Sprack sprack = (Sprack) sprite;
			spracks.add(sprack);
			sprack.setWorldLocation(x, y);
		} else {
			sprite.setScreenLocation(x, y);
		}
		sprite.setWorld(this);
		sprite.addedToWorld(this);
	}
}
