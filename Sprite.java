import greenfoot.*;

public abstract class Sprite {
	private double x;
	private double y;
	private GreenfootImage image;
	private World world;

	public Sprite() {
		x = 0.0;
		y = 0.0;
		image = null;
		world = null;
	}

	public void setImage(GreenfootImage image) {
		this.image = new GreenfootImage(image);
	}

	public GreenfootImage getImage() {
		return image;
	}

	public void setScreenLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getScreenX() {
		return x;
	}

	public double getScreenY() {
		return y;
	}

	public void render(GreenfootImage canvas) {
		canvas.drawImage(image, (int) x - image.getWidth() / 2, (int) y - image.getHeight() / 2);
	}

	public void update() {}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void addedToWorld(World world) {}
}
