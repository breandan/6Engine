package com.daexsys.sbc.entity;

import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.Planet;
import org.lwjgl.input.Keyboard;

public class Player extends SBEntity {
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
