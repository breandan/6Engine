package com.daexsys.siximpl;

public class BlockChange {
    private int x = 0;
    private int y = 0;
    private int z = 0;

    private byte oldID;
    private byte newID;

    public BlockChange(int x, int y, int z, byte oldID, byte newID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.oldID = oldID;
        this.newID = newID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public byte getOldID() {
        return oldID;
    }

    public byte getNewID() {
        return newID;
    }
}
