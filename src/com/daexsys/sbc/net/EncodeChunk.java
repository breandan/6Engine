package com.daexsys.sbc.net;

import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;

import java.io.DataOutputStream;
import java.io.IOException;

public class EncodeChunk {
    private Chunk chunk;
    private DataOutputStream dataOutputStream;

    public EncodeChunk(Chunk chunk, DataOutputStream dataOutputStream) {
        this.chunk = chunk;
        this.dataOutputStream = dataOutputStream;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void encode() throws IOException {
        Block previousBlock = null;
        short strip = 1;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    Block block = chunk.getBlock(k, i, j);

                    if(block == previousBlock) {
                        strip++;
                        System.out.println(block.getID() + " " + strip);
                    } else {
                        if(previousBlock != null) {
                            System.out.println("O: " + strip + " " + previousBlock.getID());
                            dataOutputStream.writeByte(previousBlock.getID());
                            dataOutputStream.writeShort(strip);
                        }

                        // Set strip type for this block
                        strip = 1;
                    }

                    previousBlock = block;
                }
            }
        }
    }
}
