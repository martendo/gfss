import greenfoot.*;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;

/**
 * A type of world whose display image is an upscaled version of its canvas
 * image.
 * <p>
 * All rendering should be done to a PixelWorld's canvas, which may then be
 * displayed to the user in the Greenfoot window by calling {@link #updateImage}.
 * <p>
 * This class handles separating objects of the PixelActor class by layer for
 * rendering. This render order is separate from the paint order of all Actor
 * objects defined by {@link #setPaintOrder}.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version April 2024
 */
public abstract class PixelWorld extends World {
    /** The scale factor of all PixelWorld display images. */
    public static final int PIXEL_SCALE = 4;

    private final int worldWidth;
    private final int worldHeight;
    private final GreenfootImage canvas;

    /**
     * Creates a new PixelWorld with the specified dimensions.
     * <p>
     * All PixelWorld objects use a Greenfoot cell size of 1 and are unbounded.
     *
     * @param worldWidth the width of this world, in canvas pixels
     * @param worldHeight the height of this world, in canvas pixels
     */
    public PixelWorld(int worldWidth, int worldHeight) {
        super(worldWidth * PIXEL_SCALE, worldHeight * PIXEL_SCALE, 1, false);
        canvas = new GreenfootImage(worldWidth, worldHeight);
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    /**
     * Returns the canvas image of this world, the GreenfootImage that is scaled
     * and displayed as this world's display image.
     * <p>
     * This image has dimensions matching the size specified when constructing
     * the world, and all rendering should be done to this image.
     *
     * @return the canvas image of this world, before scaling
     */
    public GreenfootImage getCanvas() {
        return canvas;
    }

    /**
     * Draws the display image of this world.
     * <p>
     * The canvas image of this world is scaled and drawn onto the world
     * background. This method should be called after all world rendering has
     * been done.
     */
    public void updateImage() {
        GreenfootImage scaled = new GreenfootImage(canvas);
        scaled.scale(worldWidth * PIXEL_SCALE, worldHeight * PIXEL_SCALE);
        setBackground(scaled);
    }

    /**
     * Gets the width of the downscaled world.
     * <p>
     * This is not the width of the final world that is displayed to the user.
     * To calculate that value, multiply the value returned by this method by
     * PixelWorld.PIXEL_SCALE.
     *
     * @return the width of the world, before scaling
     */
    @Override
    public int getWidth() {
        return worldWidth;
    }

    /**
     * Gets the height of the downscaled world.
     * <p>
     * This is not the height of the final world that is displayed to the user.
     * To calculate that value, multiply the value returned by this method by
     * PixelWorld.PIXEL_SCALE.
     *
     * @return the height of the world, before scaling
     */
    @Override
    public int getHeight() {
        return worldHeight;
    }
}
