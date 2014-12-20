package com.daexsys.sbc.world.planet.generator;

import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;
import com.daexsys.sbc.world.planet.Planet;
import com.daexsys.sbc.world.planet.PlanetType;

public class PlanetGenerator {
    private Planet planet;

    public PlanetGenerator(Planet planet) {
        this.planet = planet;
    }

    public void generate(long seed, int x, int y, int z) {
        PlanetType planetType = getPlanet().getPlanetType();

        Chunk chunk = new Chunk(x, y, z);

        Block stone = null;

        if(planetType == PlanetType.GRASSY) {
            // If fully underground chunk
            if(y < 0) {
                for (int i = 0; i < 16; i++) {
                    chunk.setXYArea(stone, i);
                }
            } else {

            }
        }
    }

    public Planet getPlanet() {
        return planet;
    }
}
