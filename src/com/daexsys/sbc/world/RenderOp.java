package com.daexsys.sbc.world;

public class RenderOp implements Runnable {
    private Runnable runnable;
    private BlockFace face;

    private int x;
    private int y;
    private int z;

    public RenderOp(Runnable runnable, float x, float y, float z, BlockFace face) {
        this.x = new Float(x).intValue();
        this.y = new Float(y).intValue();
        this.z = new Float(z).intValue();
        this.runnable = runnable;
        this.face = face;
    }

    @Override
    public void run() {
//        if(face == Face.TOP) {
//            if(y < DepthZ.getCamera().getY()) {
//                runnable.run();
//            }
//        } else if(face == Face.BOTTOM) {
//            if(y > DepthZ.getCamera().getY()) {
//                runnable.run();
//            }
//        }



//        if(face == Face.FRONT) {
//            if(z > DepthZ.getCamera().getRealZ()) {
//                runnable.run();
//            }
//        } else if(face == Face.BACK) {
//            if(z < DepthZ.getCamera().getRealZ()) {
//                runnable.run();
//            }
//        } else if(face == Face.LEFT) {
//            if(x > DepthZ.getCamera().getRealX()) {
//                runnable.run();
//            }
//        } else if(face == Face.RIGHT) {
//            if(x < DepthZ.getCamera().getRealX()) {
//                runnable.run();
//            }
//        } else
        {
            runnable.run();
        }
    }
}
