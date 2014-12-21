package com.daexsys.sbc.world.chunk;

import com.daexsys.sbc.SBC;
import com.daexsys.sbc.entity.Player;
import com.daexsys.sbc.world.block.Air;
import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.planet.Planet;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Chunk {
    public Set<Runnable> renderOperations = new HashSet<Runnable>();

    private Planet world = SBC.getUniverse().getPlanetAt(0, 0, 0);

    private int chunkX = 0;
    private int chunkY = 0;
    private int chunkZ = 0;

    public Air air = new Air();

    private boolean rebuilding = false;

    private Block[][][] blocks = new Block[16][16][16];

    public Chunk(int x, int y, int z) {
        this.chunkX = x;
        this.chunkY = y;
        this.chunkZ = z;


        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    blocks[i][j][k] = air;
                }
            }
        }

//
        Random random = new Random();
        if(y < 0) {
            for (int i = 0; i < 16; i++) {
                setXYArea(Block.STONE, i);
            }
        }
        else if(y == 0) {
            for (int i = 0; i < 12; i++) {
                setXYArea(Block.STONE, i);
            }
            for (int i = 12; i < 15; i++) {
                setXYArea(Block.DIRT, i);

            }
            setXYArea(Block.GRASS, 15);
        } else {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    if(random.nextInt(40)==0) {
                        blocks[i][0][j] = Block.WOOD;
                        blocks[i][1][j] = Block.WOOD;
                        blocks[i][2][j] = Block.WOOD;

                        blocks[i][3][j] = Block.WOOD;

                        blocks[i][4][j] = Block.LEAVES;
                        try {
                            blocks[i][4][j + 1] = Block.LEAVES;
                            blocks[i][4][j - 1] = Block.LEAVES;
                            blocks[i + 1][4][j] = Block.LEAVES;
                            blocks[i - 1][4][j] = Block.LEAVES;

                            blocks[i][5][j] = Block.LEAVES;
                            blocks[i][5][j + 1] = Block.LEAVES;
                            blocks[i][5][j - 1] = Block.LEAVES;
                            blocks[i + 1][5][j] = Block.LEAVES;
                            blocks[i - 1][5][j] = Block.LEAVES;
                        } catch (Exception e) {}
                    }
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
    }

    public void setWorld(Planet world) {
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

    public void rebuild() {
        if(!rebuilding) {
            rebuilding = true;
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    for (int k = 0; k < 16; k++) {
                        Block block = blocks[i][j][k];
                        block.rebuild(this, i, j, k);
                    }
                }
            }
            rebuilding = false;
        }
    }

    public void render() {
        Player player = SBC.getPlayer();

        int cX = player.getChunkX();
        int cY = player.getChunkY();
        int cZ = player.getChunkZ();

        if(cX > getChunkX() - 3 && cX < getChunkX() + 3) {
            if(cZ > getChunkZ() - 3 && cZ < getChunkZ() + 3) {
                for(Runnable runnable : renderOperations) {
                    runnable.run();
                }
            }
        }
    }
}
