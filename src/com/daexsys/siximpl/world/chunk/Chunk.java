package com.daexsys.siximpl.world.chunk;

import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.siximpl.BlockCoord;
import com.daexsys.siximpl.SBC;
import com.daexsys.siximpl.entity.Player;
import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.planet.Planet;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

public class Chunk {
    // The amount of blocks in each axis the chunk can hold.
    public static final int CHUNK_SIZE = 16;

    // The planet this chunk is on
    private Planet world = SBC.getUniverse().getPlanetAt(0, 0, 0);

    private int chunkX = 0;
    private int chunkY = 0;
    private int chunkZ = 0;

    // A lock designed to prevent the application from rebuilding while it's already rebuilding
    private boolean rebuilding = false;

    // The id number of this chunk's display list, used for rendering
    public int displayListNumber = -1;

    // The blocks stored in this chunk
    private Block[][][] blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];

    // Whether or not there is actually anything in this chunk. If not, it's rendering can be skipped.
    private boolean empty = true;

    // Blocks in this chunk not integrated into the display list
    private Set<BlockCoord> tempBlocks = new HashSet<BlockCoord>();
    private Set<BlockCoord> pTempBlocks = new HashSet<BlockCoord>();

    public Chunk(int x, int y, int z) {
        this.chunkX = x;
        this.chunkY = y;
        this.chunkZ = z;


        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                for (int k = 0; k < CHUNK_SIZE; k++) {
                    blocks[i][j][k] = Block.AIR;
                }
            }
        }
    }


    public void setXYArea(Block block, int level) {
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                blocks[i][level][j] = block;
            }
        }
    }

    public Block getBlock(int x, int y, int z) {
        // If within this chunk...
        if(x >= 0 && x <= CHUNK_SIZE - 1 &&
                y >= 0 && y <= CHUNK_SIZE - 1 &&
                z >= 0 && z <= CHUNK_SIZE - 1) {
            return blocks[x][y][z];
        }

        return null;
    }

    public void setBlock(int x, int y, int z, Block block) {
        empty=false;
        if(x >= 0 && x <= CHUNK_SIZE - 1
                && y >= 0 && y <= CHUNK_SIZE - 1
                && z >= 0 && z <= CHUNK_SIZE - 1) {
            blocks[x][y][z] = block;
        }

        IjWindow.addProcess(new Renderer() {
            @Override
            public void render() {
                rebuildRenderGeometry();
                IjWindow.removeProcess(this);
            }
        });
        ;
    }

    public void setInvisibleBlock(int x, int y, int z, Block block) {
        if(block != Block.AIR) {
            empty = false;
        }

        if(x >= 0 && x <= CHUNK_SIZE - 1
                && y >= 0 && y <= CHUNK_SIZE - 1
                && z >= 0 && z <= CHUNK_SIZE - 1) {
            blocks[x][y][z] = block;
        }
    }

    public void setBlockNoRebuild(int x, int y, int z, Block block) {
        if(block != Block.AIR) {
            empty = false;
        }

        tempBlocks.add(new BlockCoord(x, y, z));

        if(x >= 0 && x <= CHUNK_SIZE - 1
                && y >= 0 && y <= CHUNK_SIZE - 1
                && z >= 0 && z <= CHUNK_SIZE - 1) {
            blocks[x][y][z] = block;
        }
    }

    public void setPlanet(Planet world) {
        this.world = world;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public Planet getWorld() {
        return world;
    }

    public boolean isAir(int x, int y, int z) {
        if(x >= 0 && x <= CHUNK_SIZE - 1
            && y >= 0 && y <= CHUNK_SIZE - 1
            && z >= 0 && z <= CHUNK_SIZE - 1) {

            return blocks[x][y][z].getID() == 0;
        }

//        Planet world = getWorld();
//
//        if(x == -1) {
//            Chunk chunk = world.getChunk(getChunkX() - 1, getChunkY(), getChunkZ());
//            if(chunk == null) {
//                return true;
//            }
//
//            return chunk.isAir(CHUNK_SIZE - 1, y, z);
//        }
//
//        if(y == CHUNK_SIZE) {
//            Chunk chunk = world.getChunk(getChunkX(), getChunkY() + 1, getChunkZ());
//            if(chunk == null) {
//                return true;
//            }
//            return chunk.isAir(x, 0, z);
//        }
//
//        if(y == -1) {
//            Chunk chunk = world.getChunk(getChunkX(), getChunkY() - 1, getChunkZ());
//            if(chunk == null) {
//                return true;
//            }
//
//            return chunk.isAir(x, CHUNK_SIZE - 1, z);
//        }
//
//        if(x == CHUNK_SIZE) {
//            Chunk chunk = world.getChunk(getChunkX() + 1, getChunkY(), getChunkZ());
//            if(chunk == null) {
//                return true;
//            }
//            return chunk.isAir(0, y, z);
//        }
//
//        if(z == -1) {
//            Chunk chunk = world.getChunk(getChunkX(), getChunkY(), getChunkZ() - 1);
//            if(chunk == null) {
//                return true;
//            }
//
//            return chunk.isAir(x, y, CHUNK_SIZE - 1);
//        }
//
//        if(z == CHUNK_SIZE) {
//            Chunk chunk = world.getChunk(getChunkX(), getChunkY(), getChunkZ()  + 1);
//            if(chunk == null) {
//                return true;
//            }
//            return chunk.isAir(x, y, 0);
//        }

        return true;
    }

    /**
     * Create display list for chunk
     */
    public void rebuildRenderGeometry() {
        try {
            if (rebuilding || empty) return;

            displayListNumber = GL11.glGenLists(1);
            GL11.glNewList(displayListNumber, GL11.GL_COMPILE);

            glBindTexture(GL_TEXTURE_2D, 1);

            if (!rebuilding) {
                glBegin(GL_QUADS);
                {
                    final Chunk theChunk = this;
                    rebuilding = true;

                    for (int i = 0; i < CHUNK_SIZE; i++) {
                        for (int j = 0; j < CHUNK_SIZE; j++) {
                            for (int k = 0; k < CHUNK_SIZE; k++) {
                                Block block = blocks[i][j][k];

                                try {
                                    block.build(theChunk, i, j, k);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    rebuilding = false;
                }
                glEnd();
            }

            GL11.glEndList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearTempBlocks() {
        tempBlocks.clear();
    }

    public void render() {
        try {
            if(empty) return;
            if(rebuilding) return;

            Player player = SBC.getPlayer();

            int cX = player.getChunkX();
            int cY = player.getChunkY() * -1;
            int cZ = player.getChunkZ();

            int viewDistance = 10;

            for(BlockCoord blockCoord : tempBlocks) {
                Block block = getBlock(blockCoord.x, blockCoord.y, blockCoord.z);
                try {
                    block.renderTempBlock(this, blockCoord.x, blockCoord.y, blockCoord.z);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(cX > getChunkX() - viewDistance && cX < getChunkX() + viewDistance) {
                if (cY > getChunkY() - viewDistance && cY < getChunkY() + viewDistance) {
                    if (cZ > getChunkZ() - viewDistance && cZ < getChunkZ() + viewDistance) {
                        double anglePlayer = player.getYaw() % 360;
                        double pitchPlayer = player.getPitch() % 360;

                        if (anglePlayer < 0) {
                            anglePlayer = 360 + anglePlayer;
                        }

                        boolean render = true;

                        if (pitchPlayer < 20) {
    //                        if (anglePlayer > 45 && anglePlayer < 135) {
    //                            if (getChunkX() < player.getChunkX()) {
    //                                render = false;
    //                            }
    //                        }
    //
    //                        if (anglePlayer > 225 && anglePlayer < 315) {
    //                            if (getChunkX() > player.getChunkX()) {
    //                                render = false;
    //                            }
    //                        }
    //
    //                        // 90 270
    //                        // 270 90
    //                        if (anglePlayer > 135 && anglePlayer < 225) {
    //                            if (getChunkZ() < player.getChunkZ()) {
    //                                render = false;
    //                            }
    //                        }
    ////
    //                        if (anglePlayer > 315 && anglePlayer < 45) {
    //                            if (getChunkZ() > player.getChunkZ()) {
    //                                render = false;
    //                            }
    //                        }
                        }

                        if (render) {
                            try {
                                // Render built chunk displaylist
                                GL11.glCallList(displayListNumber);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
