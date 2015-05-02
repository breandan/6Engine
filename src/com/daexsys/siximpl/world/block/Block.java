package com.daexsys.siximpl.world.block;

import com.daexsys.siximpl.world.chunk.Chunk;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Block {
    // Default blocks, refactor
    public static final Block AIR = new Air();
    public static final Block GRASS = new Block(1,0,1);
    public static final Block DIRT = new Block(2,2);
    public static final Block STONE = new Block(5,3);
    public static final Block WOOD = new Block(3,4);
    public static final Block LEAVES = new Block(4,6);
    public static final Block SAND = new Block(6,7);
    public static final Block WATER = new Block(7,8);

    // Block size in GL units
    public static final float BLOCK_SIZE = 1f;

    // Texture shift
    public static float shift = .25f;

    // The byte id of the block, refactor into string system
    private byte id = 0;

    // The texture ids
    private int textureID = -1;
    private int sideTextureId = -1;

    /**
     * Constructor for generic block with the same texture on all six sides
     * @param id the byte id of the block
     * @param textureID the texture id of the block (for all six sides)
     */
    public Block(int id, int textureID) {
        this.id = (byte) id;
        this.textureID = textureID;
        this.sideTextureId = textureID;
    }

    /**
     * Constructor for generic block with the same texture on the top and bottom, and a different texture on the side
     * @param id the byte id of the block
     * @param textureID the texture id of the block top and bottom
     * @param sideTextureId the texture id of the block sides
     */
    public Block(int id, int textureID, int sideTextureId) {
        this.id = (byte) id;
        this.textureID = textureID;
        this.sideTextureId = sideTextureId;
    }

    /**
     * Returns the id of the block.
     * @return the byte id of the block
     */
    public byte getID() {
        return id;
    }

    /**
     * @return the texture of the block top and bottom, or all six sides if there is no side texture
     */
    public int getTextureID() {
        return textureID;
    }

    public int getSideTexture() {
        if(sideTextureId == -1) return 2;
        else return sideTextureId;
    }

    /**
     * Gets the location of a block's texture in the atlas.
     * @param texture the texture id
     * @return the X location in the texture atlas
     */
    public static float getTexX(int texture) {
        float u = texture % 4;
        return u * .25f + .25f;
    }

    /**
     * Gets the location of a block's texture in the atlas.
     * @param texture the texture id
     * @return the Y location in the texture atlas
     */
    public static float getTexY(int texture) {
        float v = texture / 4;
        return v * .25f + .25f;
    }

    /**
     * Builds the 3D model of the block, as it will be displayed in a chunk displaylist.
     * @param chunk the chunk the block is in
     * @param x the X coordinate of the block
     * @param y the Y coordinate of the block
     * @param z the Z coordinate of the block
     */
    public void build(final Chunk chunk, final int x, final int y, final int z) {
        // If either texture is -1, return, because the block is air
        if(getTextureID() == -1 || getSideTexture() == -1) return;

        // Determine whether the adjecent blocks to this one are air.
        // If they aren't, there is no need to render the face touching it,
        // because it is invisible.
        boolean renderTop = chunk.isAir(x, y - 1, z);
        boolean renderBottom = chunk.isAir(x, y + 1, z);
        boolean renderLeft = chunk.isAir(x - 1, y, z);
        boolean renderRight = chunk.isAir(x + 1, y, z);
        boolean renderFront = chunk.isAir(x, y, z - 1);
        boolean renderBack = chunk.isAir(x, y, z + 1);

        // Determine the location of the chunk within the rendering world
        final float xO = chunk.getChunkX() * (Chunk.CHUNK_SIZE * 2) * BLOCK_SIZE;
        final float yO = chunk.getChunkY() * (Chunk.CHUNK_SIZE * 2) * BLOCK_SIZE;
        final float zO = chunk.getChunkZ() * (Chunk.CHUNK_SIZE * 2) * BLOCK_SIZE;

        // Set the dimensions of blocks
        final float xSIZE = BLOCK_SIZE;
        final float zSIZE = BLOCK_SIZE;
        float floor = BLOCK_SIZE * 2;
        float ceiling = 0;

        // Get the texture id
        int texid = getTextureID();
//
        // Move to location of block
        float xp = xO + (x * BLOCK_SIZE * 2);
        float yp = yO + (y * BLOCK_SIZE * 2);
        float zp = zO + (z * BLOCK_SIZE * 2);

        floor += yp;
        ceiling += yp;

        // Attempt to render the bottom face
        if (renderBottom) {
            float xt = getTexX(texid);
            float yt = getTexY(texid);

            glTexCoord2f(xt - shift, yt - shift);
            glVertex3f(xp + xSIZE, floor, zp + -zSIZE);

            glTexCoord2f(xt, yt - shift);
            glVertex3f(xp + xSIZE, floor, zp + zSIZE);

            glTexCoord2f(xt, yt);
            glVertex3f(xp + -xSIZE, floor, zp + zSIZE);

            glTexCoord2f(xt - shift, yt);
            glVertex3f(xp + -xSIZE, floor, zp + -zSIZE);
        }

        // Attempt to render the top face
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

        // Attempt to render the left face
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

        // Attempt to render the right face
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

        // Attempt to render the front face
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

        // Attempt to render the back face
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

    /**
     * Get a block for a certain id value
     */
    public static Block getBlockByID(byte id) {
        if(id == 1) return GRASS;
        if(id == 2) return DIRT;
        if(id == 3) return WOOD;
        if(id == 4) return LEAVES;
        if(id == 5) return STONE;
        if(id == 6) return SAND;
        if(id == 7) return WATER;

        return Block.AIR;
    }

    @Deprecated
    public void renderTempBlock(final Chunk chunk, final int x, final int y, final int z) {
        if(getTextureID() == -1 || getSideTexture() == -1) return;

        boolean renderTop = true;
        boolean renderBottom = true;

        boolean renderLeft = true;
        boolean renderRight = true;

        boolean renderFront = true;
        boolean renderBack = true;

        final float xO = chunk.getChunkX() * 32 * BLOCK_SIZE;
        final float yO = chunk.getChunkY() * 32 * BLOCK_SIZE;
        final float zO = chunk.getChunkZ() * 32 * BLOCK_SIZE;

        final float xSIZE = BLOCK_SIZE;
        final float zSIZE = BLOCK_SIZE;
        final float floor = BLOCK_SIZE * 2;
        final float ceiling = 0;

        glBindTexture(GL_TEXTURE_2D, 5);

        int texid = getTextureID();

        glPushMatrix();
        {
            // Move to location of block
            glTranslatef(xO + (x * BLOCK_SIZE * 2), yO + (y * BLOCK_SIZE * 2), zO + (z * BLOCK_SIZE * 2));

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
}
