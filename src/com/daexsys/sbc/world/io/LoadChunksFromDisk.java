package com.daexsys.sbc.world.io;

import com.daexsys.sbc.net.DecodeChunk;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.PlanetCoordinate;

import java.io.*;

public class LoadChunksFromDisk {
    private String worldName;
    private PlanetCoordinate planetCoordinate;

    public LoadChunksFromDisk(String worldName, PlanetCoordinate planetCoordinate) {
        this.worldName = worldName;
        this.planetCoordinate = planetCoordinate;
    }

    public String getWorldName() {
        return worldName;
    }

    public PlanetCoordinate getPlanetCoordinate() {
        return planetCoordinate;
    }

    public Chunk load(int x, int y, int z) throws FileNotFoundException {
        String fileName =
                /* World */
            getWorldName() + "/" +
                /* Planet */
                    getPlanetCoordinate().getX() + "_"+getPlanetCoordinate().getY()+"_"+getPlanetCoordinate().getZ() +
                /* Chunk */
                    "/" + x + "_" + y + "_" + z + ".dat";

        DecodeChunk decodeChunk = new DecodeChunk(x, y, z, new DataInputStream(new FileInputStream(new File(fileName))));
        decodeChunk.decode();
        return decodeChunk.getChunk();
    }
}
