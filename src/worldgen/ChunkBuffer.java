package worldgen;

class ChunkBuffer {

    Chunk chunk;

    ChunkBuffer () {

    }

    private void genChunk() {
        chunk = new Chunk();
    }

    boolean chunkRequest() {
        if (null == chunk) {
            genChunk();
            return false;
        }
        return true;
    }

    Chunk getChunk() {
        return chunk;
    }

}
