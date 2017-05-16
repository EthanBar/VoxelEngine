package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private static final boolean GODMODE = true;
    private Vector3f velocity = new Vector3f(0, 0, 0);
    private Vector3f position = new Vector3f(8, 20, 8);
    private float pitch = 0;
    private float roll = 0;
    private float yaw = 0;
    private static final float MOVESPEED = 0.04f;
    private static final float MAXSPEED = 0.12f;
    private static final float DECAYSPEED = 0.01f;
    private static final float GRAVITY = 0.002f;

    public void move() {
        yaw += Mouse.getDX() * 0.1;
        pitch += Mouse.getDY() * -0.1;
        if (pitch > 90) {
            pitch = 90;
        } else if (pitch < -90) {
            pitch = -90;
        }
        if (velocity.x > 0) {
            velocity.x -= DECAYSPEED;
            if (velocity.x < 0) velocity.x = 0;
        } else {
            velocity.x += DECAYSPEED;
            if (velocity.x > 0) velocity.x = 0;
        }
        if (velocity.z > 0) {
            velocity.z -= DECAYSPEED;
            if (velocity.z < 0) velocity.z = 0;
        } else {
            velocity.z += DECAYSPEED;
            if (velocity.z > 0) velocity.z = 0;
        }
        if (GODMODE) {
            if (velocity.y > 0) {
                velocity.y -= DECAYSPEED;
                if (velocity.y < 0) velocity.y = 0;
            } else {
                velocity.y += DECAYSPEED;
                if (velocity.y > 0) velocity.y = 0;
            }
        } else {
            velocity.y -= GRAVITY;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            double x = Math.sin(Math.toRadians(yaw));
            double z = Math.cos(Math.toRadians(yaw));
            velocity.z -= MOVESPEED * z;
            velocity.x += MOVESPEED * x;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            double xo = Math.sin(Math.toRadians(yaw + 90));
            double zo = Math.cos(Math.toRadians(yaw + 90));
            velocity.z -= MOVESPEED * zo;
            velocity.x += MOVESPEED * xo;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            double xo = Math.sin(Math.toRadians(yaw + 90));
            double zo = Math.cos(Math.toRadians(yaw + 90));
            velocity.z += MOVESPEED * zo;
            velocity.x -= MOVESPEED * xo;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            double x = Math.sin(Math.toRadians(yaw));
            double z = Math.cos(Math.toRadians(yaw));
            velocity.z += MOVESPEED * z;
            velocity.x -= MOVESPEED * x;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            velocity.y -= MOVESPEED;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            velocity.y += MOVESPEED;
        }

        if (velocity.x > MAXSPEED) {
            velocity.x = MAXSPEED;
        } else if (velocity.x < -MAXSPEED) {
            velocity.x = -MAXSPEED;
        }
        if (velocity.y > MAXSPEED && GODMODE) {
            velocity.y = MAXSPEED;
        } else if (velocity.y < -MAXSPEED  && GODMODE) {
            velocity.y = -MAXSPEED;
        }
        if (velocity.z > MAXSPEED) {
            velocity.z = MAXSPEED;
        } else if (velocity.z < -MAXSPEED) {
            velocity.z = -MAXSPEED;
        }
        position.translate(velocity.x, velocity.y, velocity.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
