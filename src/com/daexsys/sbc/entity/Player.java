package com.daexsys.sbc.entity;

import com.daexsys.ijen3D.Coordinate;
import com.daexsys.ijen3D.entity.Entity;
import com.daexsys.sbc.BlockCoord;
import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.Planet;
import org.lwjgl.input.Keyboard;

public class Player extends SBEntity {
    private long lastCheckTime = System.currentTimeMillis() - 1000;

    public Player(Planet planet, float x, float y, float z) {
        super(x, y, z);
        setPlanet(planet);
    }

    public int getPX() {
        return new Double(getX() / 1.6).intValue();
    }

    public int getPY() {
        return new Double(getY() * -1 / 1.6).intValue();
    }

    public int getPZ() {
        return new Double(getZ() / 1.6).intValue();
    }

    public BlockCoord getNearestBlock() {
        for (int i = 0; i < 1000; i++) {
            double x = getPX() + i * new Float(Math.cos(Math.toRadians(getYaw() + 90)));
            double y = getPY() + i * new Float(Math.cos(Math.toRadians(getPitch() + 90)));
            double z = getPZ() + i * new Float(Math.sin(Math.toRadians(getYaw() + 90)));

            Block block = getPlanet().getBlock(new Double(x).intValue(), new Double(y).intValue(), new Double(z).intValue());
            if(block == Block.GRASS) return new BlockCoord(new Double(x).intValue(), new Double(y - 1).intValue(), new Double(z).intValue());
        }

        return null;
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
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                int cX = getChunkX();
//                int cY = getChunkY();
//                int cZ = getChunkZ();
//
//                for (int i = cX - 2; i < cX + 2; i++) {
//                    for (int k = cZ - 2; k < cZ + 2; k++) {
//                        for (int j = cY - 2; j < cY + 2; j++) {
//                            getPlanet().attemptGeneration(i, j, k);
//                        }
//                    }
//                }
//            }
//        }).start();
    }
}
