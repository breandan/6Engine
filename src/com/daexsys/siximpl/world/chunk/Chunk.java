package com.daexsys.siximpl.world.chunk;

import com.daexsys.ijen3D.IjWindow;
import com.daexsys.ijen3D.Renderer;
import com.daexsys.siximpl.BlockCoord;
import com.daexsys.siximpl.SBC;
import com.daexsys.siximpl.entity.Player;
import com.daexsys.siximpl.world.block.Air;
import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.planet.Planet;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Chunk {
    public Set<Runnable> renderOperations = new HashSet<Runnable>();

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
    private Block[][][] blocks = new Block[16][16][16];

    // Blocks in this chunk not integrated into the display list
    private Set<BlockCoord> tempBlocks = new HashSet<BlockCoord>();

    public Chunk(int x, int y, int z) {
        this.chunkX = x;
        this.chunkY = y;
        this.chunkZ = z;


        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    blocks[i][j][k] = Block.AIR;
                }
            }
        }
    }


    public void setXYArea(Block block, int level) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                blocks[i][level][j] = block;
            }
        }
    }


    public void setXYRandom(int level) {
        Random random = new Random();

        Block block = null;

        int integer = random.nextInt(2);

//        if(integer == 0) block = air;
        if(integer == 0) block = Block.GRASS;
        if(integer == 1) block = Block.DIRT;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                blocks[i][level][j] = block;
            }
        }
    }

    public Block getBlock(int x, int y, int z) {
        if(x >= 0 && x <= 15 && y >= 0 && y <= 15 && z >= 0 && z <= 15) {
            return blocks[x][y][z];
        }

        return null;
    }

    public void setBlock(int x, int y, int z, Block block) {
        if(x >= 0 && x <= 15 && y >= 0 && y <= 15 && z >= 0 && z <= 15) {
            blocks[x][y][z] = block;
        }
        IjWindow.addRenderer(new Renderer() {
            @Override
            public void render() {
                rebuildRenderGeometry();
            }
        });
        ;
    }

    public void setInvisibleBlock(int x, int y, int z, Block block) {
        if(x >= 0 && x <= 15 && y >= 0 && y <= 15 && z >= 0 && z <= 15) {
            blocks[x][y][z] = block;
        }
    }

    public void setBlockNoRebuild(int x, int y, int z, Block block) {
        if(getBlock(x, y, z) != null) {
//            if(getBlock(x, y, z).getID() != block.getID())
                tempBlocks.add(new BlockCoord(x, y, z));
        }

        if(x >= 0 && x <= 15 && y >= 0 && y <= 15 && z >= 0 && z <= 15) {
            blocks[x][y][z] = block;
        }

//        if(block.getID() == 0) {
//        }
//        else {
//            tempBlocks.remove(new BlockCoord(x, y, z));
//        }
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
        if(x >= 0 && x <= 15 && y >= 0 && y <= 15 && z >= 0 && z <= 15) {
            return blocks[x][y][z].getID() == 0;
        }

        Planet world = getWorld();

        if(x == -1) {
            Chunk chunk = world.getChunk(getChunkX() - 1, getChunkY(), getChunkZ());
            if(chunk == null) {
                return true;
            }

            return chunk.isAir(15, y, z);
        }

        if(y == 16) {
            Chunk chunk = world.getChunk(getChunkX(), getChunkY() + 1, getChunkZ());
            if(chunk == null) {
                return true;
            }
            return chunk.isAir(x, 0, z);
        }

        if(y == -1) {
            Chunk chunk = world.getChunk(getChunkX(), getChunkY() - 1, getChunkZ());
            if(chunk == null) {
                return true;
            }

            return chunk.isAir(x, 15, z);
        }

        if(x == 16) {
            Chunk chunk = world.getChunk(getChunkX() + 1, getChunkY(), getChunkZ());
            if(chunk == null) {
                return true;
            }
            return chunk.isAir(0, y, z);
        }

        if(z == -1) {
            Chunk chunk = world.getChunk(getChunkX(), getChunkY(), getChunkZ() - 1);
            if(chunk == null) {
                return true;
            }

            return chunk.isAir(x, y, 15);
        }

        if(z == 16) {
            Chunk chunk = world.getChunk(getChunkX(), getChunkY(), getChunkZ()  + 1);
            if(chunk == null) {
                return true;
            }
            return chunk.isAir(x, y, 0);
        }

        return true;
    }

    public void rebuildRenderGeometry() {
        displayListNumber = GL11.glGenLists(1);
        GL11.glNewList(displayListNumber, GL11.GL_COMPILE);

        renderOperations.clear();
        if(!rebuilding) {
            final Chunk theChunk = this;
                    rebuilding = true;

            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    for (int k = 0; k < 16; k++) {
                        Block block = blocks[i][j][k];
                        try { block.rebuild(theChunk, i, j, k);
                    } catch (Exception e) {}
                    }
                }
            }
            rebuilding = false;
        }

        GL11.glEndList();
    }

    public void clearTempBlocks() {
        tempBlocks.clear();
    }

    public void render() {
        Player player = SBC.getPlayer();

        int cX = player.getChunkX();
        int cY = player.getChunkY() * -1;
        int cZ = player.getChunkZ();

        int vd = 6;
//       GL11.glPushMatrix();
        {

            for(BlockCoord blockCoord : tempBlocks) {
//                            System.out.println("bp: " + blockCoord.x + " " + blockCoord.y + " " + blockCoord.z);
                Block block = getBlock(blockCoord.x, blockCoord.y, blockCoord.z);
                try {
                    block.renderSix(this, blockCoord.x, blockCoord.y, blockCoord.z);
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }

//            GL11.glPopMatrix();
        }
        if(cX > getChunkX() - vd && cX < getChunkX() + vd) {
            if(cY > getChunkY() - vd && cY < getChunkY() + vd) {
                if(cZ > getChunkZ() - vd && cZ < getChunkZ() + vd) {
//                    double angleToPlayer = Math.atan2(getChunkY() - cY, getChunkX() - cX);
//                    double degreeAngleToPlayer = Math.toDegrees(angleToPlayer);
//                    double anglePlayer = player.getYaw() % 360;
//
//                    if(anglePlayer < 0) {
//                        anglePlayer = 360 + anglePlayer;
//                    }
//
//                    if(degreeAngleToPlayer < 0) {
//                        degreeAngleToPlayer = 360 + degreeAngleToPlayer;
//                    }
//
//                    degreeAngleToPlayer = degreeAngleToPlayer > 180 ? 360 - degreeAngleToPlayer : degreeAngleToPlayer;
//                    anglePlayer = degreeAngleToPlayer > 180 ? 360 - degreeAngleToPlayer : degreeAngleToPlayer;

//                        double dist = Math.abs(degreeAngleToPlayer - anglePlayer);
//
//                        if (dist > 180) dist = 360 - dist;
//                        if (Math.abs(dist) < 65) {

//                        System.out.println("rendering: " + displayListNumber);


                    GL11.glPushMatrix();
                    {
                        // Render built chunk displaylist
                        GL11.glCallList(displayListNumber);
                    }
                    GL11.glPopMatrix();



//                        }


//                    for (Runnable runnable : renderOperations) {
//                        runnable.run();
//                    }
                }
            }
        }
    }
}
