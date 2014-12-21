package com.daexsys.sbc.entity;

import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.Planet;
import org.lwjgl.input.Keyboard;

public class Player extends Entity {
    private Planet planet;

    public Player(Planet planet, float x, float y, float z) {
        super(x, y, z);
        this.planet = planet;
    }

    @Override
    public void logic() {
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            walkForward(1);
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            walkForward(-1);
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            walkSideways(1);
        } else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            walkSideways(-1);
        }

        generateAroundPlayer();
        // TODO: Controls
        // TODO: Jumping / gravity
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
        return (int) getX() / 32;
    }

    /**
     * Gets the Y coordinate of the current chunk the player is in.
     * @return the Y coordinate of the chunk the player is in
     */
    public int getChunkY() {
        return (int) getY() / 32;
    }

    /**
     * Gets the Z coordinate fo the current chunk the player is in.
     * @return the Z coordinate fo the chunk the player is in
     */
    public int getChunkZ() {
        return (int) getZ() / 32;
    }

    /**
     * Attempt to generate all chunks nearby to the player that are currently ungenerated.
     * Currently attempt to generate a 3x3x3 area of chunks.
     */
    public void generateAroundPlayer() {
        int cX = getChunkX();
        int cY = getChunkY();
        int cZ = getChunkZ();

        System.out.println("attempting");

        for (int i = cX - 3; i < cX + 3; i++) {
                for (int k = cZ - 3; k < cZ + 3; k++) {
                    getPlanet().attemptGeneration(i, 0, k);
                }
        }
    }
}
