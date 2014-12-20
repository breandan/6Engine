package com.daexsys.sbc.world;

public class PlanetCoordinate {
    private long x;
    private long y;
    private long z;

    public PlanetCoordinate(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof PlanetCoordinate) {
            PlanetCoordinate coordinate = (PlanetCoordinate) object;

            return getX() == coordinate.getX() &&
                    getY() == coordinate.getY() &&
                    getZ() == coordinate.getZ();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (int)(x * 32) + (int)(y * 16) + (int)(z * 8);
    }
}
