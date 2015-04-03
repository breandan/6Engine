package com.daexsys.siximpl.world.chunk;

import java.util.HashMap;
import java.util.Map;

public class ChunkRow {
    private int x = 0;

    private Map<Integer, Chunk> intToChunk = new HashMap<Integer, Chunk>();

    public ChunkRow(int x) {
        this.x = x;
    }

    public int getRow() {
        return x;
    }

    public void addChunk(int chunkZ, Chunk chunk) {
        intToChunk.put(chunkZ, chunk);
    }

    public Chunk getChunk(int chunkZ) {
        return intToChunk.get(chunkZ);
    }
}
