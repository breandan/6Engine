package com.daexsys.sbc.world.io;

import com.daexsys.sbc.net.EncodeChunk;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.PlanetCoordinate;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveChunksToDisk {
    private String worldName;
    private PlanetCoordinate planetCoordinate;

    public SaveChunksToDisk(String worldName, PlanetCoordinate planetCoordinate) {
        this.worldName = worldName;
        this.planetCoordinate = planetCoordinate;
    }

    public String getWorldName() {
        return worldName;
    }

    public PlanetCoordinate getPlanetCoordinate() {
        return planetCoordinate;
    }

    public void save(Chunk chunk) throws IOException {
        String fileName =
                /* World */
                getWorldName() + "/" +
                /* Planet */
                getPlanetCoordinate().getX() + "_" + getPlanetCoordinate().getY()+"_" + getPlanetCoordinate().getZ() +
                /* Chunk */
                "/" + chunk.getChunkX() + "_" + chunk.getChunkY() + "_" + chunk.getChunkZ()+".dat";

        File file = new File(fileName);
        file.createNewFile();

        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
        EncodeChunk encodeChunk = new EncodeChunk(chunk, dataOutputStream);
        encodeChunk.encode();
    }
}
