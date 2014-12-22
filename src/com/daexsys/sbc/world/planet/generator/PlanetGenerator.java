package com.daexsys.sbc.world.planet.generator;

import com.daexsys.sbc.world.block.Air;
import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.Planet;
import com.daexsys.sbc.world.planet.PlanetType;

public class PlanetGenerator {
    private Planet planet;

    public PlanetGenerator(Planet planet) {
        this.planet = planet;
    }

    public void generate(int x, int y, int z) {
        // Get this planet's type.
        PlanetType planetType = getPlanet().getPlanetType();

        // Create the chunk object.
        Chunk chunk = new Chunk(x, y, z);

        if(planetType == PlanetType.GRASSY) {
            if(y == 0) {
                for (int i = 0; i < 16; i++) {
                    chunk.setXYArea(Block.DIRT, i);
                }

                chunk.setXYArea(Block.GRASS, 15);
            } else if(y > 0) {
                for (int i = 0; i < 16; i++) {
                    chunk.setXYArea(new Air(), i);
                }
            }
                else {
                    for (int i = 0; i < 16; i++) {
                        chunk.setXYArea(Block.STONE, i);
                    }
                }
        }

        // Rebuild chunk render geometry.
        chunk.rebuild();

        // Add the chunk to it's planet.
        planet.addChunk(chunk);
    }

    public Planet getPlanet() {
        return planet;
    }
}
