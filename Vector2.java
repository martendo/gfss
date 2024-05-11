public class Vector2 {
    /**
     * Normalize an angle in degrees to the range [0.0, 360.0].
     *
     * @param angle the angle to normalize, in degrees
     * @return an equivalent angle in degrees within the range [0.0, 360.0]
     */
    public static double normalizeAngle(double angle) {
        angle %= 360.0;
        if (angle < 0.0) {
            angle += 360.0;
        }
        return angle;
    }
}
