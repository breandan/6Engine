package com.daexsys.sbc.entities;

import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.sbc.world.Chunk;
import com.daexsys.sbc.world.Planet;

public class Player extends Entity {
    private Planet planet;

    public Player(Planet planet, float x, float y, float z) {
        super(x, y, z);
        this.planet = planet;
    }

    /**
     * The current planet the player is on
     * @return the player's current planet
     */
    public Planet getPlanet() {
        return planet;
    }

    /**
     * The current chunk the player is in
     * @return the player's current chunk
     */
    public Chunk getCurrentChunk() {
        return getPlanet().getChunk(getChunkX(), getChunkY(), getChunkZ());
    }

    /**
     * Gets the X coordinate of the current chunk the player is in
     * @return the X coordinate of the chunk the player is in
     */
    public int getChunkX() {
        return (int) getX() / 16;
    }

    /**
     * Gets the Y coordinate of the current chunk the player is in.
     * @return the Y coordinate of the chunk the player is in
     */
    public int getChunkY() {
        return (int) getY() / 16;
    }

    /**
     * Gets the Z coordinate fo the current chunk the player is in.
     * @return the Z coordinate fo the chunk the player is in
     */
    public int getChunkZ() {
        return (int) getZ() /16;
    }

    /**
     * Attempt to generate all chunks nearby to the player that are currently ungenerated.
     * Currently attempt to generate a 3x3x3 area of chunks.
     */
    public void generateAroundPlayer() {
        int cX = getChunkX();
        int cY = getChunkY();
        int cZ = getChunkZ();

        for (int i = cX - 3; i < cX + 3; i++) {
            for (int j = cY - 3; j < cY + 3; j++) {
                for (int k = cZ - 3; k < cZ + 3; k++) {
                    getPlanet().attemptGeneration(cX, cY, cZ);
                }
            }
        }
    }
}
