package com.daexsys.siximpl.world.block;

import com.daexsys.siximpl.world.chunk.Chunk;

public class Air extends Block {
    public Air() {
        super(0,-1);
    }

    @Override
    public void build(Chunk chunk, int x, int y, int z) {}
}
