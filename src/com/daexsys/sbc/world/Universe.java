package com.daexsys.sbc.world;

import java.util.HashMap;
import java.util.Map;

public class Universe {
    private static Map<PlanetCoordinate, Planet> planetMap = new HashMap<PlanetCoordinate, Planet>();

    static {
        planetMap.put(new PlanetCoordinate(0,0,0), new Planet());
    }

    public static Planet getPlanet(int x, int y, int z) {
        return planetMap.get(new PlanetCoordinate(x, y, z));
    }

    public static boolean planetExists(int x, int y, int z) {
        return planetMap.containsKey(new PlanetCoordinate(x, y, z));
    }
}
