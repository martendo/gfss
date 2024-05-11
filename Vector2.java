public class Vector2 {
    /**
     * Normalize an angle in degrees to the range [0.0, 360.0).
     *
     * @param angle the angle to normalize, in degrees
     * @return an equivalent angle in degrees within the range [0.0, 360.0)
     */
    public static double normalizeAngle(double angle) {
        angle %= 360.0;
        if (angle < 0.0) {
            angle += 360.0;
        }
        // The above addition may result in a value of 360.0 due to floating point error
        // Take the remainder again to prevent returning 360.0
        angle %= 360.0;
        return angle;
    }
}
