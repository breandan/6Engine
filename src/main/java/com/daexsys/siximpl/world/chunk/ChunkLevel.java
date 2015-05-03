package com.daexsys.siximpl.world.chunk;

import java.util.HashMap;
import java.util.Map;

public class ChunkLevel {
    private int level = 0;

    private Map<Integer, ChunkRow> chunkRowMap = new HashMap<Integer, ChunkRow>();

    public ChunkLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
         return level;
    }

    public void addChunkRow(ChunkRow chunkRow) {
        chunkRowMap.put(chunkRow.getRow(), chunkRow);
    }

    public void addChunk(int x, int z, Chunk chunk) {
        chunkRowMap.get(x).addChunk(z, chunk);
    }

    public ChunkRow getChunkRow(int row) {
        if(chunkRowMap.containsKey(row)) {
            return chunkRowMap.get(row);
        }

        return null;
    }
}
