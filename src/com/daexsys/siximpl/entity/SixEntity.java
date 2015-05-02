package com.daexsys.siximpl.entity;

import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.siximpl.world.chunk.Chunk;
import com.daexsys.siximpl.world.planet.Planet;

public class SixEntity extends Entity {
    private Planet planet;

    public SixEntity(float x, float y, float z) {
        super(x, y, z);
    }

    public SixEntity(float x, float y, float z, float width, float height, float length) {
        super(x, y, z, width, height, length);
    }

    /**
     * Sets the planet that the player is currently on.
     * @param planet the planet that the player is currenlty on.
     */
    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    /**
     * The current planet the entity is on
     * @return the entity's current entity
     */
    public Planet getPlanet() {
        return planet;
    }

    /**
     * The current chunk the entity is in
     * @return the entity's current chunk
     */
    public Chunk getCurrentChunk() {
        return getPlanet().getChunk(getChunkX(), getChunkY(), getChunkZ());
    }

    /**
     * Gets the X coordinate of the current chunk the entity is in
     * @return the X coordinate of the chunk the entity is in
     */
    public int getChunkX() {
        //32 = 16 blocks per chunk * block size (2)
        //(Chunk.CHUNK_SIZE * (int) Block.BLOCK_SIZE * 2)
        return (int) getX() / 32;
    }

    /**
     * Gets the Y coordinate of the current chunk the entity is in.
     * @return the Y coordinate of the chunk the entity is in
     */
    public int getChunkY() {
        return (int) getY() / 32;
    }

    /**
     * Gets the Z coordinate fo the current chunk the entity is in.
     * @return the Z coordinate fo the chunk the entity is in
     */
    public int getChunkZ() {
        return (int) getZ() / 32;
    }
}
