import greenfoot.*;

public abstract class Sprack extends Actor {
    private final int layerWidth;
    private final int layerHeight;
    private final GreenfootImage[] layers;

    private double worldX;
    private double worldY;
    private double rotation;

    public Sprack(GreenfootImage layerSheet, int layerCount) {
        if (layerCount < 1) {
            throw new IllegalArgumentException("Sprite stack must consist of at least 1 layer");
        }
        layerWidth = layerSheet.getWidth();
        layerHeight = layerSheet.getHeight() / layerCount;
        layers = new GreenfootImage[layerCount];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new GreenfootImage(layerWidth, layerHeight);
            layers[i].drawImage(layerSheet, 0, -layerHeight * (layerCount - 1 - i));
        }
        updateImage();
    }

    public Sprack(String layerSheetPath, int layerCount) {
        this(new GreenfootImage(layerSheetPath), layerCount);
    }

    public void act() {
        updateScreenLocation();
        if (getX() + getImage().getWidth() / 2 < 0 || getX() - getImage().getWidth() / 2 >= getWorld().getWidth()
            || getY() + getImage().getHeight() / 2 < 0 || getY() - getImage().getHeight() / 2 >= getWorld().getHeight()) {
            return;
        }
        updateImage();
    }

    public void updateImage() {
        double imageDegrees = rotation - Camera.getRotation();
        double imageRad = Math.toRadians(imageDegrees);
        double scale = Camera.getZoom();
        int width = (int) (layerWidth * scale);
        int height = (int) (layerHeight * scale);
        if (width <= 0 || height <= 0) {
            setImage((GreenfootImage) null);
            return;
        }
        int rotWidth = (int) (Math.abs(width * Math.cos(imageRad)) + Math.abs(height * Math.sin(imageRad)));
        int rotHeight = (int) (Math.abs(width * Math.sin(imageRad)) + Math.abs(height * Math.cos(imageRad)));
        GreenfootImage image = new GreenfootImage(rotWidth, rotHeight + (int) (layers.length * scale));
        for (int i = 0; i < layers.length; i++) {
            GreenfootImage layer = new GreenfootImage(layers[i]);
            layer.scale(width, height);
            GreenfootImage rotLayer = new GreenfootImage(rotWidth, rotHeight);
            rotLayer.drawImage(layer, (rotWidth - width) / 2, (rotHeight - height) / 2);
            rotLayer.rotate((int) imageDegrees);
            image.drawImage(rotLayer, 0, (int) (scale * (layers.length - 1 - i)));
        }
        setImage(image);
    }

    public void setSpriteRotation(double angle) {
        rotation = angle;
        updateImage();
    }

    public double getSpriteRotation() {
        return rotation;
    }

    @Override
    public void addedToWorld(World world) {
        setWorldLocation(getX(), getY());
    }

    public void setWorldLocation(double x, double y) {
        worldX = x;
        worldY = y;
        updateScreenLocation();
    }

    private void updateScreenLocation() {
        double scale = Camera.getZoom();
        double offsetX = (worldX - Camera.getX()) * scale;
        double offsetY = (worldY - Camera.getY()) * scale;
        double screenRad = Math.toRadians(-Camera.getRotation());
        double screenX = getWorld().getWidth() / 2 + offsetX * Math.cos(screenRad) - offsetY * Math.sin(screenRad);
        double screenY = getWorld().getHeight() / 2 + offsetX * Math.sin(screenRad) + offsetY * Math.cos(screenRad);
        setLocation((int) screenX, (int) screenY);
    }

    public double getWorldX() {
        return worldX;
    }

    public double getWorldY() {
        return worldY;
    }
}
