package com.daexsys.siximpl.world.planet;

import com.daexsys.sixapi.SixWorld;
import com.daexsys.ijen3D.entity.EntityGroup;
import com.daexsys.siximpl.entity.SixEntity;
import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.chunk.Chunk;
import com.daexsys.siximpl.world.chunk.ChunkLevel;
import com.daexsys.siximpl.world.chunk.ChunkRow;
import com.daexsys.siximpl.world.planet.generator.PlanetGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A single 'dimension' or world.
 *
 * Contains cubic chunks in all directions up to the limit of a 32-bit integer.
 */
public class Planet implements BlockWorld, SixWorld {
    // A set of all chunks in this planet.
    private Set<Chunk> chunks = new HashSet<Chunk>();

    // A complex map of all chunks for quick access.
    private Map<Integer, ChunkLevel> chunkLevelMap = new HashMap<Integer, ChunkLevel>();

    // All entities present on this planet.
    private EntityGroup entities = new EntityGroup();

    // The seed of the planet's random terrain generation.
    private long seed;

    // The terrain generator for this planet.
    private PlanetGenerator planetGenerator;

    // The biome of this planet.
    private PlanetType planetType = PlanetType.GRASSY;

    public Planet(long seed, PlanetType planetType) {
        this.seed = seed;
        this.planetType = planetType;

        planetGenerator = new PlanetGenerator(this);
    }

    /**
     * Get the seed for the terrain generator.
     * @return the terrain generation seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Get the set of all chunks in this planet.
     * @return the set of all chunks in this planet
     */
    public Set<Chunk> getChunks() {
        return chunks;
    }

    /**
     * Set a block within this world.
     * @param x the X coordinate to set the block at
     * @param y the Y coordinate to set the block at
     * @param z the Z coordinate to set the block at
     * @param block the block type to be set here
     */
    public void setBlock(int x, int y, int z, Block block) {
        // Get the coordinates of the chunk this block will be set in.
        int cX = x / 16;
        int cY = y / 16;
        int cZ = z / 16;

        // Get this chunk.
        Chunk chunk = getChunk(cX, cY, cZ);

        // If the chunk exists, set the block.
        if(chunk != null) {
            chunk.setBlock(x % 16, y % 16, z % 16, block);
        } else {
            Chunk chunk2 = new Chunk(cX, cY, cZ);
            addChunk(chunk2);
            chunk2.setBlock(x % 16, y % 16, z % 16, block);
        }
    }

    public void clearTempBlocks() {
        for(Chunk chunk : chunks) {
            chunk.clearTempBlocks();
        }
    }

    public void setBlockNoRebuild(int x, int y, int z, Block block) {
        // Get the coordinates of the chunk this block will be set in.
        int cX = x / 16;
        int cY = y / 16;
        int cZ = z / 16;

        // Get this chunk.
        Chunk chunk = getChunk(cX, cY, cZ);

        // If the chunk exists, set the block.
        if(chunk != null) {
//            System.out.println("place");
            chunk.setBlockNoRebuild(x % 16, y % 16, z % 16, block);
        } else {
            Chunk chunk2 = new Chunk(cX, cY, cZ);
            addChunk(chunk2);
            chunk2.setBlockNoRebuild(x % 16, y % 16, z % 16, block);
        }
    }

    /**
     * Add an entity to this planet's entity group.
     * @param sixEntity the entity to be added
     */
    public void addEntity(SixEntity sixEntity) {
        entities.addEntity(sixEntity);
    }

    /**
     * Remove an entity from this planet's entity group.
     * @param sixEntity the entity to be removed
     */
    public void removeEntity(SixEntity sixEntity) {
        entities.removeEntity(sixEntity);
    }

    /**
     * Gets this planets EntityGroup.
     * Used to store all entities on this planet.
     * @return the EntityGroup of this planet.
     */
    public EntityGroup getEntities() {
        return entities;
    }

    /**
     * Get the terrain generator of this planet.
     * @return this planet's terrain generator
     */
    public PlanetGenerator getPlanetGenerator() {
        return planetGenerator;
    }

    /**
     * Get a chunk on this planet by it's coordinate.
     * @param x the X coordinate of the chunk
     * @param y the Y coordinate of the chunk
     * @param z the Z coordinate of the chunk
     * @return the chunk at these coordinates
     */
    public Chunk getChunk(int x, int y, int z) {
        // Iterate through every chunk in this world:
        for(Chunk chunk : chunks) {
            // If chunk coordinates match the ones specified...
            if(chunk.getChunkX() == x
                    && chunk.getChunkY() == y
                    && chunk.getChunkZ() == z
                    ) {
                // Return that chunk.
                return chunk;
            }
        }

        // Otherwise return null.
        return null;
    }

    /**
     * Gets the block at a specified world coord.
     * @param x the X coordinate of the block
     * @param y the Y coordinate of the block
     * @param z the Z coordinate of the block
     * @return the clock at these coordinates
     */
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

    /**
     * Adds a chunk to the world.
     * @param chunk the chunk to be added to the world
     */
    public void addChunk(Chunk chunk) {
        // Add the chunk to the chunk set.
        chunks.add(chunk);

        // Add chunk to the chunk access structure.
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

        // Set this chunk's planet to be this one.
        chunk.setPlanet(this);
    }

    /**
     * Remove a chunk from this planet.
     * @param chunk the chunk to be removed
     */
    public void removeChunk(Chunk chunk) {
        chunks.remove(chunk);
    }

    /**
     * Render the chunks in this world.
     */
    public void renderWorld() {
        for(Chunk chunk : chunks) {
            chunk.render();
        }
    }

    /**
     * Rebuild the terrain geometry for this world.
     */
    public void rebuildRenderGeometry() {
        for(Chunk chunk : chunks) {
            chunk.rebuildRenderGeometry();
        }
    }

    /**
     * Attempt to generate a chunk in this world.
     * @param x the X coordinate of the chunk to be generated
     * @param y the Y coordinate of the chunk to be generated
     * @param z the Z coordinate of the chunk to be generated
     */
    public void attemptGeneration(int x, int y, int z) {
//        System.out.println("generating: "+x +" "+y+" "+z);

        if(getChunk(x, y, z) == null) {
            PlanetGenerator planetGenerator = new PlanetGenerator(this);
            planetGenerator.generate(x, y, z);
        }
    }

    /**
     * Get the type of planet / biome of this planet.
     * @return the type of planet this is.
     */
    public PlanetType getPlanetType() {
        return planetType;
    }

    @Override
    public void alterBlockAt(String s, int i, int i1, int i2) {
        int id = Integer.parseInt(s);

        Block block = null;
        if(id == 0) {
            block = Block.AIR;
        } else if(id == 1) {
            block = Block.GRASS;
        } else if(id == 2) {
            block = Block.DIRT;
        } else if(id == 5) {
            block = Block.STONE;
        }

//        System.out.println("World loc " + i + " " + i1 + " " + i2);
        setBlockNoRebuild(i, i1, i2, block);

    }

    @Override
    public String getBlockAt(int i, int i1, int i2) {
        return getBlock(i, i1, i2).getID() + "";
    }
}
