package worldgen;

import objects.Block;
import org.lwjgl.util.vector.Vector3f;

public class Chunk {

    float xpos, ypos;
    private Block[][][] blocks;

    Chunk() {
        blocks = new Block[16][16][16];
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks.length; y++) {
                for (int z = 0; z < blocks.length; z++) {
                    blocks[x][y][z] = new Block(x, y, z, y);
                }
            }
        }
    }

    public Block[][][] getBlocks() {
        return blocks;
    }
}
