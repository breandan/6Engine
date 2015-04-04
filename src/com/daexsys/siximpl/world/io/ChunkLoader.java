package com.daexsys.siximpl.world.io;

import com.daexsys.siximpl.world.chunk.Chunk;
import com.daexsys.siximpl.world.planet.Planet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ChunkLoader {
    /**
     * Loads a chunk from the filesystem.
     * @param save the name of the saved game
     * @param planet the planet the chunk is on
     * @param chunkX the x coordinate of the chunk
     * @param chunkY the y coordinate of the chunk
     * @param chunkZ the z coordinate of the chunk
     * @return the chunk that was loaded
     */
    public static Chunk loadChunk(String save, Planet planet, int chunkX, int chunkY, int chunkZ) {
        String location = save + " " + planet;
        location += chunkX + "-" + chunkY + "-" + chunkZ;

        Chunk chunk = new Chunk(chunkX, chunkY, chunkZ);
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(location)));

            int point = 0;

            while(point < 4096) {
                try {
                    int id = dataInputStream.readByte(); // Block id
                    int amount = dataInputStream.readInt();

                    int num = 0;

                    while (num < amount) {


                        num++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Get the coords of the block we're focusing on
                int inner = point % 256;
                int x = inner % 16;
                int y = point / 256;
                int z = inner / 16;

//                chunk.setBlock(x, y, z);


                point++;
            }
            // TODO: Load chunk data into chunk object
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return chunk;
    }
}
