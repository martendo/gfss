import greenfoot.*;

public class Sprack extends Sprite {
	private final String sheetName;

	private double worldX;
	private double worldY;
	private double rotation;

	public Sprack(String sheetName) {
		/*
		view = SprackView.getView(sheetName);
		*/
		this.sheetName = sheetName;
	}

	public void setSpriteRotation(double rotation) {
		this.rotation = Vector2.normalizeAngle(rotation);
	}

	public double getSpriteRotation() {
		return rotation;
	}

	public void setWorldLocation(double x, double y) {
		worldX = x;
		worldY = y;
	}

	@Override
	public void render(GreenfootImage canvas) {
		SprackView view = SprackView.getView(sheetName);
		if (view == null) {
			return;
		}

		// Update screen location, rotated around zoomed camera position
		double scale = Camera.getZoom();
		double offsetX = (worldX - Camera.getX()) * scale;
		double offsetY = (worldY - Camera.getY()) * scale;
		double screenRad = Math.toRadians(-Camera.getRotation());
		double screenX = getWorld().getWidth() / 2 + offsetX * Math.cos(screenRad) - offsetY * Math.sin(screenRad);
		double screenY = getWorld().getHeight() / 2 + offsetX * Math.sin(screenRad) + offsetY * Math.cos(screenRad);
		setScreenLocation(screenX, screenY);

		// Don't render if offscreen
		double imageRotation = rotation - Camera.getRotation();
		int centerX = view.getCenterX(imageRotation, scale);
		int centerY = view.getCenterY(imageRotation, scale);
		if (screenX + centerX < 0
			|| screenX - centerX >= getWorld().getWidth()
			|| screenY + (view.getTransformedHeight(imageRotation, scale) - centerY) < 0
			|| screenY - centerY >= getWorld().getHeight()) {
			return;
		}

		// Draw image, screen location at center of bottom layer
		GreenfootImage image = view.getTransformedImage(imageRotation, Camera.getZoom());
		if (image == null) {
			return;
		}
		canvas.drawImage(image, (int) screenX - centerX, (int) screenY - centerY);
	}

	public double getWorldX() {
		return worldX;
	}

	public double getWorldY() {
		return worldY;
	}
}
