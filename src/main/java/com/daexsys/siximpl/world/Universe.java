package com.daexsys.siximpl.world;

import com.daexsys.siximpl.world.planet.Planet;
import com.daexsys.siximpl.world.planet.PlanetCoordinate;
import com.daexsys.siximpl.world.planet.PlanetType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Universe {
    private Map<PlanetCoordinate, Planet> planetMap = new HashMap<PlanetCoordinate, Planet>();

    private Planet starterPlanet;

    public Universe() {
        starterPlanet = new Planet(new Random().nextLong(), PlanetType.GRASSY);
        planetMap.put(new PlanetCoordinate(0,0,0), starterPlanet);
    }

    public Planet getPlanetAt(int x, int y, int z) {
        return planetMap.get(new PlanetCoordinate(x, y, z));
    }

    public boolean planetExists(int x, int y, int z) {
        return planetMap.containsKey(new PlanetCoordinate(x, y, z));
    }

    public Planet getStarterPlanet() {
        return starterPlanet;
    }
}
