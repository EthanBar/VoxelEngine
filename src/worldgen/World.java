package worldgen;

import java.util.HashMap;

public class World {

    static private int worldSize = 128;
    ChunkBuffer[][] chunks;

    public World() {
        chunks = new ChunkBuffer[worldSize][worldSize];
    }

}
