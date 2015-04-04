package com.daexsys.siximpl.world.io;

import com.daexsys.siximpl.world.block.Block;
import com.daexsys.siximpl.world.chunk.Chunk;
import com.daexsys.siximpl.world.planet.Planet;

import java.io.*;

public class ChunkSaver {
    public static void save(String worldName, Planet planet, Chunk chunk) {
        try {
            String fileLocation = worldName + "/" + planet;
            fileLocation += chunk.getChunkX() + "_" + chunk.getChunkY() + "_" + chunk.getChunkZ();

            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileLocation));
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

            int point = 0;
            int instancesFound = 0;
            Block blockType = null;

            // For each block in the chunk
            while(point < 4096) {
                // Get the coords of the block we're focusing on
                int inner = point % 256;
                int x = inner % 16;
                int y = point / 256;
                int z = inner / 16;

                // Get the block at this location
                Block currentBlock = chunk.getBlock(x, y, z);

                // If this is a new block type!
                if(blockType != currentBlock) {
                    // If the currently focused-upon block != null (i.e. we haven't just started)
                    if(currentBlock != null) {
                        // Write the strand of blocks to a file
                        try {
                            dataOutputStream.writeByte(currentBlock.getID());
                            dataOutputStream.writeInt(instancesFound);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // Set the focused block type to be the current block type, and set the amount found to 1.
                    blockType = currentBlock;
                    instancesFound = 1;
                } else {
                    // If this is the same type of block we've been seeing for a while.
                    instancesFound++;
                }
                point++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
