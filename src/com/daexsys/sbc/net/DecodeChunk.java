package com.daexsys.sbc.net;

import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Decode a 16 x 16 x 16 chunk of voxels.
 */
public class DecodeChunk {
    private Chunk chunk;
    private DataInputStream dataInputStream;

    public DecodeChunk(int x, int y, int z, DataInputStream dataInputStream) {
        this.chunk = new Chunk(x, y, z);
        this.dataInputStream = dataInputStream;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void decode() {
        int distance = 0;

        boolean keepDecoding = true;
        while(keepDecoding) {
            try {
                short length = dataInputStream.readShort();
                byte id = dataInputStream.readByte();

                for (int i = distance; i < distance + length; i++) {
                    set(i, id);
                }

                System.out.println(chunk.getChunkX() + " " +chunk.getChunkY() + " " + chunk.getChunkZ() + " " +length + " "+id);

                distance = distance + length;

                if(distance >= 4096) return;
            } catch (IOException e) {
                keepDecoding = false;
            }
        }
    }

    public void set(int i, byte id) {
        int y = i / 256;

        int remainder = y % 256;

        int z = remainder / 16;
        int x = remainder % 16;

        getChunk().setBlock(x, y, z, Block.getBlockByID(id));
    }
}
