package objects;

import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class Block {

    static Random random = new Random();
    private Vector3f position;
    private int ID = 1;

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    private int light;

    public Block(int x, int y, int z, int light) {
        position = new Vector3f(x, y, z);
        if (light > 15) {
            this.light = 16;
        } else {
            this.light = light;
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Vector3f getPosition() {
        return position;
    }

}
