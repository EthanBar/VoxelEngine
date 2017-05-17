package worldgen;

import objects.Block;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class World {

    static private int worldSize = 3;
    private ChunkBuffer[][] chunks;
    static OpenSimplexNoise noise;

    public World() {
        noise = new OpenSimplexNoise(ThreadLocalRandom.current().nextLong());
        chunks = new ChunkBuffer[worldSize][worldSize];
        for (int i = 0; i < chunks.length; i++) {
            for (int z = 0; z < chunks.length; z++) {
                chunks[i][z] = new ChunkBuffer();
            }
        }
    }

    public boolean checkEmpty(int x, int y) {
        x += (worldSize / 2);
        y += (worldSize / 2);
        return !(x > worldSize - 1 || y > worldSize - 1 || x < 0 || y < 0);
    }

    public Block[][][] getBlocks(int x, int y) {
        x += (worldSize / 2);
        y += (worldSize / 2);
//        if (x > worldSize - 1 || y > worldSize - 1)
        chunks[x][y].chunkRequest(x, y);
        return chunks[x][y].getChunk().getBlocks();
    }

    public Chunk getChunk(int x, int y) {
        x += (worldSize / 2);
        y += (worldSize / 2);
        chunks[x][y].chunkRequest(x, y);
        return chunks[x][y].getChunk();
    }

}
