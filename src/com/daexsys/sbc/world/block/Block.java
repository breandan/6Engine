package com.daexsys.sbc.world.block;

import com.daexsys.sbc.world.chunk.Chunk;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Block {
    public static final Block DIRT = new Block(2,1);
    public static final Block GRASS = new Block(1,3);
    public static final Block LEAVES = new Block(4,8);
    public static final Block STONE = new Block(5,9);
    public static final Block WOOD = new Block(3,7);

    private int id = 0;
    private int textureID = 3;

    public Block(int id, int textureID) {
        this.id = id;
        this.textureID = textureID;
    }

    public int getID() {
        return id;
    }

    public int getTextureID() {
        return textureID;
    }

    public void rebuild(final Chunk chunk, final int x, final int y, final int z) {
        final float size = 0.8f;

        boolean renderTop = chunk.isAir(x, y - 1, z);
        boolean renderBottom = chunk.isAir(x, y + 1, z);

        boolean renderLeft = chunk.isAir(x - 1, y, z);
        boolean renderRight = chunk.isAir(x + 1, y, z);

        boolean renderFront = chunk.isAir(x, y, z - 1);
        boolean renderBack = chunk.isAir(x, y, z + 1);

        final float xO = chunk.getChunkX() * 32 * size;
        final float yO = chunk.getChunkY() * 32 * size;
        final float zO = chunk.getChunkZ() * 32 * size;

        final float xSize = size;
        final float zSize = size;
        final float floor = size * 2;
        final float ceiling = 0;

//        RenderUtils.renderCube(
//                size, size, size,
//                size * 2, 0, 3);

        if (renderBottom) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    glPushMatrix();
                    {
                        glTranslatef(xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2));

                        glBindTexture(GL_TEXTURE_2D, textureID);

                        glBegin(GL_QUADS);
                        {
                            glTexCoord2f(0, 0);
                            glVertex3f(xSize, floor, -zSize);
                            glTexCoord2f(1, 0);
                            glVertex3f(xSize, floor, zSize);
                            glTexCoord2f(1, 1);
                            glVertex3f(-xSize, floor, zSize);
                            glTexCoord2f(0, 1);
                            glVertex3f(-xSize, floor, -zSize);
                        }
                        glEnd();
                    }
                    glPopMatrix();
                }
            };

            chunk.renderOperations.add(new RenderOp(runnable, x, y, z, BlockFace.BOTTOM));
        }

        if (renderTop) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    glPushMatrix();
                    {
                        glTranslatef(xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2));

                        glBindTexture(GL_TEXTURE_2D, textureID);

                        glBegin(GL_QUADS);
                        {
                            glTexCoord2f(0, 0);
                            glVertex3f(xSize, ceiling, -zSize);
                            glTexCoord2f(1, 0);
                            glVertex3f(xSize, ceiling, zSize);
                            glTexCoord2f(1, 1);
                            glVertex3f(-xSize, ceiling, zSize);
                            glTexCoord2f(0, 1);
                            glVertex3f(-xSize, ceiling, -zSize);
                        }
                        glEnd();
                    }
                    glPopMatrix();
                }
            };

            chunk.renderOperations.add(new RenderOp(runnable, x, y, z, BlockFace.TOP));
        }

        if (renderLeft) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    glPushMatrix();
                    {
                        glTranslatef(xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2));

                        glBindTexture(GL_TEXTURE_2D, textureID);

                        glBegin(GL_QUADS);
                        {
                            glTexCoord2f(0, 0);
                            glVertex3f(-xSize, ceiling, -zSize);
                            glTexCoord2f(1, 0);
                            glVertex3f(-xSize, ceiling, zSize);
                            glTexCoord2f(1, 1);
                            glVertex3f(-xSize, floor, zSize);
                            glTexCoord2f(0, 1);
                            glVertex3f(-xSize, floor, -zSize);
                        }
                        glEnd();
                    }
                    glPopMatrix();
                }
            };

            chunk.renderOperations.add(new RenderOp(runnable, xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2), BlockFace.LEFT));
        }

        if (renderRight) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    glPushMatrix();
                    {
                        glTranslatef(xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2));

                        glBindTexture(GL_TEXTURE_2D, textureID);

                        glBegin(GL_QUADS);
                        {
                            glTexCoord2f(0, 0);
                            glVertex3f(xSize, ceiling, -zSize);
                            glTexCoord2f(1, 0);
                            glVertex3f(xSize, ceiling, zSize);
                            glTexCoord2f(1, 1);
                            glVertex3f(xSize, floor, zSize);
                            glTexCoord2f(0, 1);
                            glVertex3f(xSize, floor, -zSize);
                        }
                        glEnd();
                    }
                    glPopMatrix();
                }
            };

            chunk.renderOperations.add(new RenderOp(runnable,xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2), BlockFace.RIGHT));
        }

        if (renderFront) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    glPushMatrix();
                    {
                        glTranslatef(xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2));

                        glBindTexture(GL_TEXTURE_2D, textureID);

                        glBegin(GL_QUADS);
                        {
                            glTexCoord2f(0, 0);
                            glVertex3f(-xSize, ceiling, -zSize);
                            glTexCoord2f(1, 0);
                            glVertex3f(xSize, ceiling, -zSize);
                            glTexCoord2f(1, 1);
                            glVertex3f(xSize, floor, -zSize);
                            glTexCoord2f(0, 1);
                            glVertex3f(-xSize, floor, -zSize);
                        }
                        glEnd();
                    }
                    glPopMatrix();
                }
            };

            chunk.renderOperations.add(new RenderOp(runnable, xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2), BlockFace.FRONT));
        }

        if (renderBack) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    glPushMatrix();
                    {
                        glTranslatef(xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2));

                        glBindTexture(GL_TEXTURE_2D, textureID);

                        glBegin(GL_QUADS);
                        {
                            glTexCoord2f(0, 0);
                            glVertex3f(-xSize, ceiling, zSize);
                            glTexCoord2f(1, 0);
                            glVertex3f(xSize, ceiling, zSize);
                            glTexCoord2f(1, 1);
                            glVertex3f(xSize, floor, zSize);
                            glTexCoord2f(0, 1);
                            glVertex3f(-xSize, floor, zSize);
                        }
                        glEnd();
                    }
                    glPopMatrix();
                }
            };

            chunk.renderOperations.add(new RenderOp(runnable, xO + (x * size * 2), yO + (y * size * 2), zO + (z * size * 2), BlockFace.BACK));
        }
    }

    public static Block getBlockByID(byte id) {
        return null;
    }
}
