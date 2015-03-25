package com.daexsys.sbc;

import com.daexsys.sbc.world.block.Block;

public class BlockCoord {
    public int x = 0;
    public int y = 0;
    public int z = 0;

    public BlockCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}
