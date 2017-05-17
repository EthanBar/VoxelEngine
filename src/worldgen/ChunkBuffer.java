package worldgen;

class ChunkBuffer {

    Chunk chunk;

    ChunkBuffer () {

    }

    private void genChunk(int x, int y) {
        chunk = new Chunk(x, y);
    }

    boolean chunkRequest(int x, int y) {
        if (null == chunk) {
            genChunk(x, y);
            return false;
        }
        return true;
    }

    Chunk getChunk() {
        return chunk;
    }

}
