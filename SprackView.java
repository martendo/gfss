import greenfoot.*;

/**
 * A container of cached sprite stack images pre-rendered from many angles for
 * efficient use with the Sprack class. SprackView objects are immutable, and
 * only one object per sprite stack sheet should be created, since a new cache
 * of images will be created for each instance.
 *
 * @author Martin Baldwin
 * @version May 2024
 */
public class SprackView {
    /** The scale factor of the images within a SprackView object's cache. */
    public static final double IMAGE_CACHE_SCALE = 8.0;
    /** The number of different rotation angles, evenly spaced, to make available in the cache. */
    private static final int IMAGE_CACHE_ANGLE_COUNT = 140;

    /** The width of an untransformed layer, in pixels. */
    private final int layerWidth;
    /** The height of an untransformed layer, in pixels. */
    private final int layerHeight;

    /**
     * Images of the sprite stack at different rotation angles. rotCache[i]
     * contains the image representing the sprite stack at an angle of
     * i / IMAGE_CACHE_ANGLE_COUNT * 360, scaled by a factor of IMAGE_CACHE_SCALE.
     */
    private final GreenfootImage[] rotCache;

    /**
     * The positional horizontal screen centers of corresponding transformed
     * sprite stack images in rotCache, in pixels.
     */
    private final int[] rotCacheCenterX;

    /**
     * The positional vertical screen centers of corresponding transformed
     * sprite stack images in rotCache, in pixels.
     */
    private final int[] rotCacheCenterY;

    /**
     * Create a new cache for a sprite stack created by layering the specified
     * number of layers from the given layer sheet image.
     *
     * @param layerSheet a GreenfootImage containing individual layers arranged vertically from bottom to top
     * @param layerCount the number of layers to extract from the given layer sheet
     */
    public SprackView(GreenfootImage layerSheet, int layerCount) {
        // Create individual layer images from sheet
        layerWidth = layerSheet.getWidth();
        layerHeight = layerSheet.getHeight() / layerCount;
        GreenfootImage[] layers = new GreenfootImage[layerCount];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new GreenfootImage(layerWidth, layerHeight);
            layers[i].drawImage(layerSheet, 0, -layerHeight * (layerCount - 1 - i));
        }

        // Create rotated image cache
        rotCache = new GreenfootImage[IMAGE_CACHE_ANGLE_COUNT];
        rotCacheCenterX = new int[IMAGE_CACHE_ANGLE_COUNT];
        rotCacheCenterY = new int[IMAGE_CACHE_ANGLE_COUNT];
        for (int i = 0; i < IMAGE_CACHE_ANGLE_COUNT; i++) {
            double imageDegrees = 360.0 / IMAGE_CACHE_ANGLE_COUNT * i;
            double imageRad = Math.toRadians(imageDegrees);
            // Get scaled dimensions of layers
            int width = (int) (layerWidth * IMAGE_CACHE_SCALE);
            int height = (int) (layerHeight * IMAGE_CACHE_SCALE);
            // Get rotated and scaled dimensions of layer images
            int rotWidth = (int) (Math.abs(width * Math.cos(imageRad)) + Math.abs(height * Math.sin(imageRad)));
            int rotHeight = (int) (Math.abs(width * Math.sin(imageRad)) + Math.abs(height * Math.cos(imageRad)));
            // Draw layers onto an image
            GreenfootImage image = new GreenfootImage(rotWidth, rotHeight + (int) (layers.length * IMAGE_CACHE_SCALE));
            for (int j = 0; j < layers.length; j++) {
                GreenfootImage layer = new GreenfootImage(layers[j]);
                layer.scale(width, height);
                GreenfootImage rotLayer = new GreenfootImage(rotWidth, rotHeight);
                rotLayer.drawImage(layer, (rotWidth - width) / 2, (rotHeight - height) / 2);
                rotLayer.rotate((int) imageDegrees);
                image.drawImage(rotLayer, 0, (int) (IMAGE_CACHE_SCALE * (layers.length - 1 - j)));
            }
            rotCache[i] = image;
            rotCacheCenterX[i] = rotWidth / 2;
            rotCacheCenterY[i] = image.getHeight() - rotHeight / 2;
        }
    }

    /**
     * Return the index into a SprackView object's cache arrays that corresponds
     * to the given rotation angle.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @return the index into cache arrays to access the data corresponding to the given angle
     */
    private static int getCacheIndex(double rotation) {
        return (int) (Vector2.normalizeAngle(rotation) / 360.0 * IMAGE_CACHE_ANGLE_COUNT);
    }

    /**
     * Return a GreenfootImage representing the sprite stack of this SprackView
     * rotated and scaled by the specified amounts, or null if the image would
     * be empty.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return a new GreenfootImage representing the sprite stack, or null if the image would be empty
     * @throws UnsupportedOperationException if the given scale factor is larger than the scale factor of the image cache
     */
    public GreenfootImage getTransformedImage(double rotation, double scale) {
        if (scale > IMAGE_CACHE_SCALE) {
            throw new UnsupportedOperationException("Cannot scale stacked sprite larger than cached image scale (got " + scale + ", max in cache " + IMAGE_CACHE_SCALE + ")");
        }
        GreenfootImage cachedImage = rotCache[getCacheIndex(rotation)];
        int scaledWidth = (int) (cachedImage.getWidth() / IMAGE_CACHE_SCALE * scale);
        int scaledHeight = (int) (cachedImage.getHeight() / IMAGE_CACHE_SCALE * scale);
        if (scaledWidth <= 0 || scaledHeight <= 0) {
            return null;
        }
        GreenfootImage image = new GreenfootImage(cachedImage);
        image.scale(scaledWidth, scaledHeight);
        return image;
    }

    /**
     * Return the screen width of the sprite stack of this SprackView rotated
     * and scaled by the specified amounts.
     * <p>
     * This is a less expensive operation compared to calling {@link GreenfootImage#getWidth}
     * on the image returned by {@link #getTransformedImage}.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return the width of the image that would be used if the same parameters were passed to {@link #getTransformedImage}
     */
    public int getTransformedWidth(double rotation, double scale) {
        return (int) (rotCache[getCacheIndex(rotation)].getWidth() / IMAGE_CACHE_SCALE * scale);
    }

    /**
     * Return the screen height of the sprite stack of this SprackView rotated
     * and scaled by the specified amounts.
     * <p>
     * This is a less expensive operation compared to calling {@link GreenfootImage#getHeight}
     * on the image returned by {@link #getTransformedImage}.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return the height of the image that would be used if the same parameters were passed to {@link #getTransformedImage}
     */
    public int getTransformedHeight(double rotation, double scale) {
        return (int) (rotCache[getCacheIndex(rotation)].getHeight() / IMAGE_CACHE_SCALE * scale);
    }

    /**
     * Return the positional horizontal screen center of the sprite stack of
     * this SprackView rotated and scaled by the specified amounts.
     * <p>
     * The offset returned is located at the center of the sprite stack.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return the screen x offset of the sprite stack's center, relative to the image that would be used if the same parameters were passed to {@link #getTransformedImage}
     */
    public int getCenterX(double rotation, double scale) {
        return (int) (rotCacheCenterX[getCacheIndex(rotation)] / IMAGE_CACHE_SCALE * scale);
    }

    /**
     * Return the positional vertical screen center of the sprite stack of
     * this SprackView rotated and scaled by the specified amounts.
     * <p>
     * The offset returned is located at the center of the bottommost layer of
     * the sprite stack.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return the screen y offset of the sprite stack's center, relative to the image that would be used if the same parameters were passed to {@link #getTransformedImage}
     */
    public int getCenterY(double rotation, double scale) {
        return (int) (rotCacheCenterY[getCacheIndex(rotation)] / IMAGE_CACHE_SCALE * scale);
    }
}
