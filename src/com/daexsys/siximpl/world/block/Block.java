package com.daexsys.siximpl.world.block;

import com.daexsys.siximpl.world.chunk.Chunk;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Block {
    public static final Block AIR = new Air();
    public static final Block GRASS = new Block(1,0,1);
    public static final Block DIRT = new Block(2,2);
    public static final Block STONE = new Block(5,3);
    public static final Block WOOD = new Block(3,4);
    public static final Block LEAVES = new Block(4,6);
    public static final Block SAND = new Block(6,7);
    public static final Block WATER = new Block(7,8);

    // Block SIZE in units
    public static final float SIZE = 0.8f;
    public static float shift = .25f;

    private byte id = 0;
    private int textureID = -1;
    private int sideTextureId = -1;

    public Block(int id, int textureID) {
        this.id = (byte) id;
        this.textureID = textureID;
        this.sideTextureId = textureID;
    }

    public Block(int id, int textureID, int sideTextureId) {
        this.id = (byte) id;
        this.textureID = textureID;
        this.sideTextureId = sideTextureId;
    }

    public byte getID() {
        return (byte) id;
    }

    public int getTextureID() {
        return textureID;
    }

    public static float getTexX(int texture) {
        float u = texture % 4;
        return u * .25f + .25f;

    }

    public static float getTexY(int texture) {
        float v = texture / 4;
        return v * .25f + .25f;
    }

    public static void main(String[] args) {
        System.out.println("x " + getTexX(0));
        System.out.println(getTexY(0));
        System.out.println("x " + getTexX(1));
        System.out.println(getTexY(1));
        System.out.println("x " + getTexX(2));
        System.out.println(getTexY(2));
        System.out.println("x " + getTexX(3));
        System.out.println(getTexY(3));
        System.out.println("x " + getTexX(4));
        System.out.println(getTexY(4));
        System.out.println("x " + getTexX(5));
        System.out.println(getTexY(5));

    }

    public int getSideTexture() {
        if(sideTextureId == -1) return 2;
        else return sideTextureId;
    }

    public void renderTempBlock(final Chunk chunk, final int x, final int y, final int z) {
        if(getTextureID() == -1 || getSideTexture() == -1) return;

        boolean renderTop = true;
        boolean renderBottom = true;

        boolean renderLeft = true;
        boolean renderRight = true;

        boolean renderFront = true;
        boolean renderBack = true;

        final float xO = chunk.getChunkX() * 32 * SIZE;
        final float yO = chunk.getChunkY() * 32 * SIZE;
        final float zO = chunk.getChunkZ() * 32 * SIZE;

        final float xSIZE = SIZE;
        final float zSIZE = SIZE;
        final float floor = SIZE * 2;
        final float ceiling = 0;

        glBindTexture(GL_TEXTURE_2D, 5);

        int texid = getTextureID();

        glPushMatrix();
        {
            // Move to location of block
            glTranslatef(xO + (x * SIZE * 2), yO + (y * SIZE * 2), zO + (z * SIZE * 2));

            if (renderBottom) {
                glBegin(GL_QUADS);
                {
                    float xt = getTexX(texid);
                    float yt = getTexY(texid);

                    glTexCoord2f(xt - shift, yt-shift);
                    glVertex3f(xSIZE, floor, -zSIZE);
                    glTexCoord2f(xt, yt-shift);
                    glVertex3f(xSIZE, floor, zSIZE);
                    glTexCoord2f(xt, yt);
                    glVertex3f(-xSIZE, floor, zSIZE);
                    glTexCoord2f(xt - shift, yt);
                    glVertex3f(-xSIZE, floor, -zSIZE);
                }
                glEnd();
            }

            if (renderTop) {
                glBegin(GL_QUADS);
                {
                    float xt = getTexX(texid);
                    float yt = getTexY(texid);

                    glTexCoord2f(xt - shift, yt-shift);
                    glVertex3f(xSIZE, ceiling, -zSIZE);
                    glTexCoord2f(xt, yt-shift);
                    glVertex3f(xSIZE, ceiling, zSIZE);
                    glTexCoord2f(xt, yt);
                    glVertex3f(-xSIZE, ceiling, zSIZE);
                    glTexCoord2f(xt - shift, yt);
                    glVertex3f(-xSIZE, ceiling, -zSIZE);
                }
                glEnd();
            }

            texid = getSideTexture();

            if (renderLeft) {

                glBegin(GL_QUADS);
                {
                    float xt = getTexX(texid);
                    float yt = getTexY(texid);

                    glTexCoord2f(xt - shift, yt-shift);
                    glVertex3f(-xSIZE, ceiling, -zSIZE);
                    glTexCoord2f(xt, yt-shift);
                    glVertex3f(-xSIZE, ceiling, zSIZE);
                    glTexCoord2f(xt, yt);
                    glVertex3f(-xSIZE, floor, zSIZE);
                    glTexCoord2f(xt - shift, yt);
                    glVertex3f(-xSIZE, floor, -zSIZE);
                }
                glEnd();
            }

            if (renderRight) {
                glBegin(GL_QUADS);
                {
                    float xt = getTexX(texid);
                    float yt = getTexY(texid);

                    glTexCoord2f(xt - shift, yt - shift);
                    glVertex3f(xSIZE, ceiling, -zSIZE);
                    glTexCoord2f(xt, yt - shift);
                    glVertex3f(xSIZE, ceiling, zSIZE);
                    glTexCoord2f(xt, yt);
                    glVertex3f(xSIZE, floor, zSIZE);
                    glTexCoord2f(xt - shift, yt);
                    glVertex3f(xSIZE, floor, -zSIZE);
                }
                glEnd();
            }

            if (renderFront) {
                glBegin(GL_QUADS);
                {
                    float xt = getTexX(texid);
                    float yt = getTexY(texid);

                    glTexCoord2f(xt - shift, yt-shift);
                    glVertex3f(-xSIZE, ceiling, -zSIZE);
                    glTexCoord2f(xt, yt - shift);
                    glVertex3f(xSIZE, ceiling, -zSIZE);
                    glTexCoord2f(xt, yt);
                    glVertex3f(xSIZE, floor, -zSIZE);
                    glTexCoord2f(xt - shift, yt);
                    glVertex3f(-xSIZE, floor, -zSIZE);
                }
                glEnd();
            }

            if (renderBack) {
                glBegin(GL_QUADS);
                {
                    float xt = getTexX(texid);
                    float yt = getTexY(texid);

                    glTexCoord2f(xt - shift, yt-shift);
                    glVertex3f(-xSIZE, ceiling, zSIZE);
                    glTexCoord2f(xt, yt - shift);
                    glVertex3f(xSIZE, ceiling, zSIZE);
                    glTexCoord2f(xt, yt);
                    glVertex3f(xSIZE, floor, zSIZE);
                    glTexCoord2f(xt - shift, yt);
                    glVertex3f(-xSIZE, floor, zSIZE);
                }
                glEnd();
            }
        }
        glPopMatrix();
    }

    public void rebuild(final Chunk chunk, final int x, final int y, final int z) {
        if(getTextureID() == -1 || getSideTexture() == -1) return;

        boolean renderTop = chunk.isAir(x, y - 1, z);
        boolean renderBottom = chunk.isAir(x, y + 1, z);

        boolean renderLeft = chunk.isAir(x - 1, y, z);
        boolean renderRight = chunk.isAir(x + 1, y, z);

        boolean renderFront = chunk.isAir(x, y, z - 1);
        boolean renderBack = chunk.isAir(x, y, z + 1);
//
        final float xO = chunk.getChunkX() * 32 * SIZE;
        final float yO = chunk.getChunkY() * 32 * SIZE;
        final float zO = chunk.getChunkZ() * 32 * SIZE;

        final float xSIZE = SIZE;
        final float zSIZE = SIZE;
        float floor = SIZE * 2;
        float ceiling = 0;

        int texid = getTextureID();
//
        // Move to location of block
        float xp = xO + (x * SIZE * 2);
        float yp = yO + (y * SIZE * 2);
        float zp = zO + (z * SIZE * 2);

        floor += yp;
        ceiling += yp;

        if (renderBottom) {
            float xt = getTexX(texid);
            float yt = getTexY(texid);

            glTexCoord2f(xt - shift, yt-shift);
            glVertex3f(xp + xSIZE, floor, zp + -zSIZE);
            glTexCoord2f(xt, yt-shift);
            glVertex3f(xp + xSIZE, floor, zp + zSIZE);
            glTexCoord2f(xt, yt);
            glVertex3f(xp + -xSIZE, floor, zp + zSIZE);
            glTexCoord2f(xt - shift, yt);
            glVertex3f(xp + -xSIZE, floor, zp + -zSIZE);
        }

        if (renderTop) {
            float xt = getTexX(texid);
            float yt = getTexY(texid);

            glTexCoord2f(xt - shift, yt-shift);
            glVertex3f(xp + xSIZE, ceiling, zp + -zSIZE);
            glTexCoord2f(xt, yt-shift);
            glVertex3f(xp + xSIZE, ceiling, zp + zSIZE);
            glTexCoord2f(xt, yt);
            glVertex3f(xp + -xSIZE, ceiling, zp + zSIZE);
            glTexCoord2f(xt - shift, yt);
            glVertex3f(xp + -xSIZE, ceiling, zp + -zSIZE);
        }

        texid = getSideTexture();

        if (renderLeft) {
            float xt = getTexX(texid);
            float yt = getTexY(texid);

            glTexCoord2f(xt - shift, yt-shift);
            glVertex3f(xp + -xSIZE, ceiling, zp + -zSIZE);
            glTexCoord2f(xt, yt-shift);
            glVertex3f(xp + -xSIZE, ceiling, zp + zSIZE);
            glTexCoord2f(xt, yt);
            glVertex3f(xp + -xSIZE, floor, zp + zSIZE);
            glTexCoord2f(xt - shift, yt);
            glVertex3f(xp + -xSIZE, floor, zp + -zSIZE);
        }

        if (renderRight) {
            float xt = getTexX(texid);
            float yt = getTexY(texid);

            glTexCoord2f(xt - shift, yt - shift);
            glVertex3f(xp + xSIZE, ceiling, zp + -zSIZE);
            glTexCoord2f(xt, yt - shift);
            glVertex3f(xp + xSIZE, ceiling, zp + zSIZE);
            glTexCoord2f(xt, yt);
            glVertex3f(xp + xSIZE, floor, zp + zSIZE);
            glTexCoord2f(xt - shift, yt);
            glVertex3f(xp + xSIZE, floor, zp + -zSIZE);
        }

        if (renderFront) {
            float xt = getTexX(texid);
            float yt = getTexY(texid);

            glTexCoord2f(xt - shift, yt-shift);
            glVertex3f(xp + -xSIZE, ceiling, zp + -zSIZE);
            glTexCoord2f(xt, yt - shift);
            glVertex3f(xp + xSIZE, ceiling, zp + -zSIZE);
            glTexCoord2f(xt, yt);
            glVertex3f(xp + xSIZE, floor, zp + -zSIZE);
            glTexCoord2f(xt - shift, yt);
            glVertex3f(xp + -xSIZE, floor, zp + -zSIZE);
        }

        if (renderBack) {
            float xt = getTexX(texid);
            float yt = getTexY(texid);

            glTexCoord2f(xt - shift, yt-shift);
            glVertex3f(xp + -xSIZE, ceiling, zp + zSIZE);
            glTexCoord2f(xt, yt - shift);
            glVertex3f(xp + xSIZE, ceiling, zp + zSIZE);
            glTexCoord2f(xt, yt);
            glVertex3f(xp + xSIZE, floor, zp + zSIZE);
            glTexCoord2f(xt - shift, yt);
            glVertex3f(xp + -xSIZE, floor, zp + zSIZE);
        }
    }

    public static Block getBlockByID(byte id) {
        return null;
    }
}
