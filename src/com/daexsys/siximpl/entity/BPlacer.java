package com.daexsys.siximpl.entity;

import com.daexsys.siximpl.SBC;
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
        if(age > 1000) SBC.entityGroup.removeEntity(this);

        moveInAngle(a, b, 0.3f);

        Block block = SBC.getPlayer().getPlanet().getBlock(getPX(), getPY(), getPZ());
//        System.out.println("block at " + getPX() + " " + getPY() + " " + getPZ() + " is " + block);

        if(block == Block.GRASS || block == Block.DIRT) {
            if(SBC.player.getPX() < getPX()) {
                SBC.getPlayer().getPlanet().setBlock(getPX() -1, getPY(), getPZ(), Block.DIRT);
            } else if(SBC.player.getPX() < getPZ()) {
                SBC.getPlayer().getPlanet().setBlock(getPX(), getPY(), getPZ() -1, Block.DIRT);
            }
            if(SBC.player.getPY() > getPY()) {
                SBC.getPlayer().getPlanet().setBlock(getPX(), getPY() + 1, getPZ(), Block.DIRT);
            }
//            System.out.println("placing at " + getPX() + " " + getPY() + " " + getPZ());
            SBC.entityGroup.removeEntity(this);
        }
    }

    public int getPX() {
        return new Double(getX() / 1.6).intValue();
    }

    public int getPY() {
        return new Double(getY() / 1.6).intValue();
    }

    public int getPZ() {
        return new Double(getZ() / 1.6).intValue();
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
