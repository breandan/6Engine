package com.daexsys.siximpl.world.block;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used in the api to manage blocks
 */
public class BlockManager {
    private static Map<String, Block> map = new HashMap<String, Block>();

    public static Block getBlockType(String name) {
        return map.get(name);
    }

    public static void setBlockType(String name, Block block) {
        map.put(name, block);
    }
}
