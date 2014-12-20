package com.daexsys.sbc.world;

import com.daexsys.sbc.world.planet.Planet;
import com.daexsys.sbc.world.planet.PlanetCoordinate;

import java.util.HashMap;
import java.util.Map;

public class Universe {
    private Map<PlanetCoordinate, Planet> planetMap = new HashMap<PlanetCoordinate, Planet>();

    private Planet starterPlanet;

    public Universe() {
        starterPlanet = new Planet();
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
