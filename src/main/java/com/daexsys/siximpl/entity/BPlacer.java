package com.daexsys.siximpl.entity;

import com.daexsys.siximpl.SixEngineClient;
import com.daexsys.siximpl.world.block.Block;

public class BPlacer extends SixEntity {
    private float a;
    private float b;
    private int age = 0;

    public BPlacer(float x, float y, float z, float a, float b) {
        super(x, y, z);
        this.a = a;
        this.b = b;
    }

    @Override
    public void logic() {
        super.logic();
        age++;
        if(age > 1000) SixEngineClient.entityGroup.removeEntity(this);

        moveInAngle(a, b, 0.3f);

        Block block = SixEngineClient.getPlayer().getPlanet().getBlock(getPX(), getPY(), getPZ());
//        System.out.println("block at " + getPX() + " " + getPY() + " " + getPZ() + " is " + block);

        if(block == Block.GRASS || block == Block.DIRT) {
            if(SixEngineClient.player.getPX() < getPX()) {
                SixEngineClient.getPlayer().getPlanet().setBlock(getPX() -1, getPY(), getPZ(), Block.DIRT);
            } else if(SixEngineClient.player.getPX() < getPZ()) {
                SixEngineClient.getPlayer().getPlanet().setBlock(getPX(), getPY(), getPZ() -1, Block.DIRT);
            }
            if(SixEngineClient.player.getPY() > getPY()) {
                SixEngineClient.getPlayer().getPlanet().setBlock(getPX(), getPY() + 1, getPZ(), Block.DIRT);
            }
//            System.out.println("placing at " + getPX() + " " + getPY() + " " + getPZ());
            SixEngineClient.entityGroup.removeEntity(this);
        }
    }

    public int getPX() {
        return new Double(getX() / Block.BLOCK_SIZE * 2).intValue();
    }

    public int getPY() {
        return new Double(getY() /Block.BLOCK_SIZE * 2).intValue();
    }

    public int getPZ() {
        return new Double(getZ() / Block.BLOCK_SIZE * 2).intValue();
    }

    @Override
    public void render() {
//        glPushMatrix();
//        {
//            glColor3f(0.8f, 0.2f, 0.2f);
//            glTranslatef(getX(), getY(), getZ());
//
//            glRotatef(getPitch(),   1, 0, 0);
//            glRotatef(getYaw(),     0, 1, 0);
////            glRotatef(entity.getRoll(),  0, 0, 1);
//
//            glBegin(GL_QUADS);
//            {
//                float size = 0.3f;
//                glVertex3f(-size, -size, -size);
//                glVertex3f(-size, -size, size);
//                glVertex3f(size, -size, size);
//                glVertex3f(size, -size, -size);
//
//            }
//            glEnd();
//        }
//        glPopMatrix();
    }
}
