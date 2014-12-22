package com.daexsys.sbc.entity;

import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.Planet;
import org.lwjgl.input.Keyboard;

public class Player extends SBEntity {
    private long lastCheckTime = System.currentTimeMillis() - 1000;

    public Player(Planet planet, float x, float y, float z) {
        super(x, y, z);
        setPlanet(planet);
    }

    @Override
    public void logic() {
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            moveForward(1);
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            moveForward(-1);
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            walkSideways(1);
        } else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            walkSideways(-1);
        }

        if(System.currentTimeMillis() > lastCheckTime + 1000) {
            lastCheckTime = System.currentTimeMillis();
            generateAroundPlayer();
        }
        // TODO: Controls
        // TODO: Jumping / gravity
    }


    /**
     * Attempt to generate all chunks nearby to the player that are currently ungenerated.
     * Currently attempt to generate a 3x3x3 area of chunks.
     */
    public void generateAroundPlayer() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int cX = getChunkX();
                int cY = getChunkY();
                int cZ = getChunkZ();

                for (int i = cX - 2; i < cX + 2; i++) {
                    for (int k = cZ - 2; k < cZ + 2; k++) {
                        for (int j = cY - 2; j < cY + 2; j++) {
                            getPlanet().attemptGeneration(i, j, k);
                        }
                    }
                }
            }
        }).start();
    }
}
