package worldgen;

import objects.Block;

import java.util.HashMap;

public class World {

    static private int worldSize = 10;
    private ChunkBuffer[][] chunks;

    public World() {
        chunks = new ChunkBuffer[worldSize][worldSize];
        for (int i = 0; i < chunks.length; i++) {
            for (int z = 0; z < chunks.length; z++) {
                chunks[i][z] = new ChunkBuffer();
            }
        }
    }

    public Block[][][] getBlocks(int x, int y) {
        x += (worldSize / 2);
        y += (worldSize / 2);
        chunks[x][y].chunkRequest();
        Chunk buffer = chunks[x][y].getChunk();
        return buffer.getBlocks();
    }

}
