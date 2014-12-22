package com.daexsys.sbc.entity;

import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.Planet;

public class SBEntity extends Entity {
    private Planet planet;

    public SBEntity(float x, float y, float z) {
        super(x, y, z);
    }

    public SBEntity(float x, float y, float z, float width, float height, float length) {
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
        return (int) getX() / 24;
    }

    /**
     * Gets the Y coordinate of the current chunk the entity is in.
     * @return the Y coordinate of the chunk the entity is in
     */
    public int getChunkY() {
        return (int) getY() / 24;
    }

    /**
     * Gets the Z coordinate fo the current chunk the entity is in.
     * @return the Z coordinate fo the chunk the entity is in
     */
    public int getChunkZ() {
        return (int) getZ() / 24;
    }
}
