package com.daexsys.siximpl;

import com.daexsys.sixapi.SixBuffer;
import com.daexsys.sixapi.SixCache;

public class MengerGenerator {
    private int initLevel = 0;
    public SixCache sixCache;

    public MengerGenerator(String block, int level, int x, int y, int z) {
        long startTime = System.currentTimeMillis();
        this.initLevel = level;

        int totalSize = new Double(Math.pow(3, initLevel)).intValue();

        sixCache = new SixBuffer(totalSize, totalSize, totalSize);

        for (int i = 0; i < totalSize; i++) {
            for (int j = 0; j < totalSize; j++) {
                for (int k = 0; k < totalSize; k++) {
                    sixCache.alterBlockAt(block, i, j, k);

                }
            }
        }

        genAt(sixCache, block, totalSize, initLevel, 0, 0, 0);

        System.out.println("Construction complete. Level " + level + " menger sponge constructed in " + (System.currentTimeMillis() - startTime) + " milliseconds.");
    }

    public int getInitLevel() {
        return initLevel;
    }

    public SixCache getSixCache() {
        return sixCache;
    }

    public void genAt(SixCache sixCache, String block, int size, int level, int x, int y, int z) {
        int width = size / 3;

        if(level == 0) {
            return;
        }

        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                for (int k = z; k < z + size; k++) {
                    if(((i > (x - 1) + width && i < x + width * 2) && (j > (y-1) + width && j < y + width * 2)) ||
                            ((j > (y-1) + width && j < y + width * 2) &&(k > (z-1) + width && k < z + width * 2)) ||
                            ((k > (z-1) + width && k < z + width * 2) &&(i > (x - 1) + width && i < x + width * 2))) {
                        try {
                            sixCache.alterBlockAt("0", i, j, k);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        genAt(sixCache, block, width, level - 1, x,                    y,                    z);
        genAt(sixCache, block, width, level - 1, x + width,            y,                    z);
        genAt(sixCache, block, width, level - 1, x + width * 2,        y,                    z);

        genAt(sixCache, block, width, level - 1, x,                    y + width * 2,        z);
        genAt(sixCache, block, width, level - 1, x + width,            y + width * 2,        z);
        genAt(sixCache, block, width, level - 1, x + width * 2,        y + width * 2,        z);


        genAt(sixCache, block, width, level - 1, x,                    y,                    z + width * 2);
        genAt(sixCache, block, width, level - 1, x + width,            y,                    z + width * 2);
        genAt(sixCache, block, width, level - 1, x + width * 2,        y,                    z + width * 2);

        genAt(sixCache, block, width, level - 1, x,                    y + width * 2,        z + width * 2);
        genAt(sixCache, block, width, level - 1, x + width,            y + width * 2,        z + width * 2);
        genAt(sixCache, block, width, level - 1, x + width * 2,        y + width * 2,        z + width * 2);


        genAt(sixCache, block, width, level - 1, x,                    y ,                   z + width);
        genAt(sixCache, block, width, level - 1, x,                    y + width * 2,        z + width);

        genAt(sixCache, block, width, level - 1, x + width * 2,        y,                    z + width);
        genAt(sixCache, block, width, level - 1, x + width * 2,        y + width * 2,        z + width);


        genAt(sixCache, block, width, level - 1, x,                     y +width,                   z);
        genAt(sixCache, block, width, level - 1, x +width * 2,            y+ width,                   z);

        genAt(sixCache, block, width, level - 1, x,                   y + width,                   z + width * 2);
        genAt(sixCache, block, width, level - 1, x + width * 2,            y + width,                   z + width * 2);
    }

//    genAt(sixCache, block, width, level - 1, x,                    y,                    z);
//    genAt(sixCache, block, width, level - 1, x + width,            y,                    z);
//    genAt(sixCache, block, width, level - 1, x + width * 2,        y,                    z);
//
//    genAt(sixCache, block, width, level - 1, x,                    y + width * 2,        z);
//    genAt(sixCache, block, width, level - 1, x + width,            y + width * 2,        z);
//    genAt(sixCache, block, width, level - 1, x + width * 2,        y + width * 2,        z);
//
//
//    genAt(sixCache, block, width, level - 1, x,                    y,                    z + width * 2);
//    genAt(sixCache, block, width, level - 1, x + width,            y,                    z + width * 2);
//    genAt(sixCache, block, width, level - 1, x + width * 2,        y,                    z + width * 2);
//
//    genAt(sixCache, block, width, level - 1, x,                    y + width * 2,        z + width * 2);
//    genAt(sixCache, block, width, level - 1, x + width,            y + width * 2,        z + width * 2);
//    genAt(sixCache, block, width, level - 1, x + width * 2,        y + width * 2,        z + width * 2);
//
//
//    genAt(sixCache, block, width, level - 1, x,                    y ,                   z + width);
//    genAt(sixCache, block, width, level - 1, x,                    y + width * 2,        z + width);
//
//    genAt(sixCache, block, width, level - 1, x + width * 2,        y,                    z + width);
//    genAt(sixCache, block, width, level - 1, x + width * 2,        y + width * 2,        z + width);
//
//
//    genAt(sixCache, block, width, level - 1, 0,                     width,                   0);
//    genAt(sixCache, block, width, level - 1, width * 2,            width,                   0);
//
//    genAt(sixCache, block, width, level - 1, 0,                    width,                   width * 2);
//    genAt(sixCache, block, width, level - 1, width * 2,            width,                   width * 2);
}
