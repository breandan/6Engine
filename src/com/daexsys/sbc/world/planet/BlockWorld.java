package com.daexsys.sbc.world.planet;

import com.daexsys.sbc.world.block.Block;

public interface BlockWorld {
    public Block getBlock(int x, int y, int z);
}
