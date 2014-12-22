package com.daexsys.sbc.world.planet;

import com.daexsys.ijen3D.entity.EntityGroup;
import com.daexsys.sbc.entity.SBEntity;
import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.chunk.ChunkLevel;
import com.daexsys.sbc.world.chunk.ChunkRow;
import com.daexsys.sbc.world.planet.generator.PlanetGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Planet {
    private Set<Chunk> chunks = new HashSet<Chunk>();
    private Map<Integer, ChunkLevel> chunkLevelMap = new HashMap<Integer, ChunkLevel>();
    private PlanetType planetType = PlanetType.GRASSY;

    private EntityGroup entities = new EntityGroup();

    public void addEntity(SBEntity sbEntity) {
        entities.addEntity(sbEntity);
    }

    public void removeEntity(SBEntity sbEntity) {
        entities.removeEntity(sbEntity);
    }

    public EntityGroup getEntities() {
        return entities;
    }

    public Chunk getChunk(int x, int y, int z) {
        for(Chunk chunk : chunks) {
            if(chunk.getChunkX() == x
                    && chunk.getChunkY() == y
                    && chunk.getChunkZ() == z
                    ) {
                return chunk;
            }
        }

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

        int iCX = x - cX * 16;
        int iCY = y - cY * 16;
        int iCZ = z - cZ * 16;

        Chunk chunk = getChunk(cX, cY, cZ);

        if(chunk != null) {
            return chunk.getBlock(iCX, iCY, iCZ);
        }

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
        System.out.println("generating: "+x +" "+y+" "+z);

        if(getChunk(x, y, z) == null) {
            PlanetGenerator planetGenerator = new PlanetGenerator(this);
            planetGenerator.generate(x, y, z);
        }
    }

    public Set<Chunk> getChunks() {
        return chunks;
    }

    public PlanetType getPlanetType() {
        return planetType;
    }
}
