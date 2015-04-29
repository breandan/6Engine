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

    private float xa = 0.0f;
    private float ya = 0.0f;
    private float vertSpeed = 0.0f;

    private boolean flyMode = false;

    public Player(Planet planet, float x, float y, float z) {
        super(x, y, z);
        setPlanet(planet);
    }

    public void logic(long delta) {
        try {
            delta = 9;

            Block block = getPlanet().getBlock(getPX(), getPY() - 2, getPZ());

            logic2(delta);

            setY(getY() + vertSpeed * delta);
            walk(ya * delta, xa * delta);

            xa = 0;
            ya = 0;
            if (block != Block.DIRT && block != Block.GRASS && block != Block.STONE) {
                if(!flyMode) {
                    vertSpeed += 0.00045;
                }
            } else {
                vertSpeed = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void logic2(long delta) {
        float speed = flyMode ? 0.03f : 0.02f;

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            speed *= 0.3f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            xa -= speed;

            if(collides(getPX(), getPY(), getPZ())) {
                xa += speed;
            }
        } else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            xa += speed;

            if(collides(getPX(), getPY(), getPZ())) {
                xa -= speed;
            }
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            ya -= speed;

            if(collides(getPX(), getPY(), getPZ())) {
                ya += speed;
            }
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            ya +=speed;

            if(collides(getPX(), getPY(), getPZ())) {
                ya += speed;
            }
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if(vertSpeed == 0) {
                vertSpeed = -0.024f;
            }
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
            flyMode=true;
            setY(getY() - 1f);
        } else if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
            flyMode = true;
            setY(getY() + 1f);
        }

        if(System.currentTimeMillis() > lastCheckTime + 1000) {
            lastCheckTime = System.currentTimeMillis();
            generateAroundPlayer();
        }
        // TODO: Controls
        // TODO: Jumping / gravity
    }

    public boolean collides(double x, int y, double z) {
        y+=1;
        int topLeft = new Double(x - 0.5f).intValue();
        int topRight = new Double(x + 0.5f).intValue();
        int up = new Double(z + 0.5f).intValue();
        int down = new Double(z - 0.5f).intValue();
        Block block1 = getPlanet().getBlock(topLeft, y, up);
        Block block2 = getPlanet().getBlock(topRight, y, up);
        Block block3 = getPlanet().getBlock(topLeft, y, down);
        Block block4 = getPlanet().getBlock(topRight, y, down);

        if (block1 != Block.DIRT && block1 != Block.GRASS && block1 != Block.STONE) {
            if (block2 != Block.DIRT && block2 != Block.GRASS && block2 != Block.STONE) {
                if (block3 != Block.DIRT && block3 != Block.GRASS && block3 != Block.STONE) {
                    if (block4 != Block.DIRT && block4 != Block.GRASS && block4 != Block.STONE) {
                        return false;
                    }
                }
            }
        }

        return true;
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
