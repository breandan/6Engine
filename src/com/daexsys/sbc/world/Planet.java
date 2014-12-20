package com.daexsys.sbc.world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Planet {
    private Set<Chunk> chunks = new HashSet<Chunk>();
    private Map<Integer, ChunkLevel> chunkLevelMap = new HashMap<Integer, ChunkLevel>();
    private PlanetType planetType = PlanetType.GENERIC;

    public Chunk getChunk(int x, int y, int z) {
//        try {
//            return chunkLevelMap.get(y).getChunkRow(x).getChunk(z);
//        } catch (Exception e) {
//            return null;
//        }

        for(Chunk chunk : chunks) {

            if(chunk.getChunkX() == x && chunk.getChunkY() == y && chunk.getChunkZ() == z) {
//                System.out.println("Look: "+x + " "+y+" "+z);
//                System.out.println("Find: "+chunk.getChunkX() + " "+chunk.getChunkY()+" "+chunk.getChunkZ());
//                System.out.println("yes");
                return chunk;
            } else {
//                System.out.println("no");
            }
        }


//        System.out.println("Look: "+x + " "+y+" "+z);
//        System.out.println("no");

        return null;
    }

    public void setBlock(int x, int y, int z, Block block) {
        int cX = x/16;
        int cY = y/16;
        int cZ = z/16;

        Chunk chunk = getChunk(x,y,z);

        if(chunk != null) {
            chunk.setBlock(x - cX * 16, y - cY * 16, z - cZ * 16, block);
        }

    }

    public Block getBlock(int x, int y, int z) {
        int cX = x / 16;
        int cY = y / 16;
        int cZ = z / 16;

//        System.out.println("Chunk: "+cX+ " "+cY +" "+cZ);

        int iCX = x - cX * 16;
        int iCY = y - cY * 16;
        int iCZ = z - cZ * 16;

//        System.out.println("Inner block coord: "+iCX +" "+iCY+ " "+iCZ);

        Chunk chunk = getChunk(cX, cY, cZ);

        if(chunk != null) {
//            System.out.println("Chunk found!");
            return chunk.getBlock(iCX, iCY, iCZ);
        }

//        System.out.println("chunk is null");
//
        return null;
    }

    public void addChunk(Chunk chunk) {
        chunks.add(chunk);

        if(!chunkLevelMap.containsKey(chunk.getChunkY())) {
            chunkLevelMap.put(chunk.getChunkY(), new ChunkLevel(chunk.getChunkY()));
        }

        ChunkLevel chunkLevel = chunkLevelMap.get(chunk.getChunkY());

        ChunkRow chunkRow = chunkLevel.getChunkRow(chunk.getChunkX());

        if(chunkRow == null) {
            ChunkRow chunkRow1 = new ChunkRow(chunk.getChunkX());
            chunkLevel.addChunkRow(chunkRow1);
            chunkRow = chunkRow1;
        }

        chunkRow.addChunk(chunk.getChunkZ(), chunk);

        chunk.setWorld(this);
    }

    public void removeChunk(Chunk chunk) {
        chunks.remove(chunk);
    }

    public void renderWorld() {
        for(Chunk chunk : chunks) {
            chunk.render();
        }
    }

    public void rebuild() {
        for(Chunk chunk : chunks) {
            chunk.rebuild();
        }
    }

    public void attemptGeneration(int x, int y, int z) {

    }

    public PlanetType getPlanetType() {
        return planetType;
    }
}
