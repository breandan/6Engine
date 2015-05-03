package com.daexsys.siximpl.world.planet;

import com.daexsys.siximpl.world.block.Block;

public interface BlockWorld {
    public Block getBlock(int x, int y, int z);
}
