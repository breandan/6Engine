package com.daexsys.sbc.world.block;

import com.daexsys.sbc.world.block.Block;
import com.daexsys.sbc.world.chunk.Chunk;

public class Air extends Block {
    public Air() {
        super(0,0);
    }

    @Override
    public void rebuild(Chunk chunk, int x, int y, int z) {}
}
