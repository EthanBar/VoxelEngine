package worldgen;

import objects.Block;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Settings;

import static worldgen.World.noise;

public class Chunk {

    int xpos, ypos;
    private Block[][][] blocks;

    Chunk(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
        int scale = Settings.NOISE_SCALE;
        blocks = new Block[16][32][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                double yz = ((noise.eval((float)(x + (xpos * 16)) / scale, (float)(y + (ypos * 16)) / scale)));
                int yh = (int) ((yz + 1) / 2 * 16);
                for (int z = 0; z < 32; z++) {
                    if (yh < z) {
                        blocks[x][z][y] = new Block(x, y, z, z, 0);
                    } else {
                        blocks[x][z][y] = new Block(x, y, z, z, 1);
                    }
                }
            }
        }
    }

    public void updateBlock(int x, int y, int z, int id) {
        if (x >= 0 && x < 16 && y >= 0 && y < 32 && z >= 0 && z < 16) {
            blocks[x][y][z].setID(id);
        } else {
//            System.out.println("out of bounds");
        }
    }

    public Block[][][] getBlocks() {
        return blocks;
    }
}
