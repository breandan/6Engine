package com.daexsys.siximpl.entity;

import com.daexsys.ijen3D.net.server.Server;
import com.daexsys.siximpl.BlockCoord;
import com.daexsys.siximpl.SBC;
import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.planet.Planet;
import org.lwjgl.input.Keyboard;

import java.io.DataOutputStream;
import java.io.IOException;

public class Player extends SixEntity {
    private long lastCheckTime = System.currentTimeMillis() - 1000;
    private float vertSpeed = 0.0f;

    public Player(Planet planet, float x, float y, float z) {
        super(x, y, z);
        setPlanet(planet);
    }

    public void logic(long delta) {
        Block block = getPlanet().getBlock(getPX(), getPY() - 2, getPZ());

        logic();

        if(block != Block.DIRT && block != Block.GRASS && block != Block.STONE) {
            vertSpeed+= 0.1f;
        } else {
            vertSpeed = 0;
        }

        setY(getY() + vertSpeed);
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
            walkForward(0.5f);
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            walkForward(-0.5f);
        }

        //-2
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//            setY(getY() - 1);
            if(vertSpeed == 0) {
                vertSpeed -= 1f;
            }
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            walkSideways(0.5f);
        } else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            walkSideways(-0.5f);
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
//        System.out.println("Setting at " + getPX() + " " + getPY() + " " + getPZ());
//        SBC.getUniverse().getPlanetAt(0,0,0).setBlockNoRebuild(getPX(), getPY(), getPZ(), Block.DIRT);
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(SBC.client.socket.getOutputStream());
            dataOutputStream.writeByte(5);
            dataOutputStream.writeInt(getChunkX());
            dataOutputStream.writeInt(getChunkY());
            dataOutputStream.writeInt(getChunkZ());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
