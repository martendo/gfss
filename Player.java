import greenfoot.*;

public class Player extends Sprack {
    private static final double ACCEL = 0.2;
    private static final double FRIC_ACCEL = 0.08;
    private static final double MAX_SPEED = 5.0;

    private static final double ROT_ACCEL = 0.2;
    private static final double ROT_FRIC_ACCEL = 0.1;
    private static final double MAX_ROT_SPEED = 3;

    private double speed;
    private double rotSpeed;

    public Player() {
        super("car");
    }

    @Override
    public void update() {
        if (Greenfoot.isKeyDown("left")) {
            rotSpeed -= ROT_ACCEL;
        }
        if (Greenfoot.isKeyDown("right")) {
            rotSpeed += ROT_ACCEL;
        }
        // Only apply rotational friction to decrease rotational speed
        if (Math.abs(rotSpeed) < ROT_FRIC_ACCEL) {
            rotSpeed = 0.0;
        } else {
            rotSpeed -= Math.copySign(ROT_FRIC_ACCEL, rotSpeed);
        }
        rotSpeed = Math.copySign(Math.min(Math.abs(rotSpeed), MAX_ROT_SPEED), rotSpeed);
        double rotation = getSpriteRotation() + rotSpeed;
        setSpriteRotation(rotation);
        Camera.targetRotation(rotation);

        if (Greenfoot.isKeyDown("space")) {
            speed += ACCEL;
        }
        speed = Math.max(Math.min(speed - FRIC_ACCEL, MAX_SPEED), 0.0);
        // Sprite faces up
        rotation = Math.toRadians(rotation - 90.0);
        double x = getWorldX() + speed * Math.cos(rotation);
        double y = getWorldY() + speed * Math.sin(rotation);
        setWorldLocation(x, y);
        Camera.targetLocation(x, y);
    }
}
